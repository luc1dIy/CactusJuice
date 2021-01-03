package wtf.jef.cactusjuice.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import de.tr7zw.nbtapi.NBTItem;
import wtf.jef.cactusjuice.CactusJuice;
import wtf.jef.cactusjuice.commands.CommandHelper;
import wtf.jef.cactusjuice.managers.ItemManager;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class ItemCactusTap extends CactusJuiceItem implements Listener {
	private CactusJuice plugin;

	private ShapelessRecipe recipe;
	private ItemStack item;

	private Map<UUID, Boolean> usingTap;
	// somehow, if you click fast enough, you can tap twice
	// before the event handler completes without consuming durab

	public ItemCactusTap(CactusJuice plugin) {
		this.plugin = plugin;
		this.usingTap = new HashMap<UUID, Boolean>();

		ItemStack tap = new ItemStack(Material.BOWL);
		ItemMeta tapMeta = tap.getItemMeta();
		tapMeta.setDisplayName(ChatColor.YELLOW + this.getName());
		tapMeta.setLore(this.getLore());
		tap.setItemMeta(tapMeta);
		this.item = tap;

		ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(plugin, "cj.cactus_tap"), tap);
		recipe.addIngredient(Material.STICK);
		recipe.addIngredient(new MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.CRIMSON_PLANKS,
				Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS,
				Material.WARPED_PLANKS));
		this.recipe = recipe;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public ItemStack getItem() {
		ItemStack newItem = this.item.clone();
		NBTItem newItemNBT = new NBTItem(newItem);

		newItemNBT.setDouble("cj.max_durability", plugin.getConfig().getDouble("items.cactus_tap.max_durability"));
		newItemNBT.setDouble("cj.durability", newItemNBT.getDouble("cj.max_durability"));
		newItemNBT.setString("cj.type", this.getName());
		newItemNBT.setString("cj.uuid", UUID.randomUUID().toString());
		// ^^ prevent stacking

		return newItemNBT.getItem();
	}

	@Override
	public List<String> getLore() {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Used to obtain cactus juice.");
		lore.add(ChatColor.GREEN + "Right click" + ChatColor.GRAY + " on a cactus!");

		return lore;
	}

	@Override
	public String getName() {
		return "Cactus Tap";
	}

	@Override
	public Permission getPermission() {
		return PermissionManager.getPermission("cactusjuice.item.cactus_tap");
	}

	@Override
	public Recipe getRecipe() {
		return this.recipe;
	}

	@EventHandler
	public void onCraftingTap(CraftItemEvent event) {
		ItemStack result = event.getInventory().getResult();
		HumanEntity player = event.getView().getPlayer();
		if (result != null && result.getType() != Material.AIR && result.isSimilar(this.item)
				&& !player.hasPermission(this.getPermission())) {
			event.getInventory().setResult(new ItemStack(Material.AIR));
			event.setCancelled(true);
			player.sendMessage(CommandHelper.responseFormatRed + "You lack the permission to craft " + ChatColor.YELLOW
					+ this.getName());
		}
	}

	@EventHandler
	public void onCraftTap(InventoryClickEvent event) {
		if (event.isCancelled() || event.getSlotType() != SlotType.RESULT) {
			return;
		}

		ItemStack currentItem = event.getCurrentItem();

		if (currentItem == null || currentItem.getType() == Material.AIR || !currentItem.isSimilar(this.item)) {
			return;
		}

		event.setCurrentItem(this.getItem());
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();

		if (this.usingTap.containsKey(playerId) || !player.hasPermission(this.getPermission())) {
			return;
		}

		ItemStack tap = event.getItem();

		if (tap == null || tap.getType() == Material.AIR) {
			return;
		}

		NBTItem tapNBT = new NBTItem(tap);
		Block cactus = event.getClickedBlock();

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && cactus.getType() == Material.CACTUS
				&& tapNBT.hasKey("cj.type") && tapNBT.getString("cj.type").equals(this.getName())) {
			this.usingTap.put(playerId, true);
			FileConfiguration config = plugin.getConfig();

			double cost = config.getDouble("items.cactus_tap.durability_cost");

			double durability = tapNBT.getDouble("cj.durability");

			if ((durability - cost) > 0) {
				tapNBT.setDouble("cj.durability", durability - cost);
				ItemStack editedTap = tapNBT.getItem();
				ItemMeta tapMeta = editedTap.getItemMeta();
				tapMeta.setDisplayName(ChatColor.YELLOW + this.getName() + ChatColor.GRAY + " (" + (durability - cost)
						+ "/" + tapNBT.getDouble("cj.max_durability") + ")");
				editedTap.setItemMeta(tapMeta);

				if (config.getBoolean("items.cactus_tap.break_cactus")) {
					cactus.setType(Material.AIR);
				} else if (config.getBoolean("items.cactus_tap.drop_cactus")) {
					cactus.breakNaturally();
				}

				cactus.getWorld().spawnParticle(Particle.WATER_SPLASH,
						cactus.getLocation().clone().add(Math.random(), 0.5, Math.random()), 5);

				PlayerInventory inventory = player.getInventory();
				inventory.setItem(inventory.getHeldItemSlot(), editedTap);
				ItemStack cactusJuice = ItemManager.getItem("Cactus Juice").getItem();

				if (inventory.firstEmpty() == -1) {
					player.getWorld().dropItemNaturally(player.getLocation(), cactusJuice);
				} else {
					inventory.addItem(cactusJuice);
				}
			} else {
				player.getInventory().removeItem(tap);
				player.sendMessage(ChatColor.YELLOW + "Your tap broke from using it too much!");
			}

			this.usingTap.replace(playerId, false);
			this.usingTap.remove(playerId);
		}
	}
}

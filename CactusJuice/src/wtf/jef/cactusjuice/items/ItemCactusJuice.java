package wtf.jef.cactusjuice.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import wtf.jef.cactusjuice.CactusJuice;
import wtf.jef.cactusjuice.commands.CommandHelper;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class ItemCactusJuice extends CactusJuiceItem implements Listener {
	private CactusJuice plugin;

	private ItemStack item;

	public ItemCactusJuice(CactusJuice plugin) {
		this.plugin = plugin;

		ItemStack cactusJuice = new ItemStack(Material.POTION);
		PotionMeta cactusJuiceMeta = (PotionMeta) cactusJuice.getItemMeta();
		cactusJuiceMeta.setDisplayName(ChatColor.DARK_GREEN + this.getName());
		cactusJuiceMeta.setLore(this.getLore());
		cactusJuiceMeta.setColor(Color.fromRGB(255, 255, 255));
		cactusJuiceMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		cactusJuice.setItemMeta(cactusJuiceMeta);

		this.item = cactusJuice;

		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	@Override
	public ItemStack getItem() {
		return this.item.clone();
	}

	@Override
	public List<String> getLore() {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Drink cactus juice. It'll quench ya!");
		lore.add(ChatColor.GREEN + "Nothing's quenchier. It's the quenchiest!");

		return lore;
	}

	@Override
	public String getName() {
		return "Cactus Juice";
	}

	@Override
	public Permission getPermission() {
		return PermissionManager.getPermission("cactusjuice.item.cactus_juice");
	}

	@Override
	public Recipe getRecipe() {
		return null;
	}

	@EventHandler
	public void onPotionConsumption(PlayerItemConsumeEvent event) {
		if (event.isCancelled() || !event.getItem().isSimilar(this.item)) {
			return;
		}

		Player player = event.getPlayer();

		if (!player.hasPermission(this.getPermission())) {
			event.setCancelled(true);
			player.sendMessage(CommandHelper.responseFormatRed + "You lack the permission to consume "
					+ ChatColor.YELLOW + this.getName());
			return;
		}

		int duration = this.plugin.getConfig().getInt("items.cactus_juice.duration"); // to allow for config
																						// hot-reloading
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, 0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 2));
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0));
	}
}

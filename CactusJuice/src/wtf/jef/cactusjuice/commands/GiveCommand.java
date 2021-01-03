package wtf.jef.cactusjuice.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;

import wtf.jef.cactusjuice.items.CactusJuiceItem;
import wtf.jef.cactusjuice.managers.ItemManager;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class GiveCommand extends SubCommand {

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(CommandHelper.responseFormatRed + "Cannot give items to non-player.");
			return;
		} else if (args.length < 1) {
			sender.sendMessage(CommandHelper.responseFormatRed + "You must provide an item name.");
			return;
		}

		String itemName = args[0].replace('_', ' ');
		CactusJuiceItem foundItem = null;

		for (Entry<String, CactusJuiceItem> item : ItemManager.getItems().entrySet()) {
			if (item.getKey().equalsIgnoreCase(itemName)) {
				foundItem = item.getValue();
				break;
			}
		}

		if (foundItem == null) {
			sender.sendMessage(CommandHelper.responseFormatRed + "No such item.");
		} else {
			Player player = (Player) sender;
			PlayerInventory inventory = player.getInventory();
			ItemStack item = foundItem.getItem();

			if (inventory.firstEmpty() == -1) {
				player.getWorld().dropItemNaturally(player.getLocation(), item);
			} else {
				inventory.addItem(item);
			}

			sender.sendMessage(CommandHelper.responseFormatGreen + "Spawned " + ChatColor.YELLOW + foundItem.getName());
		}
	}

	@Override
	public String getDescription() {
		return "Spawns an item and places it in your inventory.";
	}

	@Override
	public String getName() {
		return "give";
	}

	@Override
	public Permission getPermission() {
		return PermissionManager.getPermission("cactusjuice.command.give");
	}

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] args) {
		List<String> suggestions = new ArrayList<String>();
		ItemManager.getItems().forEach((itemName, __) -> suggestions.add(itemName.replace(' ', '_')));

		if (args.length < 1) {
			return suggestions;
		} else if (args.length > 1) {
			return new ArrayList<String>();
		}

		return CommandHelper.filterSuggestions(CommandHelper.sortSuggestions(suggestions), args[0]);
	}

	@Override
	public String getUsage() {
		return "<item>";
	}
}

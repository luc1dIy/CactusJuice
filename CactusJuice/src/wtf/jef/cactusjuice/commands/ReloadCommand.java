package wtf.jef.cactusjuice.commands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import wtf.jef.cactusjuice.CactusJuice;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class ReloadCommand extends SubCommand {
	private static CactusJuice plugin;

	public ReloadCommand(CactusJuice plugin) {
		ReloadCommand.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		sender.sendMessage(CommandHelper.responseFormatGreen + "Reloading the config...");

		if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
			plugin.saveDefaultConfig();
		}

		plugin.reloadConfig();
		sender.sendMessage(CommandHelper.responseFormatGreen + "Reloaded the config!");
	}

	@Override
	public String getDescription() {
		return "Reloads the config.";
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public Permission getPermission() {
		return PermissionManager.getPermission("cactusjuice.command.reload");
	}

	@Override
	public String getUsage() {
		return "";
	}

}

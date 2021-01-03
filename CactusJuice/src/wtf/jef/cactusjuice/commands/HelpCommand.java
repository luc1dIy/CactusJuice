package wtf.jef.cactusjuice.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import wtf.jef.cactusjuice.managers.CommandManager;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class HelpCommand extends SubCommand {

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		if (args.length < 1) {
			List<SubCommand> subCommands = CommandManager.getCommands();
			subCommands.forEach((subCommand) -> sender.sendMessage(this.getHelpMessage(sender, label, subCommand)));
		} else if (CommandManager.getCommand(args[0]) == null) {
			sender.sendMessage(CommandHelper.responseFormatRed + "No such command.");
		} else {
			sender.sendMessage(this.getHelpMessage(sender, label, CommandManager.getCommand(args[0])));
		}
	}

	@Override
	public String getDescription() {
		return "Shows you a list of commands or how to use a specific command.";
	}

	private String getHelpMessage(CommandSender sender, String label, SubCommand subCommand) {
		return (sender.hasPermission(subCommand.getPermission()) ? CommandHelper.responseFormatGreen
				: CommandHelper.responseFormatRed) + "/" + label + " " + subCommand.getName() + " " + ChatColor.YELLOW
				+ subCommand.getUsage() + "\n> " + ChatColor.GRAY + subCommand.getDescription();
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public Permission getPermission() {
		return PermissionManager.getPermission("cactusjuice.command.help");
	}

	@Override
	public List<String> getSuggestions(CommandSender sender, String[] args) {
		List<String> suggestions = new ArrayList<String>();
		CommandManager.getCommands().forEach((subCommand) -> {
			if (sender.hasPermission(subCommand.getPermission())) {
				suggestions.add(subCommand.getName());
			}
		});

		CommandHelper.sortSuggestions(suggestions);

		if (args.length < 1) {
			return suggestions;
		} else if (args.length > 1) {
			return new ArrayList<String>();
		} else if (args.length == 1 || CommandManager.getCommand(args[0]) == null) {
			return CommandHelper.filterSuggestions(suggestions, args[0]);
		}

		return suggestions;
	}

	@Override
	public String getUsage() {
		return "[command]";
	};
}

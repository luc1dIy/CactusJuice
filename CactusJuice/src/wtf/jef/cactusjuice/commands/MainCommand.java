package wtf.jef.cactusjuice.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import wtf.jef.cactusjuice.managers.CommandManager;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class MainCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission(PermissionManager.getPermission("cactusjuice.command"))) {
			sender.sendMessage(CommandHelper.responseFormatRed + "You lack the permission to use this command.");
			return true;
		}

		SubCommand helpCommand = CommandManager.getCommand("help");

		if (args.length < 1) {
			if (sender.hasPermission(helpCommand.getPermission())) {
				helpCommand.execute(sender, label, new String[0]);
				return true;
			} else {
				sender.sendMessage(CommandHelper.responseFormatRed + "You lack the permission to use this command.");
				return true;
			}
		}

		SubCommand subCommand = CommandManager.getCommand(args[0]);
		String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

		if (subCommand == null) {
			sender.sendMessage(CommandHelper.responseFormatRed + "No such command.");
			return true;
		} else if (subCommand != null && !sender.hasPermission(subCommand.getPermission())) {
			sender.sendMessage(CommandHelper.responseFormatRed + "You lack the permission to use this command.");
			return true;
		}

		subCommand.execute(sender, label, commandArgs);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<String>();
		CommandManager.getCommands().forEach((subCommand) -> {
			if (sender.hasPermission(subCommand.getPermission())) {
				suggestions.add(subCommand.getName());
			}
		});

		if (args.length < 1) {
			return suggestions;
		} else if (args.length == 1 || CommandManager.getCommand(args[0]) == null) {
			return CommandHelper.sortSuggestions(CommandHelper.filterSuggestions(suggestions, args[0]));
		}

		return CommandManager.getCommand(args[0]).getSuggestions(sender, Arrays.copyOfRange(args, 1, args.length));
	}
}

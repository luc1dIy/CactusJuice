package wtf.jef.cactusjuice.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class SubCommand {
	public abstract void execute(CommandSender sender, String label, String[] args);

	public abstract String getDescription();

	public abstract String getName();

	public abstract Permission getPermission();

	public List<String> getSuggestions(CommandSender sender, String[] args) {
		return new ArrayList<String>();
	}

	public abstract String getUsage();;
}

package wtf.jef.cactusjuice.managers;

import java.util.ArrayList;
import java.util.List;

import wtf.jef.cactusjuice.CactusJuice;
import wtf.jef.cactusjuice.commands.GiveCommand;
import wtf.jef.cactusjuice.commands.HelpCommand;
import wtf.jef.cactusjuice.commands.MainCommand;
import wtf.jef.cactusjuice.commands.ReloadCommand;
import wtf.jef.cactusjuice.commands.SubCommand;

public class CommandManager {
	// TODO change subCommands into a map in the future because
	// I didn't add aliases as I originally thought I was going to do
	private static List<SubCommand> subCommands = new ArrayList<SubCommand>();

	public static SubCommand getCommand(String name) {
		for (int i = 0; i < subCommands.size(); i++) {
			SubCommand subCommand = subCommands.get(i);

			if (name.equalsIgnoreCase(subCommand.getName())) {
				return subCommand;
			}
		}

		return null;
	}

	public static List<SubCommand> getCommands() {
		return subCommands;
	}

	public CommandManager(CactusJuice plugin) {
		subCommands.add(new HelpCommand());
		subCommands.add(new GiveCommand());
		subCommands.add(new ReloadCommand(plugin));

		plugin.getCommand("cactusjuice").setExecutor(new MainCommand());
	}
}

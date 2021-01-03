package wtf.jef.cactusjuice;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import wtf.jef.cactusjuice.managers.CommandManager;
import wtf.jef.cactusjuice.managers.ItemManager;
import wtf.jef.cactusjuice.managers.PermissionManager;

public class CactusJuice extends JavaPlugin {
	private static CactusJuice plugin;
	private static Logger log;

	@Override
	public void onDisable() {
		log.info("Disabled CactusJuice.");
	}

	@Override
	public void onEnable() {
		CactusJuice.plugin = this;
		// TagsPlugin.listener = new TagsListener();
		CactusJuice.log = this.getLogger();

		if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
			plugin.saveDefaultConfig();
		}

		plugin.reloadConfig();

		new PermissionManager(plugin);
		new ItemManager(plugin);
		new CommandManager(plugin);

		// pluginManager.registerEvents(listener, plugin);
	}
}

package wtf.jef.cactusjuice.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import wtf.jef.cactusjuice.CactusJuice;

public class PermissionManager {
	private static Map<String, Permission> registry = new HashMap<String, Permission>();

	public static Permission getPermission(String node) {
		return PermissionManager.registry.get(node);
	}

	public PermissionManager(CactusJuice plugin) {
		Map<String, PermissionDefault> toRegister = new HashMap<String, PermissionDefault>();

		// commands
		toRegister.put("cactusjuice.command", PermissionDefault.TRUE);

		toRegister.put("cactusjuice.command.help", PermissionDefault.TRUE);
		toRegister.put("cactusjuice.command.give", PermissionDefault.OP);
		toRegister.put("cactusjuice.command.reload", PermissionDefault.OP);

		// items
		toRegister.put("cactusjuice.item.cactus_tap", PermissionDefault.TRUE);
		toRegister.put("cactusjuice.item.cactus_juice", PermissionDefault.TRUE);

		PluginManager pluginManager = plugin.getServer().getPluginManager();
		toRegister.forEach((node, permDefault) -> {
			Permission perm = new Permission(node, permDefault);
			pluginManager.addPermission(perm);
			PermissionManager.registry.put(node, perm);
		});
	}
}
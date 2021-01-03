package wtf.jef.cactusjuice.items;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permission;

public abstract class CactusJuiceItem {
	public abstract ItemStack getItem();

	public abstract List<String> getLore();

	public abstract String getName();

	public abstract Permission getPermission();

	public abstract Recipe getRecipe();
}

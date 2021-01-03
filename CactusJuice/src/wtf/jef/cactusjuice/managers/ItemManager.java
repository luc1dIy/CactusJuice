package wtf.jef.cactusjuice.managers;

import java.util.HashMap;
import java.util.Map;

import wtf.jef.cactusjuice.CactusJuice;
import wtf.jef.cactusjuice.items.CactusJuiceItem;
import wtf.jef.cactusjuice.items.ItemCactusJuice;
import wtf.jef.cactusjuice.items.ItemCactusTap;

public class ItemManager {
	private static Map<String, CactusJuiceItem> items;

	public static CactusJuiceItem getItem(String name) {
		return ItemManager.items.get(name);
	}

	public static Map<String, CactusJuiceItem> getItems() {
		return ItemManager.items;
	}

	public ItemManager(CactusJuice plugin) {
		ItemManager.items = new HashMap<String, CactusJuiceItem>();

		ItemCactusTap cactusTap = new ItemCactusTap(plugin);
		plugin.getServer().addRecipe(cactusTap.getRecipe());
		items.put("Cactus Tap", cactusTap);

		items.put("Cactus Juice", new ItemCactusJuice(plugin));
	}
}

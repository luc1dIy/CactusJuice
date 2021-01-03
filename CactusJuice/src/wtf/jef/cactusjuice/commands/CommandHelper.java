package wtf.jef.cactusjuice.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class CommandHelper {
	public static String responseFormatGreen = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "CJ " + ChatColor.GREEN;
	public static String responseFormatRed = ChatColor.DARK_RED + "" + ChatColor.BOLD + "CJ " + ChatColor.RED;

	public static List<String> filterSuggestions(List<String> suggestions, String match) {
		List<String> filtered = new ArrayList<String>();

		/*
		 * originally I was planning to just remove the filtered elements but since it's
		 * based on numerical indices, it might get fucky
		 */
		suggestions.forEach((suggestion) -> {
			if (suggestion.length() >= match.length()
					&& suggestion.substring(0, match.length()).equalsIgnoreCase(match)) {
				filtered.add(suggestion);
			}
		});

		return filtered;
	}

	/*
	 * why this works (lol): each character in a string has a different amount of
	 * bytes to represent it. less characters (every letter in the alphabet has a
	 * higher amount of bytes than its seceding letter) -> less bytes ->
	 * alphabetically sortable by size as well
	 */
	public static int getTotalBytes(String string) {
		byte[] bytes = string.getBytes();
		int total = 0;

		for (int i = 0; i < bytes.length; i++) {
			total += bytes[i];
		}

		return total;
	}

	public static List<String> sortSuggestions(List<String> suggestions) {
		suggestions.sort((string1, string2) -> getTotalBytes(string1) - getTotalBytes(string2));

		return suggestions; // for convenience
	}
}
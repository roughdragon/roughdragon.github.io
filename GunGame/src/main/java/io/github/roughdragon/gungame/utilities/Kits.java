package io.github.roughdragon.gungame.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Kits {

	public static List<Kits> allKits = new ArrayList<Kits>();
	public List<ItemStack> kitItems = new ArrayList<ItemStack>();
	
	public String name;
	public int displayItem;
	
	@SuppressWarnings("deprecation")
	public Kits(String name, List<String> items, int displayItem) {
		this.name = name;
		this.displayItem = displayItem;
		
		for(String s : items) {
			int id = 0, amount = 1;
			String displayName = "Unknown Item";
			if(s.contains(":")) {
				String[] splitItem = s.split(":");
				id = Integer.valueOf(splitItem[0].trim());
				amount = Integer.valueOf(splitItem[1].trim());
				displayName = splitItem[2].trim();	
			}
			ItemStack is = new ItemStack(id, amount);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayName);
			is.setItemMeta(im);
			this.kitItems.add(is);
				
		}
		allKits.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getDisplayItem() {
		ItemStack is = new ItemStack(displayItem);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
		
	public static Kits getKit(String name) {
		for(Kits k : allKits)
			if(name.equalsIgnoreCase(k.getName())) 
				return k;
		return null;
	}
		
	public static boolean isKit(String name) {
		for(Kits k : allKits) 
			if(name.equalsIgnoreCase(k.getName()))
				return true; 
		return false;
	}
	
}

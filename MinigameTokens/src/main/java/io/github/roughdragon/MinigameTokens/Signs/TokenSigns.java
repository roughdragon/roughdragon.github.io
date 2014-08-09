package io.github.roughdragon.MinigameTokens.Signs;

import io.github.roughdragon.MinigameTokens.TokenManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TokenSigns implements Listener {

	ChatColor RED = ChatColor.RED;
	ChatColor GRAY = ChatColor.GRAY;
	ChatColor GREEN = ChatColor.GREEN;
	ChatColor BLUE = ChatColor.BLUE;
	ChatColor YELLOW = ChatColor.YELLOW;
	
	@EventHandler
	public void onBlockPlace(SignChangeEvent event) {
		Player player = event.getPlayer();
		Sign sign = (Sign) event.getBlock().getState();
		
		String line1 = sign.getLine(0);
		String line2 = sign.getLine(1);
		String line3 = sign.getLine(2);
		String line4 = sign.getLine(3);
				
		if(line1.equalsIgnoreCase("[Shop]")) {
			if(line2.isEmpty()) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Token Shop Sign!");
				return;
			}
			if(line3.isEmpty()) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Token Shop Sign!");
				return;
			}
			if(line4.isEmpty()) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Token Shop Sign!");
				return;
			}
			Material item = Material.getMaterial(line2);
			if(item == null) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Item ID!");
				return;
			}
			try {
				Integer.parseInt(line3);
			} catch (NumberFormatException ex) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Item Quantity!");
				return;
			}
			try {
				Integer.parseInt(line4);
			} catch (NumberFormatException ex) {
				sign.setLine(1, RED + "[Shop]");
				player.sendMessage(RED + "Invalid Price!");
				return;
			}
			sign.setLine(1, BLUE + "[Shop]");
			sign.setLine(4, line4 + " Tokens");
			player.sendMessage(YELLOW + "You have created a Token Shop sign!");
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST)) {
			Sign sign = (Sign) block.getState();
			if(sign.getLine(0).equals(BLUE + "[Shop]")) {
				int itemID = Integer.parseInt(sign.getLine(1));
				int quantity = Integer.parseInt(sign.getLine(2));
				String priceString = sign.getLine(3);
				priceString.replace(" Tokens", "");
				int price = Integer.parseInt(priceString);
				if(TokenManager.getManager().enoughTokens(player, price)) {
					ItemStack item = new ItemStack(Material.getMaterial(itemID));
					item.setAmount(quantity);
					player.getInventory().addItem(item);
					TokenManager.getManager().takeTokens(player, price);
					player.sendMessage(YELLOW + "You spent " + GRAY + "[" + GREEN + price + GRAY + "]" + YELLOW + " tokens!");
					return;
				} else {
					player.sendMessage(RED + "You do not have enough tokens!");
					return;
				}
			}
		}
	}
	
}

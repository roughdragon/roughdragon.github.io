package io.github.roughdragon.MinigameTokens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokenCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[MinigameTokens] You must be a Player to use any /tokens command"); 
			return false;
		}
		
		Player player = (Player) sender;
		
		if(label.equalsIgnoreCase("tokens")) {
			if(args.length == 0) {
				int currentTokens = MinigameTokens.getInstance().getTokensConfig().getInt(player.getUniqueId().toString());
				player.sendMessage(ChatColor.WHITE + "You have " + ChatColor.GRAY + "[" + ChatColor.GREEN + currentTokens + ChatColor.GRAY + "]" + ChatColor.WHITE + " tokens!");
				return true;
			}
			if(args.length == 1) {
				// /tokens top
				return true;
			}
			if(args.length == 2) {
				// /tokens reset <player> DONE
				// /tokens get <player>
				if(args[0].equalsIgnoreCase("reset")) {
					if(player.hasPermission("mgtokens.reset") || player.isOp()) {
						Player resetPlayer = Bukkit.getServer().getPlayer(args[1]);
						if(resetPlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid Player!");
							return true;
						}
						TokenManager.getManager().resetTokens(resetPlayer);
						player.sendMessage(ChatColor.YELLOW + "You have reset " + ChatColor.GRAY + "[" + ChatColor.GREEN + resetPlayer.getName() + "'s" + ChatColor.GRAY + "]" + ChatColor.YELLOW + " tokens!");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "You do not have the required permission to use this command.");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("get")) {
					if(player.hasPermission("mgtokens.get") || player.isOp()) {
						Player getPlayer = Bukkit.getServer().getPlayer(args[1]);
						if(getPlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid Player!");
							return true;
						}
						int getTokens = TokenManager.getManager().getTokens(getPlayer);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + getPlayer.getName() + ChatColor.GRAY + "]" + ChatColor.YELLOW + " has " + ChatColor.GRAY + "[" + ChatColor.GREEN + getTokens + ChatColor.GRAY + "]" + ChatColor.YELLOW + " tokens!");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "You do not have the required permission to use this command.");
						return true;
					}
				}
			}
			if(args.length == 3) {
				// /tokens give <player> <amount>
				// /tokens take <player> <amount>
				// /tokens set <player> <amount>
				if(args[0].equalsIgnoreCase("give")) {
					if(player.hasPermission("mgtokens.give") || player.isOp()) {
						Player givePlayer = Bukkit.getServer().getPlayer(args[1]);
						if(givePlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid Player!");
							return true;
						}
						int amount;
						try {
							amount = Integer.parseInt(args[2]);
						} catch (NumberFormatException ex) {
							player.sendMessage(ChatColor.RED + "Invalid Token Amount!");
							return true;
						}
						TokenManager.getManager().giveTokens(givePlayer, amount);
						player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.GRAY + "[" + ChatColor.GREEN + amount + ChatColor.GRAY + "]" + ChatColor.YELLOW + " tokens to " + ChatColor.GRAY + "[" + ChatColor.GREEN + givePlayer.getName() + ChatColor.GRAY + "]" + ChatColor.YELLOW + "!");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "You do not have the required permission to use this command.");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("set")) {
					if(player.hasPermission("mgtokens.set") || player.isOp()) {
						Player setPlayer = Bukkit.getServer().getPlayer(args[1]);
						if(setPlayer == null) {
							player.sendMessage(ChatColor.RED + "Invalid Player!");
							return true;
						}
						int amount;
						try {
							amount = Integer.parseInt(args[2]);
						} catch (NumberFormatException ex) {
							player.sendMessage(ChatColor.RED + "Invalid Token Amount!");
							return true;
						}
						TokenManager.getManager().setTokens(setPlayer, amount);
						player.sendMessage(ChatColor.YELLOW + "You have set " + ChatColor.GRAY + "[" + ChatColor.GREEN + setPlayer.getName() + "'s" + ChatColor.GRAY + "]" + ChatColor.YELLOW + " tokens to " + ChatColor.GRAY + "[" + ChatColor.GREEN + amount + ChatColor.GRAY + "]" + ChatColor.YELLOW + "!");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "You do not have the required permission to use this command.");
						return true;
					}
				}
			}
		}
		
		return false;
	}

}

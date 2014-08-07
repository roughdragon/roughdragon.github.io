package io.github.roughdragon.playerinformer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Kick extends PlayerInformer implements Listener, CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("kick")) {
			if(sender.hasPermission("playerinformer.kick") || sender.isOp()) {
				if(args.length >= 2) {
					Player toBeKicked = Bukkit.getServer().getPlayer(args[0]);
					if(toBeKicked == null) {
						sender.sendMessage("The Player you have specified is INVALID!");
						return false;
					}
					
					String kickReason = "";
					for(int i = 2; i < args.length; i++) {
						String arg = args[i] + " ";
						kickReason = kickReason + arg;
					}
					
					String[] kickMessage = new String[3];
					
					kickMessage[0] = ChatColor.GOLD + "You have been KICKED!";
					kickMessage[1] = ChatColor.GREEN + "Reason: " + kickReason;
					kickMessage[2] = ChatColor.GREEN + "By: " + sender.getName();
					
					toBeKicked.kickPlayer(StringUtils.join(kickMessage, '\n'));
				}
				else if(args.length <=1) {
					sender.sendMessage(ChatColor.RED + "Usage: /kick <player> <reason>");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have the permission 'playerinformer.kick' to perform this command.");
				return false;
			}
		}
		return false;
	}
		
}
 
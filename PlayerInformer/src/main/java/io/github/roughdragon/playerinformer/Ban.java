package io.github.roughdragon.playerinformer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Ban extends PlayerInformer implements Listener, CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("ban")) {
			if(sender.hasPermission("playerinformer.ban") || sender.isOp()) {
				if(args.length >= 2) {
					Player toBeBanned = Bukkit.getServer().getPlayer(args[0]);
					if(toBeBanned == null) {
						sender.sendMessage("The Player you have specified is INVALID!");
						return false;
					}
					
					String banReason = "";
					for(int i = 2; i < args.length; i++) {
						String arg = args[i] + " ";
						banReason = banReason + arg;
					}
					
					toBeBanned.setBanned(true);				
					
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String date = format.format(now);
					
					String[] kickMessage = new String[5];
					
					kickMessage[0] = ChatColor.GOLD + "You have been BANNED from this Server!";
					kickMessage[1] = ChatColor.GREEN + "Reason: " + banReason;
					kickMessage[2] = ChatColor.GREEN + "By: " + sender.getName();
					kickMessage[3] = ChatColor.GREEN + "Date: " + date;
					kickMessage[4] = ChatColor.GOLD + "To Appeal, Go To: www.blackpulse.us";
					
					toBeBanned.kickPlayer(StringUtils.join(kickMessage, '\n'));
					
					getPiConfig().set(toBeBanned.getName() + ".reason", banReason);
					getPiConfig().set(toBeBanned.getName() + ".bannedBy", sender.getName());
					getPiConfig().set(toBeBanned.getName() + ".date", date);
					savePiConfig();
				}
				else if(args.length <= 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <reason>");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have the permission 'playerinformer.ban' to perform this command."); 
				return false;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onBannedLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		
		if(event.getResult() == Result.KICK_BANNED) {
			String playerBanned = player.getName();
			String reasonBanned = getPiConfig().getString(playerBanned + ".reason");
			String staffBanner = getPiConfig().getString(playerBanned + ".bannedBy");
			String dateBanned = getPiConfig().getString(playerBanned + ".date");
			
			String[] kickMessage = new String[5];
			
			kickMessage[0] = ChatColor.GOLD + "You have been BANNED from this Server!";
			kickMessage[1] = ChatColor.GREEN + "Reason: " + reasonBanned;
			kickMessage[2] = ChatColor.GREEN + "By: " + staffBanner;
			kickMessage[3] = ChatColor.GREEN + "Date: " + dateBanned;
			kickMessage[4] = ChatColor.GOLD + "To Appeal, Go To: www.blackpulse.us";
			
			event.setKickMessage(StringUtils.join(kickMessage, '\n'));
		}
	}

}

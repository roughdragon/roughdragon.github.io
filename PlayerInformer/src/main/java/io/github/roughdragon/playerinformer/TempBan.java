package io.github.roughdragon.playerinformer;

import java.util.HashMap;
import java.util.Map;

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

public class TempBan extends PlayerInformer implements Listener, CommandExecutor {
	
	public Map<String, Long> tempBans = new HashMap<String, Long>();

	public static long getTicks(int number, String units) {
		int i = number;
		int multiplier = 0;
		if(units.equalsIgnoreCase("s")) {
			multiplier = 20;
		} else
		if(units.equalsIgnoreCase("m")) {
			multiplier = 20 * 60;
		} else
		if(units.equalsIgnoreCase("h")) {
			multiplier = 20 * 60 * 60;
		} else
		if(units.equalsIgnoreCase("d")) {
			multiplier = 20 * 60 * 60 * 24;
		}
		long ticks = i * multiplier;
		return ticks;
	}
	
	public void loadTempBans() {
		for(String playername : getTempBans().getConfigurationSection("tempbans").getKeys(false)) {
			long endTime = getTempBans().getLong(playername + ".endTime");
			tempBans.put(playername, endTime);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("tempban")) {
			if(sender.hasPermission("playerinformer.tempban") || sender.isOp()) {
				if(args.length >= 4) {
					@SuppressWarnings("deprecation")
					Player toBeTBanned = Bukkit.getServer().getPlayer(args[0]);
					if(toBeTBanned == null) {
						sender.sendMessage("The Player you have specified is INVALID!");
						return false;
					}
					int time;
					try { 
						time = Integer.parseInt(args[1]);
					} catch (NumberFormatException ex) {
						sender.sendMessage("The Number you have specified is INVALID!");
						return false;
					}
					String unitArg = args[2];
					if(!unitArg.equalsIgnoreCase("s") 
							|| !unitArg.equalsIgnoreCase("m") 
							|| !unitArg.equalsIgnoreCase("h") 
							|| !unitArg.equalsIgnoreCase("d")) {
						sender.sendMessage("The Unit you have specified is INVALID! S|M|H|D");
						return false;
					}
					String kickReason = "";
					for(int i = 4; i < args.length; i++) {
						String arg = args[i] + " ";
						kickReason = kickReason + arg;
					}
					long timeToUnban = getTicks(time, unitArg);
					long currentTime = System.currentTimeMillis();
					long endTime = currentTime + timeToUnban;
					
					tempBans.put(toBeTBanned.getName(), endTime);
					
					String[] tempbanMessage = new String[4];
					tempbanMessage[0] = ChatColor.GOLD + "You have been TEMPBANNED!";
					tempbanMessage[1] = ChatColor.GREEN + "Reason: " + kickReason;
					tempbanMessage[2] = ChatColor.GREEN + "By: " + sender.getName();
					tempbanMessage[3] = ChatColor.GREEN + "For: " + time + unitArg;
					toBeTBanned.kickPlayer(StringUtils.join(tempbanMessage, '\n'));
					
					getTempBans().set(toBeTBanned.getName() + ".reason", kickReason);
					getTempBans().set(toBeTBanned.getName() + ".bannedBy", sender.getName());
					getTempBans().set(toBeTBanned.getName() + ".endTime", endTime);
					saveTempBans();
				} else if (args.length <= 3) {
					sender.sendMessage(ChatColor.RED + "Usage: /tempban <player> <time> <s|m|h|d> <reason>");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have the permission 'playerinformer.tempban' to perform this command.");
				return false;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(tempBans.containsKey(player.getName())) {
			long currentTime = System.currentTimeMillis();
			long endTime = tempBans.get(player.getName());
			if(!(currentTime >= endTime)) {
				String tempBanReason = getTempBans().getString(player.getName() + ".reason");
				String tempBanner = getTempBans().getString(player.getName() + ".bannedBy");
				long timeLeftTicks = endTime - currentTime;
				long timeLeft = timeLeftTicks / (20*60);
				String[] tempbanMessage = new String[4];
				tempbanMessage[0] = ChatColor.GOLD + "You have been TEMPBANNED!";
				tempbanMessage[1] = ChatColor.GREEN + "Reason: " + tempBanReason;
				tempbanMessage[2] = ChatColor.GREEN + "By: " + tempBanner;
				tempbanMessage[3] = ChatColor.GREEN + "For: " + String.valueOf(timeLeft) + " minutes";
				event.disallow(Result.KICK_OTHER, StringUtils.join(tempbanMessage, '\n'));
			} else tempBans.remove(player.getName());
		}
	}
	
}

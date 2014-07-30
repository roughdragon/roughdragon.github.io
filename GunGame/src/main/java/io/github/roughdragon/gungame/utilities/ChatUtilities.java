package io.github.roughdragon.gungame.utilities;

import io.github.roughdragon.gungame.Arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtilities {
	
	// Message Type Enum
	public enum SendType { NORMAL, GOOD, ERROR, INVALIDARGS }

	// ChatColor variables
	static ChatColor aqua = ChatColor.AQUA;
	static ChatColor green = ChatColor.GREEN;
	
	public static String getMessageStarter(SendType type) {
		if(type.equals(SendType.NORMAL)) {
			return ChatColor.GOLD + "";
		} else
		if(type.equals(SendType.GOOD)) {
			return ChatColor.GREEN + "";
		} else
		if(type.equals(SendType.ERROR)) {
			return ChatColor.RED + "Error Occured: ";
		} else
		if(type.equals(SendType.INVALIDARGS)) {
			return ChatColor.RED + "Invalid Arguments: ";
		} else
		return null;
	}
	
	public static String starter() {
		return (aqua + "[" + green + "PowerBall" + aqua + "] ");
	}
	
	public static void sendMessage(Player player, SendType type, String msg) {
		String msgStarter = getMessageStarter(type);
		player.sendMessage(starter() + msgStarter +  msg);
	}	
	
	@SuppressWarnings("deprecation")
	public static void powerballBroadcast(Arena arena, String msg) {
		ChatColor broadcastColor = ChatColor.YELLOW;
		for(String s : arena.getPlayers()) {
			Player player = Bukkit.getPlayer(s);
			player.sendMessage(starter() + broadcastColor + msg);
		}
	}
	
	public static void sendPowerBallHelp(Player player) {
		player.sendMessage(ChatColor.GRAY + "----------------------" + ChatColor.YELLOW + "POWERBALL" + ChatColor.GRAY + "----------------------");
		
		player.sendMessage(ChatColor.GOLD + "/pb reload" + ChatColor.GREEN + " Reload PowerBall");
		
		player.sendMessage(ChatColor.GOLD + "/pb join <arenaName>" + ChatColor.GREEN + " Join Arena <arenaName>");
		player.sendMessage(ChatColor.GOLD + "/pb leave" + ChatColor.GREEN + " Leave current Arena");
		
		player.sendMessage(ChatColor.GOLD + "/pb create <arenaName> <maxPlayers>" + ChatColor.GREEN + " Create an Arena with name <arenaName> and Max Players <maxPlayers>");
		player.sendMessage(ChatColor.GOLD + "/pb remove <arenaName>" + ChatColor.GREEN + " Remove Arena <arenaName>");
		
		player.sendMessage(ChatColor.GOLD + "/pb forceStart <arenaName>" + ChatColor.GREEN + " Force Start Arena <arenaName>");
		player.sendMessage(ChatColor.GOLD + "/pb forceEnd <arenaName>" + ChatColor.GREEN + " Force End Arena <arenaName>");
		
		player.sendMessage(ChatColor.GOLD + "/pb enable <arenaName>" + ChatColor.GREEN + " Enable Arena <arenaName>");
		player.sendMessage(ChatColor.GOLD + "/pb disable <arenaName>" + ChatColor.GREEN + " Disable Arena <arenaName>");
		
		player.sendMessage(ChatColor.GOLD + "/pb setJoinLoc <arenaName>" + ChatColor.GREEN + " Set Join Location for Arena <arenaName>");
		player.sendMessage(ChatColor.GOLD + "/pb setEndLoc <arenaName>" + ChatColor.GREEN + " Set End Location for Arena <arenaName>");
		
		player.sendMessage(ChatColor.GOLD + "/pb setTeamSpawn <arenaName> <team>" + ChatColor.GREEN + " Set Team Spawn for Arena <arenaName> for Team <team>");
		player.sendMessage(ChatColor.GOLD + "/pb removeTeamSpawn <arenaName> <team> <spawnNumber>" + ChatColor.GREEN + " Remove Team Spawn <spawnNumber> for Team <team> in Arena <arenaName>");
		
		player.sendMessage(ChatColor.GRAY + "-----------------------------------------------------");
	}
	
}

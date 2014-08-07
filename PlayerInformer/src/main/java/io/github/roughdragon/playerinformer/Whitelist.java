package io.github.roughdragon.playerinformer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Whitelist extends PlayerInformer implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if(!Bukkit.getServer().getWhitelistedPlayers().contains(player.getName())) {
			String whitelistMsg = getConfig().getString("whitelistMessage");
			event.setKickMessage(ChatColor.GOLD + whitelistMsg);
		}
	}
	
}

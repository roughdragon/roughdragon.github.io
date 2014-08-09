package io.github.roughdragon.MinigameTokens.Listeners;

import io.github.roughdragon.MinigameTokens.MinigameTokens;
import io.github.roughdragon.MinigameTokens.TokenManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {

	@EventHandler
	public void worldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = event.getPlayer().getWorld();
		World MGworld = Bukkit.getServer().getWorld(MinigameTokens.getInstance().getConfig().getString("worldName"));
		
		if(world == MGworld) {
			player.sendMessage("[MinigameTokens] You have entered the Minigame World!");
			TokenManager.getManager().setupScoreboard(player);
			TokenManager.getManager().updateScoreboard(player);
		}
	}
}

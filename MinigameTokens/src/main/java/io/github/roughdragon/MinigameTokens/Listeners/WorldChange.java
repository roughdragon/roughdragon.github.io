package io.github.roughdragon.MinigameTokens.Listeners;

import io.github.roughdragon.MinigameTokens.MinigameTokens;
import io.github.roughdragon.MinigameTokens.TokenManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldChange implements Listener {

	@EventHandler
	public void worldChange(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		World world = event.getTo().getWorld();
		World MGworld = Bukkit.getServer().getWorld(MinigameTokens.getInstance().getConfig().getString("worldName"));
				
		if(world.getName() == MGworld.getName()) {
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			if(MinigameTokens.getInstance().getConfig().getBoolean("enableScoreboard") == true) {
				TokenManager.getManager().setupScoreboard(player);
				TokenManager.getManager().updateScoreboard(player);
			}
		}
	}
}

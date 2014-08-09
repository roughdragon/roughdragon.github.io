package io.github.roughdragon.MinigameTokens.Listeners;

import io.github.roughdragon.MinigameTokens.MinigameTokens;
import io.github.roughdragon.MinigameTokens.TokenManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKilled implements Listener {

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		if(MinigameTokens.getInstance().getConfig().getBoolean("playerKillTokens") == false) {
			return;
		}
		
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		World world = Bukkit.getServer().getWorld(MinigameTokens.getInstance().getConfig().getString("worldName"));
		
		if(!(player.getWorld() == world)) {
			return;
		}
		if(!(killer.getWorld() == world)) {
			return;
		}
		
		int killTokens = MinigameTokens.getInstance().getConfig().getInt("tokensPerKill");
		
		TokenManager.getManager().giveTokens(killer, killTokens);
	}
	
}

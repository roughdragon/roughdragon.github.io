package io.github.roughdragon.gungame;

import org.bukkit.entity.Player;

public class Game {

	private static Game instance;
	public static Game getGame() { return instance; }
	
	public void damage(Player player, Double hitPoints) {
		player.setHealth(player.getHealth() - hitPoints);
	}
	
	
}

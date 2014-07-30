package io.github.roughdragon.gungame;

import java.util.Random;

import io.github.roughdragon.gungame.Arena.Team;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Game {
	
	public static void damage(Player player, Double hitPoints) {
		player.damage(hitPoints);
		return;
	}
	
	public static void respawn(Player player, Team team) {
		Arena arena = ArenaManager.getManager().getArenabyPlayer(player);
		if(arena == null) { return; }
		
		if(team == Team.RED) {
			Random random = new Random();
			int spawn = random.nextInt(arena.redSpawn.size());
			Location rspawn = arena.redSpawn.get(spawn);
			player.teleport(rspawn);
			return;
		}
		
		if(team == Team.BLUE) {
			Random random = new Random();
			int spawn = random.nextInt(arena.blueSpawn.size());
			Location rspawn = arena.blueSpawn.get(spawn);
			player.teleport(rspawn);
			return;
		}
		
	}
	
}

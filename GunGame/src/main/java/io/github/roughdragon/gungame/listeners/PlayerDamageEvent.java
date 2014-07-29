package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;
import io.github.roughdragon.gungame.Game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageEvent implements Listener {

	@EventHandler
	public void snowballHit(EntityDamageByEntityEvent event) {
		Entity entHit = event.getEntity();
		Entity entShooter = event.getDamager();
		if(entHit instanceof Player && entShooter instanceof Snowball) {
			Player hit = (Player) entHit;
			Snowball shot = (Snowball) entShooter;
			if(shot.getShooter() instanceof Player) {
				Player shooter = (Player) shot.getShooter();
				for(Arena a : Arena.arenaObjects) {
					if(a.getPlayers().contains(shooter.getName()) && a.getPlayers().contains(hit.getName())) {
						if(a.isInGame()) {
							event.setCancelled(true);
							Game.getGame().damage(hit, 2.0);
						}
					}
				}
			}	
		}
	}
	
}

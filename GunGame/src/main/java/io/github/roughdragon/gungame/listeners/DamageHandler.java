package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;
import io.github.roughdragon.gungame.ArenaManager;
import io.github.roughdragon.gungame.Game;
import io.github.roughdragon.gungame.utilities.ChatUtilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class DamageHandler implements Listener {

	@EventHandler
	public void snowballHit(EntityDamageByEntityEvent event) {
		Entity entHit = event.getEntity();
		Entity entShooter = event.getDamager();
		if(entHit instanceof Player) {
			if(entShooter instanceof Snowball) {
				Player hit = (Player) entHit;
				Snowball shot = (Snowball) entShooter;
				hit.sendMessage("This method has been ran :O");
				if(shot.getShooter() instanceof Player) {
					Player shooter = (Player) shot.getShooter();
					for(Arena a : Arena.arenaObjects) {
						if(a.getPlayers().contains(shooter.getName()) && a.getPlayers().contains(hit.getName())) {
							if(a.isInGame()) {
								event.setCancelled(true);
								Game.damage(hit, 2.0);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void healthRegen(EntityRegainHealthEvent event) {
		Entity regener = event.getEntity();
		if(regener instanceof Player) {
			Player player = (Player) regener;
			for(Arena a : Arena.arenaObjects) {
				if(a.getPlayers().contains(player.getName())) {
					if(a.isInGame()) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void deathEvent(EntityDamageEvent event) {
		Entity deathPlayer = event.getEntity();
		if(deathPlayer instanceof Player) {
			Player player = (Player) deathPlayer;
			Player killer = player.getKiller();
			for(Arena a : Arena.arenaObjects) {
				if(a.getPlayers().contains(player.getName())) {
					if(a.isInGame()) {
						if(player.getHealth() - event.getDamage() <= 0) {
							event.setCancelled(true);
							player.setHealth(20.0);
							player.setFoodLevel(20);
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
							ChatUtilities.gunGameBroadcast(a, ChatColor.WHITE + killer.getName() + ChatColor.GOLD + " has killed " + ChatColor.WHITE + player.getName() + ChatColor.GOLD + "!");
							Game.respawn(player, ArenaManager.getManager().getPlayersTeam(player));
							// Give killer's team +1 point
						}
					}
				}
			}
		}
	}
	
}

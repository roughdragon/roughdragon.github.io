package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerInventoryManipulation implements Listener {

	@EventHandler
	public void playerPickupEvent(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		
		for(Arena a : Arena.arenaObjects) {
			if(a.getPlayers().contains(player.getName())) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void playerDropEvent(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		
		for(Arena a : Arena.arenaObjects) {
			if(a.getPlayers().contains(player.getName())) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void playerInventoryClickEvent(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		
		for(Arena a : Arena.arenaObjects) {
			if(a.getPlayers().contains(player.getName())) {
				e.setCancelled(true);
			}
		}
		
	}
	
}

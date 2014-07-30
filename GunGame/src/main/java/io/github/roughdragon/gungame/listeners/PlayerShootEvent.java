package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;
import io.github.roughdragon.gungame.Game;
import io.github.roughdragon.gungame.GunGame;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerShootEvent implements Listener {

	int taskID;
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerShootEvent(PlayerInteractEvent e) {			
		Player player =  (Player) e.getPlayer();
		World world = player.getWorld();	
		ItemStack m = player.getItemInHand();
		for(Arena a : Arena.arenaObjects) {
			if(a.getPlayers().contains(player.getName())) {
				if(a.isInGame()) {
					if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
						if(m.getType().equals(Material.DIAMOND_HOE)) {
							if(m.getItemMeta().getDisplayName().equals("§5Diamond Gun")) {
								e.setCancelled(true);
								//player.launchProjectile(Snowball.class);
								final Item steak = world.dropItem(player.getEyeLocation(), new ItemStack(Material.COOKED_BEEF));
								steak.setVelocity(player.getLocation().getDirection());
								taskID = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(GunGame.instance, new Runnable() {
									@Override
									public void run() {
										if(steak.getVelocity().equals(new Vector(0.0, 0.0, 0.0))) {
											final double damageRadius = 2D;
											List<Entity> nearbyEntities = steak.getLocation().getWorld().getEntities();
											for(Entity e : nearbyEntities) {
												if(e.getLocation().distance(steak.getLocation()) <= damageRadius) {
													if(e.getType() == EntityType.PLAYER) {
														final Player hit = (Player) e;
														Game.damage(hit, 2.0);
														steak.remove();
														cancelTask();
													}
												}
											}
										}
									}
								}, 20l, 20l);
							}
						}
					}
				}
			}
		}		
	}
	
	public void cancelTask() {
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}
	
}

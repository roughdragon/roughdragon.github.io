package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;
import io.github.roughdragon.gungame.GunGame;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Grenade implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerGrenadeEvent(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		
		ItemStack m = player.getItemInHand();
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			/* if Player right clicked */
			if(m.getType() == Material.SLIME_BALL) {
				if(m.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "GRENADE!!!")) {
					/* if Player right clicked a slime ball with name GRENADE!!! */
					for(Arena a : Arena.arenaObjects) {
						if(a.getPlayers().contains(player.getName())) {
							/* if Player is in an Arena */
							if(a.isInGame()) {
								/* if Arena is in game */
								e.setCancelled(true);
								final Item grenade = world.dropItem(player.getEyeLocation(), new ItemStack(Material.SLIME_BALL));
								grenade.setVelocity(player.getEyeLocation().getDirection());
								GunGame.instance.getServer().getScheduler().scheduleAsyncDelayedTask(GunGame.instance, new Runnable() {
									@Override
									public void run() {
										//grenade.getWorld().createExplosion(grenade.getLocation(), 1.0F, false);
										double x = grenade.getLocation().getX();
										double y = grenade.getLocation().getY();
										double z = grenade.getLocation().getZ();
										grenade.getWorld().createExplosion(x, y, z, 4.0F, false, false);
										grenade.remove();
									}
								}, 40L);

							} else return;
						} else return;
					}
				} else return;
			} else return;
		} else return;
	}
	
}

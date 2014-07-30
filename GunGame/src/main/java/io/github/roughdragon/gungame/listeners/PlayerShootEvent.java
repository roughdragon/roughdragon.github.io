package io.github.roughdragon.gungame.listeners;

import io.github.roughdragon.gungame.Arena;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerShootEvent implements Listener {

	@EventHandler
	public void playerShootEvent(PlayerInteractEvent e) {
				
		Player player =  (Player) e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			/* if Player right clicked */
			ItemStack m = player.getItemInHand();
			if(m.getType() == Material.DIAMOND_HOE && m.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Diamond Gun")) {
				/* if Player right clicked Diamond Gun with Color Purple and Bolded */
				for(Arena a : Arena.arenaObjects) {
					if(a.getPlayers().contains(player.getName())) {
						/* if Player is in an Arena */
						if(a.isInGame()) {
							/* if Arena is in game */
							e.setCancelled(true);
							player.launchProjectile(Snowball.class);

						} else return;
					} else return;
				}
			} else return;
		} else return;
	}
}

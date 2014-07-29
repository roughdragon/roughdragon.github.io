package io.github.roughdragon.gungame.utilities;

import io.github.roughdragon.gungame.Arena;
import io.github.roughdragon.gungame.ArenaManager;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCountdown extends BukkitRunnable {

	Arena a;
	
	public ArenaCountdown(Arena arena) { a = arena; }
		
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		if(a.timeLeft == 0) {
			/* Countdown Over. Start Game! */
			ChatUtilities.powerballBroadcast(a, "The game has started!");
			for(String p : a.getPlayers()) {	
				Player player = Bukkit.getPlayer(p);
				player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 10, 1);
			}
			a.stopCountdown();
			a.countdownDone = true;
			ArenaManager.getManager().startArena(a.getName());
		}
		
		if(a.getPlayers().size() < a.getMaxPlayers()) {
			/* Not Enough Players. Stop Countdown */
			ChatUtilities.powerballBroadcast(a, "Not enough players to begin! Countdown stopped.");
			a.stopCountdown();
		}
		
		if(a.timeLeft == 20 || a.timeLeft == 15 || a.timeLeft == 10) {
			/* Time Left Is 20, 15, 10. Broadcast Time */	
			ChatUtilities.powerballBroadcast(a, a.timeLeft + " seconds left!");
		}
		
		if(a.timeLeft <= 5 && a.timeLeft > 0) {
			/* Time Left Is 5, 4, 3, 2, 1. Broadcast Time */
			ChatUtilities.powerballBroadcast(a, a.timeLeft + " seconds left!");
			for(String p : a.getPlayers()) {	
				Player player = Bukkit.getPlayer(p);
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 10, 1);
			}
		}
		
		a.timeLeft -= 1;
		
	}
	
	
	
}

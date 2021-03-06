package io.github.roughdragon.MinigameTokens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class TokenManager {

	private static TokenManager manager = new TokenManager();
	public static TokenManager getManager() { return manager; }
	
	public int getTokens(Player player) {
		String UUIDstring = player.getUniqueId().toString();
		String tokensString = MinigameTokens.getInstance().getTokensConfig().getString(UUIDstring);
		int tokens = 0;
		try {
			tokens = Integer.parseInt(tokensString);
		} catch (NumberFormatException ex) {
			MinigameTokens.getInstance().getLogger().severe("[MinigameTokens] Internal Error Occured! Please Send A Bug Report To RoughDragon.");
		}
		return tokens;
	}
	
	public void giveTokens(Player player, int amountOfTokens) {
		int currentTokens = getTokens(player);
		int newTokens = currentTokens + amountOfTokens;
		setTokens(player, newTokens);
	}
	
	public void takeTokens(Player player, int amountOfTokens) {
		int currentTokens = getTokens(player);
		int newTokens = currentTokens - amountOfTokens;
		setTokens(player, newTokens);
	}
	
	public void setTokens(Player player, int amountOfTokens) {
		String UUIDstring = player.getUniqueId().toString();
		MinigameTokens.getInstance().getTokensConfig().set(UUIDstring, amountOfTokens);
		MinigameTokens.getInstance().saveTokensConfig();
	}
	
	public void resetTokens(Player player) {
		String UUIDstring = player.getUniqueId().toString();
		String baseTokensString = MinigameTokens.getInstance().getConfig().getString("baseTokens");
		int baseTokens = 0;
		try {
			baseTokens = Integer.parseInt(baseTokensString);
		} catch (NumberFormatException ex) {
			MinigameTokens.getInstance().getLogger().severe("[MinigameTokens] Internal Error Occured! Please Send A Bug Report To RoughDragon.");
		}
		MinigameTokens.getInstance().getTokensConfig().set(UUIDstring, baseTokens);
		MinigameTokens.getInstance().saveTokensConfig();
	}
	
	public boolean enoughTokens(Player player, int tokens) {
		if(getTokens(player) >= tokens) { return true; }
		return false;
	}
	
	public void setupScoreboard(final Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		final Objective objective = scoreboard.registerNewObjective("tokens", "dummy");
		
		objective.setDisplayName(ChatColor.YELLOW + "Tokens");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		player.setScoreboard(scoreboard);
		updateScoreboard(player);
	}
	
	@SuppressWarnings("deprecation")
	public void updateScoreboard(Player player) {
		Scoreboard scoreboard = player.getScoreboard();
		Objective objective = scoreboard.getObjective("tokens");
		
		int tokens = MinigameTokens.getInstance().getTokensConfig().getInt(player.getUniqueId().toString());
		
		objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "My Tokens:")).setScore(tokens);
	}
	
}

package io.github.roughdragon.gungame;

import io.github.roughdragon.gungame.listeners.DamageHandler;
import io.github.roughdragon.gungame.listeners.Grenade;
import io.github.roughdragon.gungame.listeners.PlayerInventoryManipulation;
import io.github.roughdragon.gungame.listeners.PlayerShootEvent;
import io.github.roughdragon.gungame.utilities.ChatUtilities;
import io.github.roughdragon.gungame.utilities.ChatUtilities.SendType;
import io.github.roughdragon.gungame.utilities.Kits;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GunGame extends JavaPlugin implements Listener {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static GunGame instance;
	
	public static int arenaCountdownID;
	
	public GunGame getInstance() { return instance; }
	
	@Override
	public void onEnable() {
		instance = this;
		
		this.logger.info("[GunGame] has been enabled!");
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(configFile.exists())
			ArenaManager.getManager().loadArenas();
		
		createMainConfig();
		registerListeners();
		setupKits();

	}
	
	@Override
	public void onDisable() {
		instance = null;
		
		this.logger.info("[GunGame] has been disabled!");
		
		saveConfig();
	}
	
	public void createMainConfig() {
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			this.logger.info("[GunGame] Config file found, loading...");
			return;
		}
		
		this.logger.info("[GunGame] Config file not found, creating...");
		try {
			FileOutputStream writer = new FileOutputStream(new File(getDataFolder() + File.separator + "config.yml"));
			InputStream out = this.getResource("config.yml");
			byte[] linebuffer = new byte[4096];
			int lineLength = 0;
			while((lineLength = out.read(linebuffer)) >= 0)
			{
			   writer.write(linebuffer, 0, lineLength);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.info("[GunGame] Error Occured. Config File not created!");
		}
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(this, this);
		pm.registerEvents(new PlayerShootEvent(), this);
		pm.registerEvents(new Grenade(), this);
		pm.registerEvents(new PlayerInventoryManipulation(), this);
		pm.registerEvents(new DamageHandler(), this);
	}
		
	public void setupKits() {
		new Kits("Grenadier", Arrays.asList("341:4:§a§lGRENADE!", "293:1:§5Diamond Gun"), 341);
		new Kits("Swordsman", Arrays.asList("276:1:§4Diamond Blade"), 293);
		new Kits("Archer", Arrays.asList("261:1:§eSniper", "262:64:Arrows"), 261);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Slow down! You must be a player to send [GunGame] commands");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("Gun") || cmd.getName().equalsIgnoreCase("GunGame")) {
			
			if(args.length == 0) {
				ChatUtilities.sendGunGameHelp(p);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				getServer().getPluginManager().disablePlugin(this);
				getServer().getPluginManager().enablePlugin(this);
				ChatUtilities.sendMessage(p, SendType.GOOD, "GunGame has been reloaded!");
				return true;
			}		
			else if(args[0].equalsIgnoreCase("join")) {											
				if(args.length == 2) {
					ArenaManager.getManager().addPlayers(p, args[1]);  
					return true;
				} 
				else if(args.length == 1 || args.length > 2) { 
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun join <arenaName>");
					return true;
				}	
			}
			else if(args[0].equalsIgnoreCase("leave")) {
				ArenaManager.getManager().removePlayer(p);	
				return true;
			}
			else if(args[0].equalsIgnoreCase("create")) {	
				if(args.length == 3) {
					ArenaManager.getManager().createArena(p, args[1], args[2]);
					return true;
				}
				else if(args.length <= 2 || args.length > 3) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun create <arenaName> <maxPlayers>");
					return true;
				}	
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				if(args.length == 2) {
					ArenaManager.getManager().removeArena(p, args[1]);
					return true;
				}
				else if(args.length == 1 || args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun remove <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("forceStart")) {
				if(args.length == 2) {
					ArenaManager.getManager().forceStart(p, args[1]);
					return true;
				}
				else if(args.length == 1 || args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS,"Correct Usage: /gun forceStart <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("forceEnd")) {
				if(args.length == 2) {
					ArenaManager.getManager().forceEnd(p, args[1]);
					return true;
				}
				else if(args.length == 1 || args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun forceEnd <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("enable")) {
				if(args.length == 2) {
					ArenaManager.getManager().setStatus(p, args[1], "enable");
					return true;
				}
				else if(args.length <= 2 || args.length > 3) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS,"Correct Usage: /gun enable <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("disable")) {
				if(args.length == 2) {
					ArenaManager.getManager().setStatus(p, args[1], "disable");
					return true;
				}
				else if(args.length <= 2 || args.length > 3) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS,"Correct Usage: /gun disable <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("setJoinLoc")) {
				if(args.length == 2) {
					ArenaManager.getManager().setJoinLocation(p, args[1]);
					return true;
				}
				else if(args.length == 1 || args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS,"Correct Usage: /gun setJoinLoc <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("setEndLoc")) {
				if(args.length == 2) {
					ArenaManager.getManager().setEndLocation(p, args[1]);
					return true;
				}
				else if(args.length == 1 || args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS,"Correct Usage: /gun setEndLoc <arenaName>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("setTeamSpawn")) {
				if(args.length == 3) {
					ArenaManager.getManager().setTeamSpawn(p, args[2], args[1]);	
					return true;
				}
				else if(args.length <= 2 || args.length > 3) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun setTeamSpawn <arenaName> <team>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("removeTeamSpawn")) {
				if(args.length == 4) {
					ArenaManager.getManager().removeTeamSpawn(p, args[1], args[2], args[3]);
					return true;
				}
				else if(args.length <= 3 || args.length > 4) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun removeTeamSpawn <arenaName> <team> <spawnNumber>");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("kits")) {
				if(args.length == 2) {
					ArenaManager.getManager().setKit(p, args[1]);
					return true;
				}
				else if(args.length == 1) {
					ChatUtilities.sendKitList(p);
					return true;
				}
				else if(args.length > 2) {
					ChatUtilities.sendMessage(p, SendType.INVALIDARGS, "Correct Usage: /gun kits <kitName>");
					return true;
				}
			}
		}
		
		
		
		
		return false;
	}
	
}


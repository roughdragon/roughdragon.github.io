package io.github.roughdragon.gungame;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.roughdragon.gungame.Arena.Team;
import io.github.roughdragon.gungame.utilities.ChatUtilities;
import io.github.roughdragon.gungame.utilities.ChatUtilities.SendType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaManager {
	
	public enum Status { ENABLED, DISABLED }
	
	public int startCountdownID;
	
	private Map<String, ItemStack[]> playerInventory = new HashMap<String, ItemStack[]>();
	private Map<String, ItemStack[]> playerArmor = new HashMap<String, ItemStack[]>();
	
	private static ArenaManager am = new ArenaManager();

	//Usefull for getting the ArenaManager, like so: ArenaManager.getManager()
	public static ArenaManager getManager() {
		return am;
	}


	//A method for getting one of the Arenas out of the list by name:
	public Arena getArena(String name) {
		for (Arena a: Arena.arenaObjects) { //For all of the arenas in the list of objects
			if (a.getName().equalsIgnoreCase(name)) { //If the name of an arena object in the list is equal to the one in the parameter...
				return a; //Return that object
			}
		}
		return null; //No objects were found, return null
	}

	//A method for getting which arena a player is in
	public Arena getArenabyPlayer(Player player) {
		for(Arena a : Arena.arenaObjects) { //For all the arenas
			if(a.getPlayers().contains(player.getName())) { //If the name of the player is in one the arenas
				return a; //Return arena
			}
		}
		return null;
	}
	
	//A method for getting status of an Arena
	public Status getArenaStatus(String arenaName) {
		
		FileConfiguration fc = GunGame.instance.getConfig();
		String path = "arenas." + arenaName + "."; 
		
		Arena arena = getArena(arenaName);
		if(arena == null) return null;
		
		String status = (String) fc.get(path + "status");
		
		if(status.equals("DISABLED")) {
			return Status.DISABLED;
		}
		else if(status.equals("ENABLED")) {
			return Status.ENABLED;
		}
		
		return null;
	}
	
	public Team getPlayersTeam(Player player) {
		for(Arena a : Arena.arenaObjects) {
			if(a.blueTeamPlayers.contains(player)) {
				return Team.BLUE;
			}
			else if(a.redTeamPlayers.contains(player)) {
				return Team.RED;
			}
		}
		return null;
	}
	
	//A method for adding players
	public void addPlayers(Player player, String arenaName) {
		
		/*
		if (getArena(arenaName) != null) { //If the arena exsists

			if(getArenaStatus(arenaName) == Status.ENABLED) {
				
				Arena arena = getArena(arenaName); //Create an arena for using in this method

				if (!arena.isFull()) { //If the arena is not full

					if (!arena.isInGame()) {
						
						if(!arena.getPlayers().contains(player.getName())) {

							//Every check is complete, arena is joinable
							player.getInventory().clear(); //Clear the players inventory
							player.setHealth(20); //Heal the player
							player.setFireTicks(0); //Heal the player even more ^ ^ ^

							//Teleport to the arena's join location
							player.teleport(arena.getJoinLocation());

							//Add the player to the arena list
							arena.getPlayers().add(player.getName()); //Add the players name to the arena

							int playersLeft = arena.getMaxPlayers() - arena.getPlayers().size(); //How many players needed to start
							//Send the arena's players a message
							arena.sendMessage(ChatColor.BLUE + player.getName() + " has joined the arena! We only need " + playersLeft + " player(s) to start the game!");


							if (playersLeft == 0) { //IF there are 0 players needed to start the game
								startArena(arenaName); //Start the arena, see the method way below :)
							}

						} else { //Player is in this arena
							player.sendMessage(ChatColor.RED + "You are already in this arena.");
						}


					} else { //Specifiend arena is in game, send the player an error message
						player.sendMessage(ChatColor.RED + "The arena you are looking for is currently in-game!");
					}
					
				} else { //Specified arena is full, send the player an error message
					player.sendMessage(ChatColor.RED + "The arena you are looking for is currently full!");
				}
				
			} else { //Specfified arena is DISABLED
				player.sendMessage(ChatColor.RED + "The arena you are looking for is currently DISABLED!");
			}

		} else { //The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
		}
		*/
		
		
		/* REWRITTEN CODE BY ROUGHDRAGON */
		
		if(getArenabyPlayer(player) != null) {
			/* Player is currently in an Arena */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "You are in an Arena. To join another Arena, you must first leave the current Arena. /pb leave");
			return;
		}
		
		if(getArena(arenaName) == null) {
			/* Specified Arena does not exist */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		// Arena is an Arena so initialize that here
		Arena arena = getArena(arenaName);
		
		if(getArenaStatus(arenaName) == Status.DISABLED) {
			/* Specified Arena is DISABLED */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified is currently DISABLED!");
			return;
		}
						
		if(arena.isFull() == true) {
			/* Specified Arena is FULL */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified is currently full!");
			return;
		}
		
		if(arena.isInGame() == true) {
			/* Specified Arena is INGAME */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified is already IN-GAME!");
			return;
		}
				
		// All checks are done. Add player now!
		arena.getPlayers().add(player.getName()); /* Add Player To Arena List */
		
		playerInventory.put(player.getName(), player.getInventory().getContents()); /* Save Player's Inventory */
		playerInventory.put(player.getName(), player.getInventory().getArmorContents()); /* Save Player's Armor */
		
		player.getInventory().clear(); /* Clear Player's Inventory */
		player.getInventory().setArmorContents(null); /* Clear Player's Armor */
		player.setHealth(20.0); /* Heal Player To 10 Hearts */
		
		player.teleport(arena.getJoinLocation()); /* Teleport Player To Arena Lobby */	
		
		int playersLeft = arena.getMaxPlayers() - arena.getPlayers().size(); /* Players' Needed To Start Game */
		ChatUtilities.powerballBroadcast(arena, player.getName() + " has joined the Arena! We need " + playersLeft + " more player(s) to start the game!");
		
		if(playersLeft == 0) { startArena(arenaName); } /* Start Arena If 0 Players Needed */
		
	}


	//A method for removing players
	public void removePlayer(Player player) {

		/*
		if (getArena(arenaName) != null) { //If the arena exsists

			Arena arena = getArena(arenaName); //Create an arena for using in this method

			if (arena.getPlayers().contains(player.getName())) { //If the arena has the player already

				//Every check is complete, arena is leavable
				player.getInventory().clear(); //Clear the players inventory
				player.setHealth(20); //Heal the player
				player.setFireTicks(0); //Heal the player even more ^ ^ ^

				
				FileConfiguration fc = PowerBall.instance.getConfig();
				String path = "arenas." + arenaName + ".";
				
				double x = fc.getDouble(path + "endX");
				double y = fc.getDouble(path + "endY");
				double z = fc.getDouble(path + "endZ");
				World world = Bukkit.getServer().getWorld(fc.getString(path + "world"));
				
				Location endLocation = new Location(world, x, y, z);
							
				player.teleport(endLocation);

				//remove the player to the arena list
				arena.getPlayers().remove(player.getName()); //Removes the players name to the arena

				//Send the arena's players a message
				arena.sendMessage(ChatColor.BLUE + player.getName() + " has left the Arena! There are " + arena.getPlayers().size() + "players currently left!");

			} else { //Specified arena doesn't have the player, send the player an error message
				player.sendMessage(ChatColor.RED + "Your not in the arena your looking for!");

			}


		} else { //The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
		}
		*/
		
		
		/* REWRITTEN CODE BY ROUGHDRAGON */
		
		if(getArenabyPlayer(player) == null) {
			/* Player Is Not In An Arena */
			// SEND PLAYER A MESSAGE || SENDTYPE.ERROR
			ChatUtilities.sendMessage(player, SendType.ERROR, "You are not in an Arena!");
			return;
		}
		
		// Arena is an Arena so initialize that here
		Arena arena = getArenabyPlayer(player);
		
		// All checks are done. Remove player now!
		arena.getPlayers().remove(player.getName()); /* Remove Player From Arena List */
		
		player.getInventory().clear(); /* Clear Player's Inventory */
		player.getInventory().setArmorContents(null); /* Clear Player's Armor */
		player.setHealth(20.0); /* Heal Player To 10 Hearts */
		
		player.getInventory().setContents(playerInventory.get(player.getName())); /* Restore Player's Inventory */
		player.getInventory().setArmorContents(playerArmor.get(player.getName())); /* Restore Player's Armor */
		
		player.teleport(arena.getEndLocation()); /* Teleport Player To Hub */
		
		ChatUtilities.powerballBroadcast(arena, player.getName() + " has left the Arena! There are " + arena.getPlayers().size() + " player(s) currently left!");
		ChatUtilities.sendMessage(player, SendType.NORMAL, "You have left the Arena!");
	}


	//A method for starting an Arena:
	@SuppressWarnings("deprecation")
	public void startArena(String arenaName) {
		
		if (getArena(arenaName) != null) { //If the arena exsists

			if(getArenaStatus(arenaName) == Status.ENABLED) {
				
				Arena arena = getArena(arenaName); //Create an arena for using in this method

				arena.startCountdown();
				
				if(arena.countdownDone == true) {
					arena.sendMessage(ChatColor.GOLD + "The arena has BEGUN!");

					//Set ingame
					arena.setInGame(true);

					int i = 0;
					for (String s: arena.getPlayers()) {//Loop through every player in the arena

						if (i < arena.getPlayers().size() / 2) {
							// Put player on Red Team
							arena.setPlayerTeam(s, Team.RED);
							ChatUtilities.sendMessage(Bukkit.getPlayer(s), SendType.NORMAL, s + " is now on the " + ChatColor.RED + "RED " + ChatColor.GOLD + "team.");
							Random random = new Random();
							int spawn = random.nextInt(arena.redSpawn.size());
							Location rspawn = arena.redSpawn.get(spawn);
							Bukkit.getPlayer(s).teleport(rspawn);
						} else {
							// Put player on Blue Team
							arena.setPlayerTeam(s, Team.BLUE);
							ChatUtilities.sendMessage(Bukkit.getPlayer(s), SendType.NORMAL, s + " is now on the " + ChatColor.AQUA + "BLUE " + ChatColor.GOLD + "team.");
							Random random = new Random();
							int spawn = random.nextInt(arena.blueSpawn.size());
							Location rspawn = arena.blueSpawn.get(spawn);
							Bukkit.getPlayer(s).teleport(rspawn);
						}
						i++;
						
						/* Diamond Gun */
						ItemStack weapon = new ItemStack(Material.DIAMOND_HOE);
						ItemMeta weaponMeta = weapon.getItemMeta();
						
						weaponMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Diamond Gun");
						weapon.setItemMeta(weaponMeta);
						
						/* Grenade */
						ItemStack grenade = new ItemStack(Material.SLIME_BALL);
						ItemMeta grenadeMeta = grenade.getItemMeta();
						
						grenadeMeta.setDisplayName(ChatColor.GREEN + "GRENADE!!!");
						grenade.setItemMeta(grenadeMeta);
						
						Player player = Bukkit.getServer().getPlayer(s);
						
						player.getInventory().addItem(weapon);
						player.getInventory().addItem(grenade);
						player.updateInventory();

						arena.stopCountdown();
						arena.countdownDone = false;
						
					}
				}
				
			}
		}

	}


	//A method for ending an Arena:
	@SuppressWarnings("deprecation")
	public void endArena(Player sender, String arenaName) {

		/*
		if (getArena(arenaName) != null) { //If the arena exsists

			Arena arena = getArena(arenaName); //Create an arena for using in this method

			//Send them a message
			arena.sendMessage(ChatColor.GOLD + "The arena has ended :(");

			//Set ingame
			arena.setInGame(false);

			for (String s: arena.getPlayers()) {//Loop through every player in the arena

				//Teleport them:

				Player player = Bukkit.getPlayer(s); //Create a player by the name
				/
				FileConfiguration fc = PowerBall.instance.getConfig();
				String path = "arenas." + arenaName + ".";
				
				double x = fc.getDouble(path + "endX");
				double y = fc.getDouble(path + "endY");
				double z = fc.getDouble(path + "endZ");
				World world = Bukkit.getServer().getWorld(fc.getString(path + "world"));
				
				Location endLocation = new Location(world, x, y, z);
							
				player.teleport(endLocation);

				player.getInventory().clear(); //Clear the players inventory
				player.setHealth(20); //Heal the player
				player.setFireTicks(0); //Heal the player even more ^ ^ ^

			}
			
			//Remove them all from the list
			arena.getPlayers().clear();;
			
		}
		*/
		
		
		/* REWRITTEN BY ROUGHDRAGON */
		if(getArena(arenaName) == null) {
			/* Specified Arena does not exist */
			ChatUtilities.sendMessage(sender, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		// Arena is an Arena so initialize that here
		Arena arena = getArena(arenaName);
		
		arena.setInGame(false); /* Set Arena Status Of InGame To False */
		
		for(String s : arena.getPlayers()) {
			Player player = Bukkit.getServer().getPlayer(s); /* Get Player By Name */
			
			player.teleport(arena.getEndLocation()); /* Teleport Player To End Location */
			
			player.getInventory().clear(); /* Clear Player's Inventory */
			player.getInventory().setArmorContents(null); /* Clear Player's Armor */
			player.setHealth(20.0); /* Heal The Player */
			
			player.getInventory().setContents(playerInventory.get(player.getName())); /* Restore Player's Inventory */
			player.getInventory().setArmorContents(playerArmor.get(player.getName())); /* Restore Player's Armor */
			
			player.teleport(arena.getEndLocation()); /* Teleport Player To Hub */
			
			ChatUtilities.sendMessage(player, SendType.NORMAL, "The Arena has been force ended!");
		}
		
	}


	//And our final method, loading each arena
	//This will be resonsible for creating each arena from the config, and creating an object to represent it
	//Call this method in your main class, onEnable


	public void loadArenas() {

		//I just create a quick Config Variable, obviously don't do this.
		//Use your own config file
		FileConfiguration fc = GunGame.instance.getConfig(); //If you just use this code, it will erorr, its null. Read the notes above, USE YOUR OWN CONFIGURATION FILE

		//Youll get an error here, FOR THE LOVE OF GAWD READ THE NOTES ABOVE!!!
		try {
			for (String keys: fc.getConfigurationSection("arenas").getKeys(false)) { //For each arena name in the arena file

				//Now lets get all of the values, and make an Arena object for each:
				//Just to help me remember: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)

				World world = Bukkit.getServer().getWorld(fc.getString("arenas." + keys + ".world"));
				int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayers");

				//Arena's name is keys
				Arena arena = new Arena(keys, /* joinLocation, startLocation, endLocation, */ maxPlayers);

				double joinX = fc.getDouble("arenas." + keys + "." + "joinX");
				double joinY = fc.getDouble("arenas." + keys + "." + "joinY");
				double joinZ = fc.getDouble("arenas." + keys + "." + "joinZ");
				Location joinLocation = new Location(world, joinX, joinY, joinZ);

				//double startX = fc.getDouble("arenas." + keys + "." + "startX");
				//double startY = fc.getDouble("arenas." + keys + "." + "startY");
				//double startZ = fc.getDouble("arenas." + keys + "." + "startZ");
				//Location startLocation = new Location(world, startX, startY, startZ);
				for(String num : fc.getConfigurationSection("arenas." + keys + ".blueteam").getKeys(false)) {
					double spawnX = fc.getDouble("arenas." + keys + ".blueteam." + num + ".spawnX");
					double spawnY = fc.getDouble("arenas." + keys + ".blueteam." + num + ".spawnY");
					double spawnZ = fc.getDouble("arenas." + keys + ".blueteam." + num + ".spawnZ");
					Location blueSpawn = new Location(world, spawnX, spawnY, spawnZ);
					arena.blueSpawn.add(blueSpawn);
				}
				for(String num : fc.getConfigurationSection("arenas." + keys + ".redteam").getKeys(false)) {
					double spawnX = fc.getDouble("arenas." + keys + ".redteam." + num + ".spawnX");
					double spawnY = fc.getDouble("arenas." + keys + ".redteam." + num + ".spawnY");
					double spawnZ = fc.getDouble("arenas." + keys + ".redteam." + num + ".spawnZ");
					Location redSpawn = new Location(world, spawnX, spawnY, spawnZ);
					arena.redSpawn.add(redSpawn);
				}

				double endX = fc.getDouble("arenas." + keys + "." + "endX");
				double endY = fc.getDouble("arenas." + keys + "." + "endX");
				double endZ = fc.getDouble("arenas." + keys + "." + "endX");

				Location endLocation = new Location(world, endX, endY, endZ);

				//Now lets create an object to represent it:
				arena.setJoinLocation(joinLocation);
				arena.setEndLocation(endLocation);

			}
		} catch (NullPointerException e) {
			GunGame.instance.logger.info("[PowerBall] Config File is empty. Arenas were not loaded.");
		}

	}

	//Our final method, create arena!
	public void createArena(Player player, String arenaName, String maxPlayersString) {

		/*
		//Now, lets create an arena object to represent it:
		Arena arena = new Arena(arenaName, maxPlayers);

		//Now here is where you would save it all to a file, again, im going to create a null FileConfiguration, USE YOUR OWN!!!
		FileConfiguration fc = PowerBall.instance.getConfig(); //USE YOUR OWN PUNK

		fc.set("arenas." + arenaName, null); //Set its name
		//Now sets the other values

		arena.setName(arenaName);
		arena.setMaxPlayers(maxPlayers);
		
		String path = "arenas." + arenaName + "."; //Shortcut	
		
		fc.set(path + "status", "DISABLED");
		
		fc.set(path + "maxPlayers", maxPlayers);
		fc.set(path + "world", player.getWorld().getName());
		
		//Now save it up down here
		PowerBall.instance.saveConfig();
		*/
		
		
		/* REWRITTEN CODE BY ROUGHDRAGON */
		
		FileConfiguration fc = GunGame.instance.getConfig();
		
		if(!(getArena(arenaName) == null)) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena " + arenaName + " already exists!");
			return;
		}
		
		int maxPlayers = 0;
		try {
			maxPlayers = Integer.parseInt(maxPlayersString);
		} catch (NumberFormatException e) {
			ChatUtilities.sendMessage(player, SendType.ERROR, maxPlayersString + " is not a valid Integer.");
			return;
		}
			
		Arena arena = new Arena(arenaName, maxPlayers);
		
		fc.set("arenas." + arenaName, null);
		
		arena.setName(arenaName);
		arena.setMaxPlayers(maxPlayers);
		
		String path = "arenas." + arenaName + ".";	
		
		fc.set(path + "status", "DISABLED");
		
		fc.set(path + "maxPlayers", maxPlayers);
		fc.set(path + "world", player.getWorld().getName());
		
		GunGame.instance.saveConfig();
		
		ChatUtilities.sendMessage(player, SendType.GOOD, "The Arena " + arenaName + " has been created! Use /pb enable " + arenaName + " to ENABLE the Arena.");
		
	}
	
	public void removeArena(Player player, String arenaName) {
		
		if(getArena(arenaName) == null) {
			/* Specified Arena does not exist */
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		FileConfiguration fc = GunGame.instance.getConfig();
		String path = "arenas." + arenaName;
		
		Arena arenaToRemove = getArena(arenaName);
		
		if(fc.contains(path)) {
			fc.set(path, null);
			Arena.arenaObjects.remove(arenaToRemove);
			GunGame.instance.saveConfig();
			ChatUtilities.sendMessage(player, SendType.GOOD, "The Arena " + arenaToRemove.getName() + " has been removed!");
		}
	}
	
	public void setJoinLocation(Player player, String arenaName) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		Arena arena = getArena(arenaName);
		
		double x = player.getLocation().getBlockX() + 0.5;
		double y = player.getLocation().getBlockY() + 1;
		double z = player.getLocation().getBlockZ() + 0.5;
		World world = player.getWorld();
		
		FileConfiguration fc = GunGame.instance.getConfig();
		
		String path = "arenas." + arenaName + ".";
		
		fc.set(path + "joinX", x);
		fc.set(path + "joinY", y);
		fc.set(path + "joinZ", z);
		
		Location joinLocation = new Location(world, x, y, z);
		
		arena.setJoinLocation(joinLocation);
		
		GunGame.instance.saveConfig();
		
		ChatUtilities.sendMessage(player, SendType.GOOD, "You have set the JOIN LOCATION for Arena " + arenaName + "!");
		
	}
	
	public void setEndLocation(Player player, String arenaName) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		Arena arena = getArena(arenaName);
		
		double x = player.getLocation().getBlockX() + 0.5;
		double y = player.getLocation().getBlockY() + 1;
		double z = player.getLocation().getBlockZ() + 0.5;
		World world = player.getWorld();
		
		FileConfiguration fc = GunGame.instance.getConfig();
		
		String path = "arenas." + arenaName + ".";
		
		fc.set(path + "endX", x);
		fc.set(path + "endY", y);
		fc.set(path + "endZ", z);
		
		Location endLocation = new Location(world, x, y, z);
		
		arena.setEndLocation(endLocation);
				
		GunGame.instance.saveConfig();
		
		ChatUtilities.sendMessage(player, SendType.GOOD, "You have set the END LOCATION for Arena " + arenaName + "!");
		
	}
	
	public void forceStart(Player player, String arenaName) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		startArena(arenaName);
		ChatUtilities.sendMessage(player, SendType.GOOD, "You have FORCE STARTED Arena " + arenaName + "!");
		
		Arena arena = getArena(arenaName);
		ChatUtilities.powerballBroadcast(arena, "The Arena has been FORCE STARTED by " + player.getName().toString() + "!");
	}
	
	public void forceEnd(Player player, String arenaName) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		endArena(player, arenaName);
		ChatUtilities.sendMessage(player, SendType.GOOD, "You have FORCE ENDED Arena " + arenaName + "!");
		
		Arena arena = getArena(arenaName);
		ChatUtilities.powerballBroadcast(arena, "The Arena has been FORCE ENDED by " + player.getName().toString() + "!");
	}
	
	public void setStatus(Player player, String arenaName, String status) {
		
		FileConfiguration fc = GunGame.instance.getConfig();
		
		String path = "arenas." + arenaName + ".";
		
		if(getArena(arenaName) == null) {
			/* Specified Arena does not exist */
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		Arena arena = getArena(arenaName);
		
		if(getArenaStatus(arenaName) == Status.ENABLED && status.equalsIgnoreCase("enable")) {
			/* Arena is already Enabled */
			ChatUtilities.sendMessage(player, SendType.NORMAL, "The Arena " + arena.getName() + " is already ENABLED!");
			return;
		}
		
		if(getArenaStatus(arenaName) == Status.DISABLED && status.equalsIgnoreCase("disable")) {
			/* Arena is already Enabled */
			ChatUtilities.sendMessage(player, SendType.NORMAL, "The Arena " + arena.getName() + " is already DISABLED!");
			return;
		}
		
		/* Checks done */
		if(getArenaStatus(arenaName) == Status.DISABLED && status.equalsIgnoreCase("enable")) {
			/* Enable a Disabled Arena */
			if(!fc.contains(path + "joinX") && !fc.contains(path + "joinY") && !fc.contains(path + "joinZ")) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "Join Location does not exist. You cannot enable this Arena.");
				return;
			}		
			if(!fc.contains(path + "redteam.2") && !fc.contains(path + "blueteam.2")) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "Team Spawn Locations do not exist. You cannot enable this Arena.");
				return;
			}		
			if(!fc.contains(path + "endX") && !fc.contains(path + "endY") && !fc.contains(path + "endZ")) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "End Location does not exist. You cannot enable this Arena.");
				return;
			}
			
			fc.set(path + "status", "ENABLED");
			GunGame.instance.saveConfig();		
			ChatUtilities.sendMessage(player, SendType.GOOD, "The Arena " + arena.getName() + " has been ENABLED!");
			return;
		}
		
		if(getArenaStatus(arenaName) == Status.ENABLED && status.equalsIgnoreCase("disable")) {
			fc.set(path + "status", "DISABLED");
			GunGame.instance.saveConfig();		
			ChatUtilities.sendMessage(player, SendType.GOOD, "The Arena " + arena.getName() + " has been DISABLED!");
			return;
		}
	}
	
	public void setTeamSpawn(Player player, String teamName, String arenaName) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
		
		Arena arena = getArena(arenaName);
				
		double x = player.getLocation().getBlockX() + 0.5;
		double y = player.getLocation().getBlockY() + 1;
		double z = player.getLocation().getBlockZ() + 0.5;
		World world = player.getWorld();	
		Location setLocation = new Location(world, x, y, z);
		
		FileConfiguration fc = GunGame.instance.getConfig();
		String path = "arenas." + arenaName + ".";
		
		if(teamName.equalsIgnoreCase("RED")) {
			if(arena.getTeamSpawns(Team.BLUE).contains(setLocation)) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "This Spawn Location is for the Blue Team.");		
				return;
			}
			if(arena.getTeamSpawns(Team.RED).contains(setLocation)) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "This Spawn Location is already for the Red Team.");
				return;
			}
			arena.setTeamSpawns(setLocation, Team.RED);		
			int currentRedSpawns = arena.redSpawn.size();
			fc.set(path + "redteam." + (currentRedSpawns) + ".spawnX" , x);
			fc.set(path + "redteam." + (currentRedSpawns) + ".spawnY" , y);
			fc.set(path + "redteam." + (currentRedSpawns) + ".spawnZ" , z);
			GunGame.instance.saveConfig();	
			ChatUtilities.sendMessage(player, SendType.GOOD, "Spawn Location " + "<" + currentRedSpawns + ">" + " set for Team " + ChatColor.RED + "RED" + ChatColor.GREEN + ".");
		}
		else if(teamName.equalsIgnoreCase("BLUE")) {
			if(arena.getTeamSpawns(Team.RED).contains(setLocation)) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "This Spawn Location is for the Red Team.");		
				return ;
			}
			if(arena.getTeamSpawns(Team.BLUE).contains(setLocation)) {
				ChatUtilities.sendMessage(player, SendType.ERROR, "This Spawn Location is already for the Blue Team.");
				return;
			}
			arena.setTeamSpawns(setLocation, Team.BLUE);
			int currentBlueSpawns = arena.blueSpawn.size();
			fc.set(path + "blueteam." + (currentBlueSpawns) + ".spawnX" , x);
			fc.set(path + "blueteam." + (currentBlueSpawns) + ".spawnY" , y);
			fc.set(path + "blueteam." + (currentBlueSpawns) + ".spawnZ" , z);
			GunGame.instance.saveConfig();
			ChatUtilities.sendMessage(player, SendType.GOOD, "Spawn Location " + "<" + currentBlueSpawns + ">" + " set for Team " + ChatColor.AQUA + "BLUE" + ChatColor.GREEN + ".");
		}
		else {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The <team> you have specified does not exist!");
			return;
		}
	}
	
	public void removeTeamSpawn(Player player, String arenaName, String team, String spawnNumber) {
		if(getArena(arenaName) == null) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The Arena you have specified does not exist!");
			return;
		}
				
		int number = 0;
		try {
			number = Integer.parseInt(spawnNumber);
		} catch (NumberFormatException e) {
			ChatUtilities.sendMessage(player, SendType.ERROR, "<number> must be an Integer.");
			return;
		}
		
		FileConfiguration fc = GunGame.instance.getConfig();
		String path = "arenas." + arenaName + ".";
		
		if(team.equalsIgnoreCase("BLUE")) {
			if(fc.contains(path + "blueteam." + number)) {
				fc.set(path + "blueteam." + number, null);
				GunGame.instance.saveConfig();
				ChatUtilities.sendMessage(player, SendType.ERROR, "You have removed Spawn Number " + number + " for the " + ChatColor.AQUA + "BLUE " + ChatColor.GREEN + "team.");
				return;
			} else {
				ChatUtilities.sendMessage(player, SendType.ERROR, "The Blue Team Spawn <number> you have specified does not exist.");
				return;
			}
		}
		else if(team.equalsIgnoreCase("RED")) {
			if(fc.contains(path + "redteam." + number)) {
				fc.set(path + "redteam." + number, null);
				GunGame.instance.saveConfig();
				ChatUtilities.sendMessage(player, SendType.ERROR, "You have removed Spawn Number " + number + " for the " + ChatColor.RED + "RED " + ChatColor.GREEN + "team.");
				return;
			} else {
				ChatUtilities.sendMessage(player, SendType.ERROR, "The Red Team Spawn <number> you have specified does not exist.");
				return;
			}
		}
		else {
			ChatUtilities.sendMessage(player, SendType.ERROR, "The <team> you have specified does not exist!");
			return;
		}
		
	}
		
}


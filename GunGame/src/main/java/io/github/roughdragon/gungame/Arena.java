package io.github.roughdragon.gungame;

import java.util.ArrayList;

import io.github.roughdragon.gungame.utilities.ArenaCountdown;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Arena {

	//A list of all the Arena Objects
	public static ArrayList<Arena> arenaObjects = new ArrayList<Arena>();

	//Some fields we want each Arena object to store:
	private Location joinLocation, startLocation, endLocation; //Some general arena locations
	
	public ArrayList<Location> redSpawn = new ArrayList<Location>();
	public ArrayList<Location> blueSpawn = new ArrayList<Location>();

	private String name; //Arena name
	private ArrayList<String> players = new ArrayList<String>(); //And arraylist of players name
	public ArrayList<String> redTeamPlayers = new ArrayList<String>();
	public ArrayList<String> blueTeamPlayers = new ArrayList<String>();

	private int maxPlayers;

	private boolean inGame = false; //Boolean to determine if an Arena is ingame or not, automaticly make it false

	public enum Team { RED, BLUE }
	
	public int countdownID;
	public boolean countdownDone;
	public int timeLeft;

	//Now for a Constructor:
	public Arena (String arenaName, /* Location joinLocation, Location startLocation, Location endLocation, */ int maxPlayers) { //So basicly: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)
		//Lets initalize it all:
		this.name = arenaName;
		//this.joinLocation = joinLocation;
		//this.startLocation = startLocation;
		//this.endLocation = endLocation;
		this.maxPlayers = maxPlayers;

		//Now lets add this object to the list of objects:
		arenaObjects.add(this);

	}

	//Now for some Getters and Setters, so with our arena object, we can use special methods:
	public Location getJoinLocation() {
		return this.joinLocation;
	}

	public void setJoinLocation(Location joinLocation) {
		this.joinLocation = joinLocation;
	}

	// Team Spawn Getters/Setters
	public ArrayList<Location> getTeamSpawns(Team team) {
		if(team.equals(Team.RED)) { return redSpawn; } 
		else if(team.equals(Team.BLUE)) { return blueSpawn; }
		return null;
	}
	
	public void setTeamSpawns(Location spawnLocation, Team team) {
		if(team.equals(Team.RED)) { redSpawn.add(spawnLocation); }
		if(team.equals(Team.BLUE)) { blueSpawn.add(spawnLocation); }
	}
	// End Team Spawn Getters/Setters
	
	// Team Player List
	public ArrayList<String> getTeamPlayers(Team team) {
		if(team.equals(Team.RED)) { return redTeamPlayers; }
		else if(team.equals(Team.BLUE)) { return blueTeamPlayers; }
		return null;
	}
	
	public void setPlayerTeam(String playerName, Team team) {
		if(team.equals(Team.RED)) { redTeamPlayers.add(playerName); }
		else if(team.equals(Team.BLUE)) { blueTeamPlayers.add(playerName); }
	}
	// End Team Player List
	
	public Location getStartLocation() {
		return this.startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return this.endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public ArrayList<String> getPlayers() {
		return this.players;
	}

	//And finally, some booleans:
	public boolean isFull() { //Returns weather the arena is full or not
		if (players.size() >= maxPlayers) {
			return true;
		} else {
			return false;
		}
	}


	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	//To send each player in the arena a message
	@SuppressWarnings("deprecation")
	public void sendMessage(String message) {
		for (String s: players) {
			Bukkit.getPlayer(s).sendMessage(message);
		}
	}
	
	public void startCountdown() {
		timeLeft = 30;
		countdownID = GunGame.instance.getServer().getScheduler().scheduleSyncRepeatingTask(GunGame.instance, new ArenaCountdown(this), 20l, 20l);
	}
	
	public void stopCountdown()  {
		GunGame.instance.getServer().getScheduler().cancelTask(countdownID);
	}

}

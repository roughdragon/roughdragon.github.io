package io.github.roughdragon.playerinformer;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInformer extends JavaPlugin {
	
	public File piFile = new File(getDataFolder(), "banList.yml");
	public FileConfiguration piFileConfiguration = YamlConfiguration.loadConfiguration(piFile);
	
	public File tbFile = new File(getDataFolder(), "tempbans.yml");
	public FileConfiguration tbFileConfiguration = YamlConfiguration.loadConfiguration(tbFile);

	@Override
	public void onEnable() {
		getLogger().info("[PlayerInformer] has been ENABLED!");
		
		getServer().getPluginManager().registerEvents(new Ban(), this);
		getServer().getPluginManager().registerEvents(new Whitelist(), this);
		getServer().getPluginManager().registerEvents(new TempBan(), this);
		getCommand("ban").setExecutor(new Ban());
		getCommand("kick").setExecutor(new Kick());
		getCommand("tempban").setExecutor(new TempBan());
	}
	
	@Override
	public void onDisable() {
		getLogger().info("[PlayerInformer] has been DISABLED!");
	}
	
	public String getStarter() {
		return ChatColor.GOLD + "[PlayerInformer]";
	}
	
	public FileConfiguration getPiConfig() {
		if(piFileConfiguration == null) {
			reloadPiConfig();
		}
		return piFileConfiguration;
	}
	
	public void reloadPiConfig() {
		if(piFile == null) {
			piFile = new File(getDataFolder(), "banList.yml");
		}
		piFileConfiguration = YamlConfiguration.loadConfiguration(piFile);
	}
	
	public void savePiConfig() {
		if(piFileConfiguration == null || piFile == null) {
			return;
		}
		try {
			getPiConfig().save(piFile);
		} catch (IOException ex) {
			getLogger().info("[PlayerInformer] 'banList.yml' could not be saved!");
			ex.printStackTrace();
		}
	}
	
	public FileConfiguration getTempBans() {
		if(tbFileConfiguration == null) {
			reloadTempBans();
		}
		return tbFileConfiguration;
	}
	
	public void reloadTempBans() {
		if(tbFile == null) {
			tbFile = new File(getDataFolder(), "tempbans.yml");
		}
		tbFileConfiguration = YamlConfiguration.loadConfiguration(tbFile);
	}
	
	public void saveTempBans() {
		if(tbFileConfiguration == null || tbFile == null) {
			return;
		}
		try {
			getTempBans().save(tbFile);
		} catch (IOException ex) {
			getLogger().info("[PlayerInformer] 'tempbans.yml' could not be saved!");
			ex.printStackTrace();
		}
	}
	
}

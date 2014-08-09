package io.github.roughdragon.MinigameTokens;
import io.github.roughdragon.MinigameTokens.Listeners.PlayerKilled;
import io.github.roughdragon.MinigameTokens.Listeners.WorldChange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class MinigameTokens extends JavaPlugin {

	public static MinigameTokens instance;
	public static MinigameTokens getInstance() { return instance; }
	
	public File tcFile = new File(getDataFolder(), "tokens.yml");
	public FileConfiguration tcFileConfiguration = YamlConfiguration.loadConfiguration(tcFile);
	
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info(" has been Enabled!");
		
		registerListeners();
		createConfig();
		saveTokensConfig();
		
		getCommand("tokens").setExecutor(new TokenCommand());
	}
	
	@Override
	public void onDisable() {
		instance = null;
		getLogger().info(" has been Disabled!");
	}
	
	public void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerKilled(), instance);
		pm.registerEvents(new WorldChange(), instance);
	}
	
	public void createConfig() {
		if(!getDataFolder().exists()) {
			try { getDataFolder().createNewFile(); } 
			catch (IOException ex) { getLogger().severe("[MinigameTokens] Internal Error Occured! Please Send A Bug Report To RoughDragon."); }
		}
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			getLogger().info("[MinigameTokens] Config file found, loading...");
			return;
		}
		
		getLogger().info("[MinigameTokens] Config file not found, creating...");
		try {
			FileOutputStream writer = new FileOutputStream(new File(getDataFolder() + File.separator + "config.yml"));
			InputStream out = getResource("config.yml");
			byte[] linebuffer = new byte[4096];
			int lineLength = 0;
			while((lineLength = out.read(linebuffer)) >= 0)
			{
			   writer.write(linebuffer, 0, lineLength);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().info("[MinigameTokens] Error Occured. Config File not created!");
		}
	}
	
	public FileConfiguration getTokensConfig() {
		if(tcFileConfiguration == null) {
			reloadTokensConfig();
		}
		return tcFileConfiguration;
	}
	
	public void reloadTokensConfig() {
		if(tcFile == null) {
			tcFile = new File(getDataFolder(), "tokens.yml");
		}
		tcFileConfiguration = YamlConfiguration.loadConfiguration(tcFile);
	}
	
	public void saveTokensConfig() {
		if(tcFileConfiguration == null || tcFile == null) {
			return;
		}
		try {
			getTokensConfig().save(tcFile);
		} catch (IOException ex) {
			getLogger().info("[MinigameTokens] 'tokens.yml' could not be saved!");
			ex.printStackTrace();
		}
	}		
}

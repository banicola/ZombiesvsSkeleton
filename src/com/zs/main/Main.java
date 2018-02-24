package com.zs.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.commands.NoCommand;
import com.zs.commands.YesCommand;
import com.zs.commands.ZombiesvsSkeletonCommand;
import com.zs.event.PlayerInventoryClick;
import com.zs.event.PlayerLeaveServer;
import com.zs.event.PlayerMove;
import com.zs.event.deathPlayer;
import com.zs.event.foodLevelEvent;
import com.zs.game.KickPlayerGame;

import de.robingrether.idisguise.api.DisguiseAPI;

public class Main extends JavaPlugin{
	  
	
	public static DisguiseAPI api;
	
	public static List<Player> waitingPlayers = new ArrayList<Player>();
	public static List<Player> inGamePlayers = new ArrayList<Player>();
	public static List<String> allGames = new ArrayList<String>();
	public static List<String> availableGames = new ArrayList<String>();
	public static List<String> activeGames = new ArrayList<String>();
	
	public static HashMap<String, List<Player>> playersTeamZombie = new HashMap<String, List<Player>>();
	public static HashMap<String, List<Player>> playersTeamSkeleton = new HashMap<String, List<Player>>();
	
	public static HashMap<Player, String> playerKit = new HashMap<Player, String>();
	public static HashMap<Player, Integer> playerKill = new HashMap<Player, Integer>();
	
	
	public static HashMap<String, List<Player>> listPlayerGame = new HashMap<String, List<Player>>();
	public static HashMap<Player, Location> oldPlayerLocation = new HashMap<Player, Location>();
	public static HashMap<Player, ItemStack[]> oldPlayerInventory = new HashMap<Player, ItemStack[]>();
	public static HashMap<Player, Float> oldPlayerExp = new HashMap<Player, Float>();
	public static HashMap<Player, Integer> oldPlayerLevel = new HashMap<Player, Integer>();
	public static HashMap<Player, Double> oldPlayerHealth = new HashMap<Player, Double>();
	public static HashMap<Player, Integer> oldPlayerFood = new HashMap<Player, Integer>();
	
	public static HashMap<Player, Boolean> waitingConfirmation = new HashMap<Player, Boolean>();
	public static HashMap<Player, String> waitingAction = new HashMap<Player, String>();
	public static HashMap<Player, String> gameAction = new HashMap<Player, String>();
	
	public static File configFile, gamesFile;
    public static FileConfiguration config;
	public static FileConfiguration gamesConfig;
	
    public void onEnable(){
    	
    	api = this.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
    	
    	createFiles();
    	
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerLeaveServer(), this);
        pm.registerEvents(new PlayerInventoryClick(), this);
        pm.registerEvents(new PlayerMove(), this);
        pm.registerEvents(new deathPlayer(), this);
        pm.registerEvents(new foodLevelEvent(), this);
    	
    	if (getConfig().getStringList("games.list").size() != 0){
    		allGames.addAll(config.getStringList("games.list"));
    		for (String game : allGames){
    			List<Player> newPlayerList = new ArrayList<Player>();
    			listPlayerGame.put(game, newPlayerList);
    		}
    	}
    	
    	CommandExecutor zombiesvsskeletonExecutor = new ZombiesvsSkeletonCommand();
    	getCommand("zombiesvsskeleton").setExecutor(zombiesvsskeletonExecutor);
    	
    	CommandExecutor yesExecutor = new YesCommand();
    	getCommand("y").setExecutor(yesExecutor);
    	
    	CommandExecutor noExecutor = new NoCommand();
    	getCommand("n").setExecutor(noExecutor);
    	
    	for (String game : allGames){
    		if (getGamesConfig().getBoolean("games."+game+".active")){
    			availableGames.add(game);
    		}
    	}
    }

	public void onDisable(){
		if (allGames.size() != 0){
			for(String game : Main.availableGames){
				if(!Main.listPlayerGame.get(game).isEmpty()){
					List<Player> player = new ArrayList<Player>();
					for (Player p : Main.listPlayerGame.get(game)){
						player.add(p);
					}
					for (Player p : player){
						KickPlayerGame.kickPlayer(p, game, "end");
					}
				}
			}
			config.set("games.list", allGames);
			for (String game : allGames){
				if (getGamesConfig().getList("games."+game+".players").size() != 0){
					getGamesConfig().getList("games."+game+".players").clear();
				}
			}
			try {
				config.save(configFile);
				getGamesConfig().save(gamesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
    }
	
	public static String getPlayerGame(Player player){
		for(String game : allGames){
			if (listPlayerGame.get(game).contains(player)){
				return game;
			}
		}
		return null;
	}
	
	public static String getPlayerTeam(Player player){
		if(Main.playersTeamSkeleton.get(getPlayerGame(player)).contains(player)){
			return "skeleton";
		} else if(Main.playersTeamZombie.get(getPlayerGame(player)).contains(player)){
			return "zombie";
		}
		return null;
	}
	
	public FileConfiguration getGamesConfig() {
        return Main.gamesConfig;
    }
	
	private void createFiles() {
		configFile = new File(getDataFolder(), "config.yml");
        gamesFile = new File(getDataFolder(), "games.yml");

        if (!configFile.exists()) {
        	configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!gamesFile.exists()) {
        	gamesFile.getParentFile().mkdirs();
            saveResource("games.yml", false);
         }
        loadFiles();
    }
	
	public static void loadFiles(){
		config = new YamlConfiguration();
        gamesConfig = new YamlConfiguration();
        try {
            config.load(configFile);
            gamesConfig.load(gamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}

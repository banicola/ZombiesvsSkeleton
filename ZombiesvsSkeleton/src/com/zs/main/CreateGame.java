package com.zs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CreateGame extends JavaPlugin{
	
	public static boolean addAllList(String name, Integer minPlayer, Integer maxPlayer){
		if (!Main.allGames.contains(name)){
			
			if (minPlayer == -1){
				minPlayer = Main.config.getInt("games.minPlayer");
			}
			
			if(maxPlayer == -1){
				maxPlayer = Main.config.getInt("games.maxPlayer");
			}
			
			Main.allGames.add(name);
			
			Main.config.set("games.list", Main.allGames);
			
			List<Player> listPlayer = new ArrayList<Player>();
			
			Main.gamesConfig.set("games."+name+".active", false);
			Main.gamesConfig.set("games."+name+".step", "loading");
			
			Main.gamesConfig.set("games."+name+".minPlayer", minPlayer);
			Main.gamesConfig.set("games."+name+".maxPlayer", maxPlayer);
			Main.gamesConfig.set("games."+name+".players", listPlayer);
			Main.gamesConfig.set("games."+name+".spawn.X", -1);
			Main.gamesConfig.set("games."+name+".spawn.Y", -1);
			Main.gamesConfig.set("games."+name+".spawn.Z", -1);
			Main.gamesConfig.set("games."+name+".spawn.facingX", 0);
			Main.gamesConfig.set("games."+name+".spawn.facingY", 0);
			Main.gamesConfig.set("games."+name+".teamZombie.X", -1);
			Main.gamesConfig.set("games."+name+".teamZombie.Y", -1);
			Main.gamesConfig.set("games."+name+".teamZombie.Z", -1);
			Main.gamesConfig.set("games."+name+".teamZombie.facingX", 0);
			Main.gamesConfig.set("games."+name+".teamZombie.facingY", 0);
			Main.gamesConfig.set("games."+name+".teamSkeleton.X", -1);
			Main.gamesConfig.set("games."+name+".teamSkeleton.Y", -1);
			Main.gamesConfig.set("games."+name+".teamSkeleton.Z", -1);
			Main.gamesConfig.set("games."+name+".teamSkeleton.facingX", 0);
			Main.gamesConfig.set("games."+name+".teamSkeleton.facingY", 0);
			
			try {
				Main.config.save(Main.configFile);
				Main.gamesConfig.save(Main.gamesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<Player> playerList = new ArrayList<Player>();
			Main.listPlayerGame.put(name, playerList);
			
			return true;
		}
		return false;
	}

}

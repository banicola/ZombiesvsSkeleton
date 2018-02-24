package com.zs.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.main.Main;

public class StopGame extends JavaPlugin{

	public static void stopGame(String game){
		List<Player> player = new ArrayList<Player>();
		for (Player p : Main.listPlayerGame.get(game)){
			player.add(p);
		}
		for (Player p : player){
			KickPlayerGame.kickPlayer(p, game, "end");
		}
		Main.activeGames.remove(game);
		
		Main.playersTeamZombie.remove(game);
		Main.playersTeamSkeleton.remove(game);
		
		Main.gamesConfig.set("games."+game+".step", "loading");
		
		try {
			Main.gamesConfig.save(Main.gamesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

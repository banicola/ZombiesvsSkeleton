package com.zs.game;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.main.Countdown;
import com.zs.main.Main;

public class ResultGame extends JavaPlugin{
	
	public static void resultGame(String game){
		Main.gamesConfig.set("games."+game+".step", "ending");
		
		try {
			Main.gamesConfig.save(Main.gamesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Player p : Main.listPlayerGame.get(game)){
			p.setSaturation(20);
			p.setHealth(20);
			Bukkit.broadcastMessage(p.getName()+": "+Main.playerKill.get(p)+" kills");
		}
		
		Countdown.CountdownStart(10, game, "ending");
	}
}

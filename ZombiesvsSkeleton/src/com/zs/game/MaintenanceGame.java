package com.zs.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.main.Main;

import net.md_5.bungee.api.ChatColor;

public class MaintenanceGame extends JavaPlugin{

	public static void maintenanceGame(Player sender, String game){
		
		if (Main.availableGames.contains(game)){
			Main.availableGames.remove(game);
			Main.gamesConfig.set("games."+game+".active", false);
			if (!Main.activeGames.contains(game)){
				List<Player> player = new ArrayList<Player>();
				for (Player p : Main.listPlayerGame.get(game)){
					player.add(p);
				}
				for (Player p : player){
					KickPlayerGame.kickPlayer(p, game, "server");
				}
			}
			
			sender.sendMessage(ChatColor.GREEN+"Succesfully set maintenance of "+ChatColor.WHITE+game);
			
		} else if (!Main.gamesConfig.getBoolean("games."+game+".active")){
			
			if((Main.gamesConfig.getDouble("games."+game+".spawn.X") == -1) && (Main.gamesConfig.getDouble("games."+game+".spawn.Y")== -1) && (Main.gamesConfig.getDouble("games."+game+".spawn.Z")== -1) && !(Main.waitingConfirmation.containsKey(sender))){
				sender.sendMessage(ChatColor.RED+"You have not changed the spawn position of this game. Tap "+ChatColor.WHITE+"/y"+ChatColor.RED+" to continue without changing it");
				Main.waitingConfirmation.put(sender, false);
				Main.waitingAction.put(sender, "maintenance");
				Main.gameAction.put(sender, game);
				return;
			}
			
			if (!Main.waitingConfirmation.containsKey(sender)){
				
				Main.availableGames.add(game);
				Main.gamesConfig.set("games."+game+".active", true);
				
				sender.sendMessage(ChatColor.GREEN+"Succesfully removed maintenance of "+ChatColor.WHITE+game);
			}
			
			else if (Main.waitingConfirmation.get(sender)){
				Main.availableGames.add(game);
				Main.gamesConfig.set("games."+game+".active", true);
				
				Main.waitingAction.remove(sender);
				Main.waitingConfirmation.remove(sender);
				Main.gameAction.remove(sender);
				
				sender.sendMessage(ChatColor.GREEN+"Succesfully removed maintenance of "+ChatColor.WHITE+game);
			}
			
		}
		
		try {
			Main.gamesConfig.save(Main.gamesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

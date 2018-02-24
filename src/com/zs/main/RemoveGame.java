package com.zs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.game.KickPlayerGame;

import net.md_5.bungee.api.ChatColor;

public class RemoveGame extends JavaPlugin{
	
	public static boolean removeAvailableList(Player sender, String name){
		if (name == null){
			if (Main.gameAction.containsKey(sender)){
				name = Main.gameAction.get(sender);
			}
		}
		
		if (Main.allGames.contains(name)){
			if (!Main.waitingConfirmation.containsKey(sender)){
				Main.waitingConfirmation.put(sender, false);
				Main.gameAction.put(sender, name);
				Main.waitingAction.put(sender, "remove");
				sender.sendMessage(ChatColor.RED+"Warning! Every data will be deleted! Tap: "+ChatColor.WHITE+"/y"+ChatColor.RED+" to confirm!");
				return false;
			}
			
			
			if (Main.waitingConfirmation.get(sender)){
				if (Main.listPlayerGame.get(name).size() != 0){
					List<Player> player = new ArrayList<Player>();
					for (Player p : Main.listPlayerGame.get(name)){
						player.add(p);
					}
					for (Player p : player){
						KickPlayerGame.kickPlayer(p, name, "server");
					}
				}
				Main.allGames.remove(name);
				
				Main.availableGames.remove(name);
				
				Main.listPlayerGame.remove(name);
				
				Main.gamesConfig.set("games."+name, null);
				Main.config.getStringList("games.list").remove(name);
				
				try {
					Main.config.save(Main.configFile);
					Main.gamesConfig.save(Main.gamesFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			Main.waitingConfirmation.remove(sender);
			Main.waitingAction.remove(sender);
			return true;
			
		}
		else{
			sender.sendMessage(ChatColor.RED+"An error appears. The game "+ChatColor.WHITE+name+ChatColor.RED+" could not exist!");
		}
		return true;
	}
}


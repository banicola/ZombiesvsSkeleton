package com.zs.game;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.zs.main.Main;

import net.md_5.bungee.api.ChatColor;

public class KickPlayerGame extends JavaPlugin{
	
	public static boolean kickPlayer(Player player, String game, String reason){
		if(Main.gamesConfig.getList("games."+game+".players").contains(player)){
			
			Main.inGamePlayers.remove(player);
			
			Main.gamesConfig.getList("games."+game+".players").remove(player);
			
			Main.listPlayerGame.get(game).remove(player);
			
			Main.api.undisguise(player);
			Bukkit.getServer().getPlayer(player.getName()).setExp(Main.oldPlayerExp.get(player));
			Bukkit.getServer().getPlayer(player.getName()).setLevel(Main.oldPlayerLevel.get(player));
			Bukkit.getServer().getPlayer(player.getName()).teleport(Main.oldPlayerLocation.get(player));
			Bukkit.getServer().getPlayer(player.getName()).getInventory().clear();
			Bukkit.getServer().getPlayer(player.getName()).setHealth(Main.oldPlayerHealth.get(player));
			Bukkit.getServer().getPlayer(player.getName()).setFoodLevel(Main.oldPlayerFood.get(player));
			for (PotionEffect effect : player.getActivePotionEffects()){
				Bukkit.getServer().getPlayer(player.getName()).removePotionEffect(effect.getType());
			}
			
			for (ItemStack item : Main.oldPlayerInventory.get(player)){
				if(item != null){
					Bukkit.getServer().getPlayer(player.getName()).getInventory().addItem(item);
				}
			}
			
			
			Main.oldPlayerLocation.remove(player);
			Main.oldPlayerInventory.remove(player);
			Main.oldPlayerExp.remove(player);
			Main.oldPlayerLevel.remove(player);
			Main.oldPlayerHealth.remove(player);
			Main.oldPlayerFood.remove(player);
			
			Main.playerKit.remove(player);
			Main.playerKill.remove(player);
			
			player.setGameMode(GameMode.SURVIVAL);
			
			if(!Main.playersTeamZombie.isEmpty()){
				if(Main.playersTeamZombie.get(game).contains(player)){
					Main.playersTeamZombie.get(game).remove(player);
				} else if(Main.playersTeamSkeleton.get(game).contains(player)){
					Main.playersTeamSkeleton.get(game).remove(player);
				}
			}
			
			try {
				Main.gamesConfig.save(Main.gamesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(Player p : Main.listPlayerGame.get(game)){
				if(Main.gamesConfig.getString("games."+game+".step") != "ending"){
					p.sendMessage(ChatColor.GREEN+player.getName()+ChatColor.BLUE+" left the game!");
				}
			}
			
			if (reason.contentEquals("admin") || (reason.contentEquals("server"))){
				Bukkit.getPlayer(player.getName()).sendMessage(ChatColor.RED+"You have been kicked from "+ChatColor.WHITE+game+ChatColor.RED+" by an administrator");
				if (reason.contentEquals("server")){
					Bukkit.getPlayer(player.getName()).sendMessage(ChatColor.RED+"Reason: This game has been removed by an administrator");
				}
			}
			else if (reason.contentEquals("end")){
				Bukkit.getPlayer(player.getName()).sendMessage(ChatColor.BLUE+"The game is end! Thanks for playing!");
			}
			else{
				if(Bukkit.getOnlinePlayers().contains(player)){
					Bukkit.getPlayer(player.getName()).sendMessage(ChatColor.BLUE+"You left the game!");
				}
			}
			
			return true;
		}
		return false;
	}
}

package com.zs.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.main.Countdown;
import com.zs.main.Countdown_Event;
import com.zs.main.Main;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinGame extends JavaPlugin{
	public static void addPlayerList(Player p){
		
		if (Main.availableGames.size() != 0){
			for(String game : Main.availableGames){
				Main.waitingPlayers.add(p);
				if ((Main.listPlayerGame.get(game).size() < Main.gamesConfig.getInt("games."+game+".maxPlayer")) && !Main.activeGames.contains(game)){
					Main.waitingPlayers.remove(p);
					
					Main.inGamePlayers.add(p);
					
					Main.listPlayerGame.get(game).add(p);
					
					if(!Main.playersTeamZombie.containsKey(game)){
		  			  	Main.playersTeamZombie.put(game, new ArrayList<Player>());
		  		  	}
		  		  	if(!Main.playersTeamSkeleton.containsKey(game)){
		  		  		Main.playersTeamSkeleton.put(game, new ArrayList<Player>());
		  		  	}
					
					Integer numberPlayer = Main.listPlayerGame.get(game).size();
					
					for(Player player : Main.listPlayerGame.get(game)){
						if(player != p){
							player.sendMessage(ChatColor.GREEN+p.getName()+ChatColor.BLUE+" joined the game as "+ChatColor.GREEN+numberPlayer+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer"));
						}
					}
					
					Main.gamesConfig.set("games."+game+".players", Main.listPlayerGame.get(game));
					
					try {
						Main.gamesConfig.save(Main.gamesFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Main.playerKill.put(p, 0);
					
					Location oldPlayerLocation = Bukkit.getServer().getPlayer(p.getName()).getLocation();
					Main.oldPlayerLocation.put(p, oldPlayerLocation);
					
					ItemStack[] oldPlayerInventory = Bukkit.getServer().getPlayer(p.getName()).getInventory().getContents();
					Main.oldPlayerInventory.put(p, oldPlayerInventory);
					
					Float oldPlayerExp = Bukkit.getServer().getPlayer(p.getName()).getExp();
					Main.oldPlayerExp.put(p,oldPlayerExp);
					
					Integer oldPlayerLevel = Bukkit.getServer().getPlayer(p.getName()).getLevel();
					Main.oldPlayerLevel.put(p,oldPlayerLevel);
					
					Double oldPlayerHealth = Bukkit.getServer().getPlayer(p.getName()).getHealth();
					Main.oldPlayerHealth.put(p, oldPlayerHealth);
					
					Integer oldPlayerFood = Bukkit.getServer().getPlayer(p.getName()).getFoodLevel();
					Main.oldPlayerFood.put(p, oldPlayerFood);
					
					Location gameLocation = new Location(p.getWorld(), Main.gamesConfig.getDouble("games."+game+".spawn.X"), Main.gamesConfig.getDouble("games."+game+".spawn.Y"), Main.gamesConfig.getDouble("games."+game+".spawn.Z"), (float) Main.gamesConfig.getDouble("games."+game+".spawn.facingX"), (float) Main.gamesConfig.getDouble("games."+game+".spawn.facingY"));
					
					Bukkit.getServer().getPlayer(p.getName()).teleport(gameLocation);
					Bukkit.getServer().getPlayer(p.getName()).getInventory().clear();
					Bukkit.getServer().getPlayer(p.getName()).setExp(0);
					Bukkit.getServer().getPlayer(p.getName()).setLevel(0);
					Bukkit.getServer().getPlayer(p.getName()).setHealth(20);
					Bukkit.getServer().getPlayer(p.getName()).setFoodLevel(20);
					
					ItemStack skullZ = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
					SkullMeta meta1 = (SkullMeta) skullZ.getItemMeta();
					meta1.setDisplayName("Team Zombie");
					List<String> lore1 = Arrays.asList("Join team Zombie", Main.playersTeamZombie.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
					meta1.setLore(lore1);
					skullZ.setItemMeta(meta1);
					
					ItemStack skullS = new ItemStack(Material.SKULL_ITEM);
					SkullMeta meta2 = (SkullMeta) skullS.getItemMeta();
					meta2.setDisplayName("Team Skeleton");
					List<String> lore2 = Arrays.asList("Join team Skeleton", Main.playersTeamSkeleton.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
					meta2.setLore(lore2);
					skullS.setItemMeta(meta2);
					
					ItemStack kit = new ItemStack(Material.ANVIL);
					ItemMeta meta3 = (ItemMeta) kit.getItemMeta();
					meta3.setDisplayName("Kit");
					kit.setItemMeta(meta3);
					
					Bukkit.getServer().getPlayer(p.getName()).getInventory().setItem(0, skullZ);
					Bukkit.getServer().getPlayer(p.getName()).getInventory().setItem(8, skullS);
					Bukkit.getServer().getPlayer(p.getName()).getInventory().setItem(4, kit);
					
					p.sendMessage(ChatColor.BLUE + "You joined the game " + ChatColor.GREEN + game + ChatColor.BLUE + " ! You are " + ChatColor.GREEN + numberPlayer +"/"+ Main.gamesConfig.getInt("games."+game+".maxPlayer"));
					
					if(Main.listPlayerGame.get(game).size() == Main.gamesConfig.getInt("games."+game+".minPlayer")){
						Countdown_Event e = new Countdown_Event();
						Bukkit.getServer().getPluginManager().callEvent(e);
						Countdown.CountdownStart(Main.config.getInt("games.start_timer"), game, "load");
					}
					return;
				}	
			}
			Main.waitingPlayers.remove(p);
			p.sendMessage(ChatColor.RED+"Sorry, all the games are full! Try later!");
		}
		else{
			p.sendMessage(ChatColor.RED + "This game is currently unavailable!");
		}
		return;
	}
}

package com.zs.main;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.zs.game.ResultGame;
import com.zs.game.StartGame;
import com.zs.game.StopGame;

import net.md_5.bungee.api.ChatColor;

public class Countdown extends JavaPlugin{
	
	static int time;
	static int TaskID;
	String game;
	
	public static void CountdownStart(int amount, String game, String step) {
        time = amount;
        
        TaskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("ZombiesvsSkeleton"), new Runnable() {
            @Override
            public void run() {
            	if(step == "load"){
            		if(Main.listPlayerGame.get(game).size() < Main.gamesConfig.getInt("games."+game+".minPlayer")){
                		Bukkit.getServer().getScheduler().cancelTask(TaskID);
                		for (Player p : Main.listPlayerGame.get(game)){
                			p.setLevel(0);
                		}
                		return;
                	}
                	
                	if(time == amount){
                		for (Player p : Main.listPlayerGame.get(game)){
                			p.sendMessage(ChatColor.LIGHT_PURPLE+"The game start in "+ChatColor.WHITE+amount+ChatColor.LIGHT_PURPLE+" seconds!");
                		}
                	}
                	
            		if(time >= 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.setLevel(time);
            				if(time <= 5){
            					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 10, 1);
            				}
            				if(time == 0 && Main.getPlayerTeam(p) == null){
								String team;
            					final String[] teamList = {"zombie", "skeleton"};
            					Random random = new Random();
            					int i = random.nextInt(teamList.length);
            					team = teamList[i];
            					if(team == "zombie"){
            						Main.playersTeamZombie.get(game).add(p);
            					} else {
            						Main.playersTeamSkeleton.get(game).add(p);
            					}
            					p.sendMessage(ChatColor.BLUE+"You have joined randomly the team "+ChatColor.GREEN+team);
            				}
            			}
            			time--;
            		}
            		
            		if(time == -1){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.setLevel(0);
            			}
            			time--;
            			Bukkit.getServer().getScheduler().cancelTask(TaskID);
            			StartGame.startGame(game);
            		}
            	} else if (step == "starting_game"){
            		if(time == amount){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.sendMessage(ChatColor.BLUE+"The battle start in");
            			}
            		}
            		if(time > 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.setLevel(time);
            				p.sendMessage(ChatColor.GREEN+""+time);
            			}
            		}
            		if(time == 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.setLevel(0);
            				p.sendMessage(ChatColor.LIGHT_PURPLE+"Go");
            			}
            			Bukkit.getServer().getScheduler().cancelTask(TaskID);
            			StartGame.LauchGame(game);
            		}
            		time--;
            		
            	} else if(step == "playing"){
            		if(Main.playersTeamZombie.get(game).size() == 0 || Main.playersTeamSkeleton.get(game).size() == 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.sendMessage(ChatColor.BLUE+"Good job. All the enemies have been killed!");
            			}
            			Bukkit.getServer().getScheduler().cancelTask(TaskID);
            			ResultGame.resultGame(game);
            		}
            		if(time <= 3 && time > 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				if(time == 3){
            					p.sendMessage(ChatColor.BLUE+"The game will end in");
            				}
            				p.sendMessage(ChatColor.GREEN+""+time);
            			}
            		}
            		else if(time == 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.sendMessage(ChatColor.BLUE+"Time is up! Now the results!");
            			}
            			Bukkit.getServer().getScheduler().cancelTask(TaskID);
            			ResultGame.resultGame(game);
            		}
            		time--;
            	} else if(step == "ending"){
            		if(time > 0){
            			for (Player p : Main.listPlayerGame.get(game)){
            				p.setLevel(time);
            			}
            		}
            		if(time == 0){
            			Bukkit.getServer().getScheduler().cancelTask(TaskID);
            			StopGame.stopGame(game);
            		}
            		time--;
            	}
            }
        }, 0L, 20L);
    }
}

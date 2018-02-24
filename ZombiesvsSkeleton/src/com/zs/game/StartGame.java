package com.zs.game;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.zs.main.Countdown;
import com.zs.main.KitMenu;
import com.zs.main.Main;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;

public class StartGame extends JavaPlugin{
	
	public static void startGame(String game){
		World world = Bukkit.getPlayer("clipsy1").getLocation().getWorld();
		Location teamZombieLocation = new Location(world, Main.gamesConfig.getDouble("games."+game+".teamZombie.X"), Main.gamesConfig.getDouble("games."+game+".teamZombie.Y"), Main.gamesConfig.getDouble("games."+game+".teamZombie.Z"), (float) Main.gamesConfig.getDouble("games."+game+".teamZombie.facingX"), (float) Main.gamesConfig.getDouble("games."+game+".teamZombie.facingY"));
		Location teamSkeletonLocation = new Location(world, Main.gamesConfig.getDouble("games."+game+".teamSkeleton.X"), Main.gamesConfig.getDouble("games."+game+".teamSkeleton.Y"), Main.gamesConfig.getDouble("games."+game+".teamSkeleton.Z"), (float) Main.gamesConfig.getDouble("games."+game+".teamSkeleton.facingX"), (float) Main.gamesConfig.getDouble("games."+game+".teamSkeleton.facingY"));
		
		for(Player p : Main.listPlayerGame.get(game)){
			p.getInventory().clear();
			KitMenu.setKitPlayer(p);
			if(Main.playersTeamZombie.get(game).contains(p)){
				Disguise mob = new MobDisguise(DisguiseType.ZOMBIE);
				Main.api.disguise(p, mob);
				p.teleport(teamZombieLocation);
			} else if (Main.playersTeamSkeleton.get(game).contains(p)){
				Disguise mob = new MobDisguise(DisguiseType.SKELETON);
				Main.api.disguise(p, mob);
				p.teleport(teamSkeletonLocation);
			}
			
		}
		Main.activeGames.add(game);
		Main.gamesConfig.set("games."+game+".step", "starting_game");
		
		try {
			Main.gamesConfig.save(Main.gamesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Countdown.CountdownStart(3, game, "starting_game");
	}
	public static void LauchGame(String game){
		Main.gamesConfig.set("games."+game+".step", "playing");
		
		try {
			Main.gamesConfig.save(Main.gamesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Countdown.CountdownStart(30, game, "playing");
	}

}

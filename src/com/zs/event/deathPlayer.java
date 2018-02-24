package com.zs.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.zs.main.Main;

public class deathPlayer implements Listener{
	@EventHandler
	public void playerTakeDamage(EntityDamageByEntityEvent e){
		
		Player p = Bukkit.getServer().getPlayer(e.getEntity().getName());
		Player damager = Bukkit.getServer().getPlayer(e.getDamager().getName());
		String game = Main.getPlayerGame(p);
		if(game != null && Main.gamesConfig.getString("games."+game+".step") != "playing"){
			e.setCancelled(true);
		} 
		else if(game != null && Main.gamesConfig.getString("games."+game+".step") == "playing"){
			if(p.getHealth()-e.getDamage() < 0){
				if(e.getCause() == DamageCause.PROJECTILE) {
				    Arrow a = (Arrow) e.getDamager();
				    if(a.getShooter() instanceof Player) {
				    	damager = (Player) a.getShooter();
				    }
				}
				Main.playerKill.put(damager, Main.playerKill.get(damager)+1);
				if(Main.getPlayerTeam(p) == "zombie"){
					Main.playersTeamZombie.get(game).remove(p);
				} else if(Main.getPlayerTeam(p) == "skeleton"){
					Main.playersTeamSkeleton.get(game).remove(p);
				}
				p.setGameMode(GameMode.SPECTATOR);
				e.setCancelled(true);
			}
		}
	}
}

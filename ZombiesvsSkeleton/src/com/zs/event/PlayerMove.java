package com.zs.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.zs.main.Main;

public class PlayerMove implements Listener{
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		String game = Main.getPlayerGame(p);
		
		if(game != null && Main.gamesConfig.getString("games."+game+".step") == "starting_game"){
			e.setCancelled(true);
		}
	}
}

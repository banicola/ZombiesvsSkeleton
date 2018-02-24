package com.zs.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.zs.main.Main;

public class foodLevelEvent implements Listener{
	@EventHandler
	public void foodLevelChange(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if(Main.inGamePlayers.contains(p)){
			e.setCancelled(true);
		}
	}
}

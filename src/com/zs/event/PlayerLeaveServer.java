package com.zs.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zs.game.KickPlayerGame;
import com.zs.main.Main;

public class PlayerLeaveServer implements Listener{

	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent e) {
		if(Main.inGamePlayers.contains(e.getPlayer())){
			KickPlayerGame.kickPlayer(e.getPlayer(), Main.getPlayerGame(e.getPlayer()), "player");
		}
		return;
    }

}

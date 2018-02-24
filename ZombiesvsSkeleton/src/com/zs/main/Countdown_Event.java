package com.zs.main;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class Countdown_Event extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	
	public Countdown_Event()
	{
		
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlersList() {
		return handlers;
	}

}

package com.zs.main;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class EditConfigFile extends JavaPlugin{
	
	public static void editConfigFile(Player sender,String edit, String value){
		
		if (edit.equalsIgnoreCase("minPlayer")){
			int value2 = Integer.parseInt(value);
			if (value2 >= 0){
				if (value2 <= Main.config.getInt("games.maxPlayer")){
					Main.config.set("games.minPlayer", value2);
					sender.sendMessage(ChatColor.GREEN+"Succesfully changed minPlayer to "+value2);
				} else {
					sender.sendMessage(ChatColor.RED+"minPlayer cannot be bigger than maxPlayer ("+Main.config.getInt("games.maxPlayer")+")");
				}
			} else {
				sender.sendMessage(ChatColor.RED+"minPlayer cannot be negative!");
			}
			
			
		}
		else if (edit.equalsIgnoreCase("maxPlayer")){
			int value2 = Integer.parseInt(value);
			if (value2 >= 0){
				if (value2 >= Main.config.getInt("games.minPlayer")){
					Main.config.set("games.maxPlayer", value2);
					sender.sendMessage(ChatColor.GREEN+"Succesfully changed maxPlayer to "+value2);
				} else {
					sender.sendMessage(ChatColor.RED+"maxPlayer cannot be lower than minPlayer ("+Main.config.getInt("games.minPlayer")+")");
				}
			} else {
				sender.sendMessage(ChatColor.RED+"maxPlayer cannot be negative!");
			}
		}
		else if (edit.equalsIgnoreCase("start_timer")){
			int value2 = Integer.parseInt(value);
			if (value2 >= 0){
				Main.config.set("games.start_timer", value2);
				sender.sendMessage(ChatColor.GREEN+"Succesfully changed start_timer to "+value2);
			} else {
				sender.sendMessage(ChatColor.RED+"start_timer cannot be negative!");
			}
		}
		else{
			sender.sendMessage(ChatColor.RED+edit+" is not defined into config!");
		}
		
		try {
			Main.config.save(Main.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}

}

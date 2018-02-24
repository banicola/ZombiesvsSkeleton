package com.zs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zs.main.Main;
import com.zs.main.RemoveGame;

import net.md_5.bungee.api.ChatColor;

public class NoCommand implements CommandExecutor{

	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
    		if (args.length == 0){
    			if(Main.waitingConfirmation.containsValue(sender)){
    				Boolean removegame = RemoveGame.removeAvailableList((Player) sender, null);
    				
    				if (removegame){
    					sender.sendMessage(ChatColor.BLUE+"The game remove as been canceled!");
    				}
    				return true;
    			}
    		}
		}
		return false;
	}
}


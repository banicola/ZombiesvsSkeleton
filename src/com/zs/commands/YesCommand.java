package com.zs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zs.game.MaintenanceGame;
import com.zs.main.Main;
import com.zs.main.RemoveGame;

import net.md_5.bungee.api.ChatColor;

public class YesCommand implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
    		if (args.length == 0){
    			if(Main.waitingConfirmation.containsKey(sender)){
    				Main.waitingConfirmation.put((Player) sender, true);
    				
    				if (Main.waitingAction.get(sender) == "remove"){
    					Boolean removegame = RemoveGame.removeAvailableList((Player) sender, null);
        				
        				if (removegame){
        					sender.sendMessage(ChatColor.BLUE+"The game " + ChatColor.GREEN + Main.gameAction.get(sender) + ChatColor.BLUE + " has been removed!");
        					Main.gameAction.remove(sender);
        				}
        				return true;
    				}
    				else if (Main.waitingAction.get(sender) == "maintenance"){
    					MaintenanceGame.maintenanceGame((Player) sender, Main.gameAction.get(sender));
    					return true;
    				}
    				
    			}
    		}
		}
		return false;
	}
}

package com.zs.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zs.game.KickPlayerGame;
import com.zs.game.MaintenanceGame;
import com.zs.game.PlayerJoinGame;
import com.zs.main.CreateGame;
import com.zs.main.EditConfigFile;
import com.zs.main.Main;
import com.zs.main.RemoveGame;

import net.md_5.bungee.api.ChatColor;

public class ZombiesvsSkeletonCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (args.length > 0){
			
    		if (args[0].equalsIgnoreCase("join")){
    			if (!Main.waitingPlayers.contains(sender) && !Main.inGamePlayers.contains(sender)){
    				PlayerJoinGame.addPlayerList((Player) sender);
    			}
    			else{
    				sender.sendMessage(ChatColor.RED+"You are already in the queue!");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("leave")){
    			if (Main.waitingPlayers.contains(sender) || Main.inGamePlayers.contains(sender)){
    				KickPlayerGame.kickPlayer((Player) sender, Main.getPlayerGame((Player) sender), "player");
    			}
    			else{
    				sender.sendMessage(ChatColor.RED+"You are not in a game!");
    			}
    			
    		}
    		
    		else if (args[0].equalsIgnoreCase("list")){
    			if (Main.allGames.size() != 0){
    				for(String game : Main.allGames){
    					ChatColor color = ChatColor.RED;
    					if (Main.gamesConfig.getBoolean("games."+game+".active")){
    						color = ChatColor.GREEN;
    					}
        				sender.sendMessage(game.toString() + " : " + color + Main.listPlayerGame.get(game).size() +"/"+ Main.gamesConfig.getInt("games."+game+".maxPlayer"));
        			}
    			}
    			else{
    				sender.sendMessage(ChatColor.RED + "This game is currently unavailable!");
    			}
    			
    		}
    		
    		else if (args[0].equalsIgnoreCase("info")){
    			if (!(args.length == 1)){
    				String listPlayer = "";
    				if (Main.allGames.contains(args[1])){
    					for (Player p : Main.listPlayerGame.get(args[1])){
    						if (listPlayer.length() == 0){
    							listPlayer = p.getName();
    						}
    						else{
    							listPlayer = listPlayer.replace(listPlayer, listPlayer+", "+p.getName());
    						}
    					}
    					sender.sendMessage(ChatColor.BLUE+"======== Informations "+ChatColor.GREEN+args[1]+ChatColor.BLUE+" ========");
    					String status = ChatColor.RED+"Offline";
    					String step = Main.gamesConfig.getString("games."+args[1]+".step");
    					if (Main.gamesConfig.getBoolean("games."+args[1]+".active")){
    						status = status.replace(status, ChatColor.GREEN+"Online");
    					}
    					sender.sendMessage("status : "+status);
    					sender.sendMessage("step : "+step);
    					sender.sendMessage("minPlayer : " + Main.gamesConfig.getInt("games."+args[1]+".minPlayer"));
    					sender.sendMessage("maxPlayer : " + Main.gamesConfig.getInt("games."+args[1]+".maxPlayer"));
    					if (listPlayer.length() != 0){
    						sender.sendMessage("Players : "+ ChatColor.GREEN +listPlayer);
    					}
    					else{
    						sender.sendMessage("Players :"+ChatColor.GREEN+" []" );
    					}
    				}
    				else{
    					sender.sendMessage(ChatColor.RED + "The game " + ChatColor.WHITE + args[1] + ChatColor.RED + " does not exist!");
    				}
    			}
    			else{
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton info [game]");
    			}
				
			}
    		
    		else if (args[0].equalsIgnoreCase("create") && sender.isOp()){
    			if (args.length > 1){
    				int minPlayer = -1;
    				int maxPlayer = -1;
    				if (args.length == 4){
    					minPlayer = Integer.parseInt(args[2]);
        				maxPlayer = Integer.parseInt(args[3]);
    				}
    				
    				Boolean creategame = CreateGame.addAllList(args[1], minPlayer, maxPlayer);
    				if (creategame){
    					sender.sendMessage(ChatColor.BLUE+"The game " + ChatColor.GREEN +args[1]+ ChatColor.BLUE + " has been created!");
    				}
    				else{
    					sender.sendMessage(ChatColor.RED+"An error appears. The game "+ChatColor.WHITE+args[1]+ChatColor.RED+" could already exist!");
    				}
    			}
    			else{
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton create [game] [minPlayer] [maxPlayer]");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("remove") && sender.isOp()){
    			if (args.length == 2){
    				RemoveGame.removeAvailableList((Player) sender, args[1]);
    			}
    			else if (args.length == 1){
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton remove [game]");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("maint") && sender.isOp()){
    			if (args.length == 2){
    				MaintenanceGame.maintenanceGame((Player) sender, args[1]);
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton maint [game]");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("kick") && sender.isOp()){
    			if (args.length == 2){
    				Player player = Bukkit.getServer().getPlayer(args[1]);
    				String playerGame = Main.getPlayerGame(player);
    				if (playerGame != null){
    					Boolean kickPlayer = KickPlayerGame.kickPlayer(player, playerGame, "admin");
        				
        				if (kickPlayer){
        					sender.sendMessage(ChatColor.BLUE+"You succesfully kicked "+ChatColor.GREEN+args[1]+ChatColor.BLUE+" from "+ChatColor.GREEN+playerGame);
        				}
        				else{
        					sender.sendMessage(ChatColor.RED+"Error");
        				}
    				} else {
    					sender.sendMessage(ChatColor.RED+player.getName()+" is not in a game!");
    				}
    				
    			}
    			else{
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton kick [player]");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("kickall") && sender.isOp()){
    			if (args.length == 2){
    				if (Main.allGames.contains(args[1])){
    					List<Player> playerTempList = new ArrayList<Player>();
    					for (Player p : Main.listPlayerGame.get(args[1])){
    						playerTempList.add(p);
    					}
    					for (Player player : playerTempList){
        					KickPlayerGame.kickPlayer(player, args[1], "admin");
        				}
        				sender.sendMessage(ChatColor.GREEN+"All the players have been kicked from " + args[1]);
    				}
    				else{
    					sender.sendMessage(ChatColor.RED+"This game does not exist!");
    				}
    				
    			}
    			else{
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton kickall [game]");
    			}
    		}
    		
    		else if (args[0].equalsIgnoreCase("reload") && sender.isOp()){
    			Main.loadFiles();
    			Bukkit.getServer().getPluginManager().getPlugin("ZombiesvsSkeleton").reloadConfig();
    			
    			sender.sendMessage(ChatColor.GREEN+"The config file has been reloaded!");
    		}
    		
    		else if (args[0].equalsIgnoreCase("editconfig") && sender.isOp()){
    			if (args.length == 3){
    				EditConfigFile.editConfigFile((Player)sender, args[1], args[2]);
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /zombiesvsskeleton editconfig [edit] [value]");
    			}
    		}
    		
    		else{
    			return false;
    		}
    	}
    	else{
    		sender.sendMessage(ChatColor.BLUE + "=========== Zombies VS Skeleton ===========");
    		sender.sendMessage(ChatColor.GREEN + "/zs join : Join a game\n/zs leave : Leave a game\n/zs list : List of games\n/zs info [game] : Info on the game");
    		if (sender.isOp()){
    			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/zs create [game] [minPlayer] [maxPlayer] : Create a game (admin)\n/zs remove [game] : Remove a game (admin)\n/zs kick [player] : Kick a player from a game (admin)\n/zs kickall [game] : Kick all the players in a game (admin)\n/zs editconfig [edit] [value] : Edit the config file (admin)\n/zs maint [game] : Remove game from availablelist (admin)\n/zs reload : Reload the files (admin)");
    		}
    		sender.sendMessage(ChatColor.BLUE + "==================================");
    	}
    	
	return true;
   } 
}

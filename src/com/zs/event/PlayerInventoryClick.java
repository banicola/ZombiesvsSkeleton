package com.zs.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;

import com.zs.main.KitMenu;
import com.zs.main.Main;

import net.md_5.bungee.api.ChatColor;

public class PlayerInventoryClick implements Listener{

	@EventHandler
	public void playerInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		String game = Main.getPlayerGame(p);
		
		if (game != null && !Main.activeGames.contains(game)) {
			event.setCancelled(true);
			
  		  	if(event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)){
  	  		  	if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Team Zombie")){
  	  		  		if(!Main.playersTeamZombie.get(game).contains(p) && Main.playersTeamZombie.get(game).size() < Main.gamesConfig.getInt("games."+game+".maxPlayer")/2){
  	  		  			Main.playersTeamZombie.get(game).add(p);
  	  		  			p.sendMessage(ChatColor.BLUE+"You joined the team "+ChatColor.GREEN+"Zombie");
  	  		  			if(Main.playersTeamSkeleton.get(game).contains(p)){
  	  		  				Main.playersTeamSkeleton.get(game).remove(p);
	  	  		  			if(Main.playerKit.containsKey(p)){
		  		  				Main.playerKit.remove(p);
		  		  			}
  	  		  			}
  	  		  			SkullMeta meta1 = (SkullMeta) event.getCurrentItem().getItemMeta();
		  		  		List<String> lore1 = Arrays.asList("Join Team Zombie", Main.playersTeamZombie.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
  	  		  			meta1.setLore(lore1);
						event.getCurrentItem().setItemMeta(meta1);
  	  		  		} else {
  	  		  			//p.sendMessage("You are already in Team Zombie!");  
  	  		  		}
  	  		  	} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Team Skeleton")){
  	  		  		if(!Main.playersTeamSkeleton.get(game).contains(p) && Main.playersTeamSkeleton.get(game).size() < Main.gamesConfig.getInt("games."+game+".maxPlayer")/2){
  	  		  			Main.playersTeamSkeleton.get(game).add(p);
  	  		  			p.sendMessage(ChatColor.BLUE+"You joined the team "+ChatColor.GREEN+"Skeleton");
  	  		  			if(Main.playersTeamZombie.get(game).contains(p)){
  	  		  				Main.playersTeamZombie.get(game).remove(p);
  	  		  				if(Main.playerKit.containsKey(p)){
  	  		  					Main.playerKit.remove(p);
  	  		  				}
  	  		  			}
  	  		  			SkullMeta meta2 = (SkullMeta) event.getCurrentItem().getItemMeta();
		  		  		List<String> lore2 = Arrays.asList("Join team Skeleton", Main.playersTeamSkeleton.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
		  		  		meta2.setLore(lore2);
						event.getCurrentItem().setItemMeta(meta2);
  	  		  		} else {
  	  		  			//p.sendMessage("You are already in team Skeleton!");  
  	  		  		}
  	  		  	} else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Kit")){
  	  		  		KitMenu.kitMenuDisplay(p);  	  		  		
  	  		  	} else if(event.getInventory().getName() == "Kit Menu"){
  	  		  		if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Main.config.getString("games.kits.zombie.kit1.name")) || event.getCurrentItem().getItemMeta().getDisplayName().equals(Main.config.getString("games.kits.skeleton.kit1.name"))){
  	  		  			KitMenu.chooseKitPlayer(p, "kit1");
  	  		  		}
  	  		  	}
  		  	}
		}
	}
			
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
	  Player p = event.getPlayer();
	  Action action = event.getAction();
	  
	  if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){
		  String game = Main.getPlayerGame(p);
			
	      if (game != null && !Main.activeGames.contains(game)) {
	    	  event.setCancelled(true);
	    	  
	    	  if(p.getItemInHand().getType().equals(Material.SKULL_ITEM) || p.getItemInHand().getType().equals(Material.ANVIL)){
	    		  if(!Main.playersTeamZombie.containsKey(game)){
	    			  Main.playersTeamZombie.put(game, new ArrayList<Player>());
	    		  }
	    		  if(!Main.playersTeamSkeleton.containsKey(game)){
	    			  Main.playersTeamSkeleton.put(game, new ArrayList<Player>());
	    		  }
    			  
	    		  if(p.getInventory().getHeldItemSlot() == 0){
	    			  if(!Main.playersTeamZombie.get(game).contains(p) && Main.playersTeamZombie.get(game).size() < Main.gamesConfig.getInt("games."+game+".maxPlayer")/2){
	    				  Main.playersTeamZombie.get(game).add(p);
	    				  p.sendMessage(ChatColor.BLUE+"You joined the team "+ChatColor.GREEN+"Zombie");
	    				  if(Main.playersTeamSkeleton.get(game).contains(p)){
	    					  Main.playersTeamSkeleton.get(game).remove(p);
	    					  if(Main.playerKit.containsKey(p)){
	    						 Main.playerKit.remove(p);
	  	  		  			  }
	    				  }
	    				  SkullMeta meta1 = (SkullMeta) p.getInventory().getItemInHand().getItemMeta();
		  		  		  List<String> lore1 = Arrays.asList("Join team Zombie", Main.playersTeamZombie.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
  	  		  			  meta1.setLore(lore1);
  	  		  			  p.getInventory().getItemInHand().setItemMeta(meta1);
	    			  } else {
    					  //p.sendMessage("You are already in Team Zombie!");  
    				  }
	    		  } else if(p.getInventory().getHeldItemSlot() == 8){
	    			  if(!Main.playersTeamSkeleton.get(game).contains(p) && Main.playersTeamSkeleton.get(game).size() < Main.gamesConfig.getInt("games."+game+".maxPlayer")/2){
	    				  Main.playersTeamSkeleton.get(game).add(p);
	    				  p.sendMessage(ChatColor.BLUE+"You joined the team "+ChatColor.GREEN+"Skeleton");
	    				  if(Main.playersTeamZombie.get(game).contains(p)){
	    					  Main.playersTeamZombie.get(game).remove(p);
	    					  if(Main.playerKit.containsKey(p)){
		    					 Main.playerKit.remove(p);
		  	  		  		  }
	    				  }
	    				  SkullMeta meta2 = (SkullMeta) p.getInventory().getItemInHand().getItemMeta();
		  		  		  List<String> lore2 = Arrays.asList("Join team Skeleton", Main.playersTeamSkeleton.get(game).size()+"/"+Main.gamesConfig.getInt("games."+game+".maxPlayer")/2, "Available");
  	  		  			  meta2.setLore(lore2);
  	  		  			  p.getInventory().getItemInHand().setItemMeta(meta2);
	    			  } else {
    					  //p.sendMessage("You are already in team Skeleton!");  
    				  }
	    		  } else if(p.getInventory().getHeldItemSlot() == 4) {
	    			  KitMenu.kitMenuDisplay(p);
	    		  } else {
	    			  p.sendMessage(ChatColor.RED+"Error. Please contact an administrator!");
	    		  }
	    	  }
	      }
	   }
	}
}

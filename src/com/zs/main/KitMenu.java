package com.zs.main;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class KitMenu extends JavaPlugin{
	
	private static Inventory kitMenu = Bukkit.createInventory(null, 9, "Kit Menu");
	
	public static void kitMenuDisplay(Player p){
		
		ItemStack kit1 = new ItemStack((Material.AIR));
		if(Main.getPlayerTeam(p) == null){
			p.sendMessage(ChatColor.RED+"Join a team to choose your kit!");
			return;
		}
		else if(Main.getPlayerTeam(p).equals("zombie")){
			kit1 = new ItemStack(Material.DIAMOND_SWORD);
			ItemMeta meta1 = (ItemMeta) kit1.getItemMeta();
			meta1.setDisplayName(Main.config.getString("games.kits.zombie.kit1.name"));
			kit1.setItemMeta(meta1);
		}
		else if(Main.getPlayerTeam(p).equals("skeleton")){
			kit1 = new ItemStack(Material.BOW);
			ItemMeta meta1 = (ItemMeta) kit1.getItemMeta();
			meta1.setDisplayName(Main.config.getString("games.kits.skeleton.kit1.name"));
			kit1.setItemMeta(meta1);
		}
		
		kitMenu.setItem(0, kit1);
		p.openInventory(kitMenu);
	}
	
	public static void chooseKitPlayer(Player p, String kit){
		if(kit.contentEquals("kit1")){
			Main.playerKit.put(p, "kit1");
			String team = Main.getPlayerTeam(p);
			p.sendMessage(ChatColor.BLUE+"You took the kit "+ChatColor.GREEN+Main.config.getString("games.kits."+team+"."+kit+".name")+ChatColor.BLUE+"!");
		}
		p.closeInventory();
	}
	
	@SuppressWarnings("deprecation")
	public static void setKitPlayer(Player p){
		String kit = Main.playerKit.get(p);
		String team = Main.getPlayerTeam(p);
		
		if(kit == null){
			final String[] kitList = {"kit1"};
			Random random = new Random();
			int i = random.nextInt(kitList.length);
			kit = kitList[i];
			p.sendMessage(ChatColor.BLUE+"The kit "+ChatColor.GREEN+Main.config.getString("games.kits."+team+"."+kit+".name")+ChatColor.BLUE+" has been choosen randomly!");
		}
		List<String> items = Main.config.getStringList("games.kits."+team+"."+kit+".items");
		
		int n = 0;
		for(String i : items){
			String split[]= StringUtils.split(i);
			Integer itemID = Integer.parseInt(split[0]);
			Integer amount = Integer.parseInt(split[1]);
			p.getInventory().setItem(n, new ItemStack(Material.getMaterial(itemID), amount));
			n++;
		}
		
	}
}

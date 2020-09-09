package me.ninjego.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {

	public static String MEESSAGE;
	
	public static Main main;
	
	public boolean Captcha = false;
	
	public int KickCap = 0;
	
	public List<String> AllPlayer = new ArrayList<String>();
	
	public List<String> AllCap = new ArrayList<String>();
	
	public String TextF = "";
	
	public String[] Numbbers = {"a", "b", "c", "d", "e", "f", "g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z", "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = (Player) e.getPlayer();
		
		if(player.hasPermission("VaderCaptcha.bypass"))
			AllPlayer.add(player.getName());

		KickCap = 0;
		
		if(AllPlayer.contains(player.getName())) return;
		
		Captcha = false;
		
		TextF = "";
		
		for(int i = 0; i < 7; i++) {
			int rndm = new Random().nextInt(Numbbers.length);
			TextF = TextF + Numbbers[rndm];
		}
		
		AllCap.add("." + TextF);
		
		
		MEESSAGE = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("message")+ "." + TextF);
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable(){
			  @Override
			  public void run(){
				  player.sendMessage(MEESSAGE);
			  }	  
			}, 50L);
		
	}
	
	@EventHandler
	public void onCHATT(AsyncPlayerChatEvent e) {
		Player player = (Player) e.getPlayer();
		if(AllPlayer.contains(player.getName())) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
	    Player player = (Player) e.getPlayer();
	    
		if(AllPlayer.contains(player.getName())) return;
		if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
			player.teleport(e.getFrom());
		}
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent e) {	
        if (e.getEntity() instanceof Player){
        	if(AllPlayer.contains(e.getEntity().getName())) return;
            e.setCancelled(true);
        }
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent e) {
		Player player = (Player) e.getPlayer();
		
		AllPlayer.remove(player.getName());
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		Player player = (Player) e.getPlayer();
		
		if(AllPlayer.contains(player.getName())) return;
		e.setCancelled(true);
		
		String Mess = e.getMessage();
		
		
		if(AllCap.contains(Mess)) {
			AllPlayer.add(player.getName());
			AllCap.remove(e.getMessage());
			e.setCancelled(true);
			TextF = "";
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("correct-message")));
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("incorrect-message")));
			e.setCancelled(true);
			if(KickCap > 2)
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message")));
			else
				KickCap++;
		}
	}
	
	public static boolean inBArray(List<String> array, String check) {
		   for (String o : array){
		      if (o == check) {
		         return true;
		      }
		   }
		   return false;
		}
	
}

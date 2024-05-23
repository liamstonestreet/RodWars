package me.liam.rodwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    
    @EventHandler
    public void onJoin(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        
        // reset stuff
        p.setGlowing(false);
        p.setGravity(true);
    }
    
}

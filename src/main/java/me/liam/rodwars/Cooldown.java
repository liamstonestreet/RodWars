package me.liam.rodwars;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
    
    /**
     * This class can be used to add a cooldown for all interactible items,
     * for ex: using an item adds cooldown to all items.
     */
    
    @Getter private final HashMap<UUID, Long> cooldowns;
    
    public Cooldown() {
        cooldowns = new HashMap<>();
    }
    
    public void addCooldown(Player p, double secondsCooldown) {
        UUID uuid = p.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long millisCooldown = (long) (1000L * secondsCooldown);
        if (cooldowns.containsKey(uuid)) return;
        cooldowns.put(uuid, currentTime + millisCooldown);
    }
    
    public void removeCooldown(Player p) {
        if (!cooldowns.containsKey(p.getUniqueId())) {
            return;
        }
        cooldowns.remove(p.getUniqueId());
    }
    
    public boolean hasCooldown(Player p) {
        // TODO Remove this after testing is done!
        if (ChatColor.stripColor(p.getDisplayName()).equals("Kubokuu")) {
            return false;
        }
        
        if (cooldowns.containsKey(p.getUniqueId())) {
            long currentTime = System.currentTimeMillis();
            long cooldownDone = cooldowns.get(p.getUniqueId());
            
            // check if cooldown is over
            if (currentTime >= cooldownDone) {
                removeCooldown(p);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
}

package me.liam.rodwars.rods;

import me.liam.rodwars.Rod;
import me.liam.rodwars.particleEffects.teslaRod.TeslaOrb;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeslaRod extends Rod {
    
    public TeslaRod() {
        super(Material.BLAZE_ROD, "Tesla Rod", "&b<<&8&l Tesla Rod &b>>");
    }
    
    @Override
    protected void handleRightClickAir(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        
        // 1: handle cooldown
        if (!handleCooldown(p, 5)) {
            return;
        }
        
        // magnetosphere thing from terraria
        new TeslaOrb(p, 7, 5, getName()).start();
    }
}

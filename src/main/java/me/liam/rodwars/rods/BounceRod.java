package me.liam.rodwars.rods;

import me.liam.rodwars.Rod;
import me.liam.rodwars.particleEffects.bounceRod.SphereBubble;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class BounceRod extends Rod {
    
    public BounceRod() {
        super(Material.LIGHTNING_ROD, "Bounce Rod", "&b<<&3&l Bounce Rod &b>>");
    }
    
    @Override
    protected void handleRightClickAir(PlayerInteractEvent e) {
        // DO STUFF
        double velocity = 2;
        // the particle effect!
        new SphereBubble(e.getPlayer(), velocity, 4, 10, "Bounce Rod").start();
    }
    
    @Override
    protected void handleRightClickBlock(PlayerInteractEvent e) {
        // TODO fix this calling the effect twice when block is clicked
        handleRightClickAir(e);
    }
}

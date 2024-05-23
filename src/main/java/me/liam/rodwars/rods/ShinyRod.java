package me.liam.rodwars.rods;

import me.liam.rodwars.Rod;
import me.liam.rodwars.RodWars;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


public class ShinyRod extends Rod {
    
    private final double cooldownDuration = 15;
    
    public ShinyRod() {
        super(Material.BLAZE_ROD, "Shiny Rod", "&b<< &2&lShiny Rod &b>>");
    }
    
    @Override
    protected void handleRightClickAir(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        Location start = getParticleUtil().getInfrontOf(p, 2);
    
        // COOLDOWN MANAGEMENT
        if (!handleCooldown(p, cooldownDuration)) return;
        
        // Start the effect!!!!
        // variables/constants
        long delay = 1;
        
        // Spawn the warden particle effect first
        //w.spawnParticle(Particle.SONIC_BOOM, start, 0);
    
        // playing initial sounds
        w.playSound(start, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1.5f); // charge up sound
        
        // RUNS AFTER 10 TICK DELAY (gives time for sonic boom particle)
        Runnable r = () -> {
            start.setDirection(p.getEyeLocation().getDirection());
            // spawning first particles
            w.spawnParticle(Particle.FLASH, start, 0);
            
            // playing first sounds
            w.playSound(start, Sound.ENTITY_WARDEN_SONIC_BOOM, .5f, 0.8f);
            w.playSound(start, Sound.ENTITY_WARDEN_ROAR, .5f, 1f);
            
            // spawning particle circles + hitting entities
            int distance = 150;
            double radius = .5;
            for (int i = 1; i <= distance; i++) {
                // spawn particles
                w.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, start, 0);
                getParticleUtil().spawnRotatedCircle(radius, start, start, Particle.REDSTONE,
                        40, new Particle.DustOptions(Color.SILVER, 0.2f));
                w.spawnParticle(Particle.REDSTONE, start, 0, new Particle.DustOptions(Color.RED, 3f));
    
                // if non-air block is hit, stop
                if (!w.getBlockAt(start).getType().isAir()) {
                    break;
                }
                
                // do things to hit entity!
                for (Entity en : w.getNearbyEntities(start, 1, 1, 1)) {
                    if (en.getUniqueId().equals(p.getUniqueId()) || !(en instanceof LivingEntity le)) {
                        continue;
                    }
                    applyShinyDamage(le, p);
                }
                
                // update vars
                start.add(start.getDirection().normalize()); // moves loc 1 block forward.
                //radius += 0.025;
            }
        };
        Bukkit.getScheduler().runTaskLater(RodWars.getInstance(), r, delay);
    }
    
    /**
     * This method handles what happens to hit entities.
     */
    public void applyShinyDamage(LivingEntity le, Player invoker) {
        double damage = (le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()
                + le.getAttribute(Attribute.GENERIC_ARMOR).getValue()) / 2;
        le.damage(damage, invoker);
        le.setAI(false);
        le.setFireTicks(60);
        le.setGlowing(true);
        le.setGravity(false);
        le.setArrowsInBody(7);
        // make it go up
        le.setVelocity(new Vector(0, 0.2,0));
        Bukkit.getScheduler().scheduleSyncDelayedTask(RodWars.getInstance(),
                () -> {
                    if (!le.isDead()) {
                        le.setAI(true);
                        le.setGravity(true);
                        le.setGlowing(false);
                        le.setArrowsInBody(0);
                    }
                }, 20L * 3);
        invoker.playSound(invoker.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.5f);
    }
    
}

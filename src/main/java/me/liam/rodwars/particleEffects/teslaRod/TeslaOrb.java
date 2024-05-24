package me.liam.rodwars.particleEffects.teslaRod;

import me.liam.rodwars.RodParticleEffectTask;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.Locale;

/**
 * This class runs the effect for the Tesla Rod! A slow-moving orb that zaps nearby enemies!
 *
 */
public class TeslaOrb extends RodParticleEffectTask {
    
    private Location current;
    private World w;
    private double speed = 0.2;
    
    public TeslaOrb(Player invoker, double effectDuration, double cooldownDuration, String rodName) {
        super(invoker, effectDuration, cooldownDuration, rodName);
    }
    
    
    
    @Override
    protected void loop() {
        if (seconds == 0) {
            // INITIAL
            // initialize start location
            current = getParticleUtil().getInfrontOf(invoker, 2);
            w = current.getWorld();
            // sounds effects (initial)
            w.playSound(current, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 0.4f);
            w.playSound(current, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.7f, 1.2f);
        }
        
        // sphere particle effect
        getParticleUtil().spawnSphere(current, 1.5, 15, Particle.DUST_COLOR_TRANSITION,
                new Particle.DustTransition(Color.SILVER, Color.TEAL, .75f));
        getParticleUtil().spawnParticle(Particle.REDSTONE, current, 0,
                new Particle.DustOptions(Color.SILVER, 1));
        
        // sound effects (continuous)
        
        
        // zap nearby entities (every 0.5 seconds)
        if (seconds % 0.5 <= 0.1) {
            for (Entity en : w.getNearbyEntities(current, 10, 10, 10)) {
                if (!(en instanceof LivingEntity le)) continue;
                if (!le.getUniqueId().equals(invoker.getUniqueId())) {
                    // zap!
                    getParticleUtil().spawnParticleLine(current, le.getEyeLocation(), Particle.CRIT, 2);
                    getParticleUtil().spawnParticle(Particle.FLASH, le.getLocation(), 0);
                    // sounds
                    w.playSound(le.getLocation(), Sound.ENTITY_CAT_HISS, .5f, 0.3f);
                    w.playSound(le.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 2f);
                    // damage the entity
                    le.damage(5, invoker);
                }
            }
        }
        
        // IMPORTANT: propagate current location forward!
        current.add(current.getDirection().normalize().multiply(speed));
    }
    
    @Override
    protected void terminate() {
    
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}

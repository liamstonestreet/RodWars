package me.liam.rodwars.particleEffects.bounceRod;

import me.liam.rodwars.RodParticleEffectTask;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class SphereBubble extends RodParticleEffectTask {
    
    private final Snowball source;
    private final double velocity;
    
    public SphereBubble(Player invoker, double velocity, double effectDuration, double cooldownDuration, String rodName) {
        super(invoker, effectDuration, cooldownDuration, rodName);
        this.velocity = velocity;
        this.source = setup();
    }
    
    protected Snowball setup() {
        // Spawn Entity
        Location spawnLocation = invoker.getEyeLocation().add(invoker.getEyeLocation().getDirection()
                .normalize().multiply(2));
        Snowball source = (Snowball) getParticleUtil().spawnEntity(spawnLocation, EntityType.SNOWBALL);
        source.setVisibleByDefault(false);
        source.setSilent(true);
        source.setGravity(false);
        source.setVelocity(invoker.getLocation().getDirection().clone().normalize().multiply(velocity));
        
        // Cool Particles
        spawnLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, spawnLocation, 0);
        return source;
    }
    
    @Override
    protected void loop() {
        //getParticleUtil().spawnSphere(source.getLocation(), 3, 30, Particle.WATER_BUBBLE);
        getParticleUtil().spawnSphere(source.getLocation(), 3, 40, Particle.REDSTONE,
                new Particle.DustOptions(Color.BLUE, .5f));
        source.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, source.getLocation(), 0);
        source.getNearbyEntities(3, 3, 3).forEach(e -> {
            if (e.getUniqueId().equals(invoker.getUniqueId()) && (source.getVelocity().length() > 0.001)) {
                // do nothing to the player who shot the bubble if the bubble is still travelling.
                return;
            }
            e.setVelocity(new Vector(0, 1, 0));
        });
    }
    
    @Override
    protected void terminate() {
        source.remove();
    }
    
    @Override
    public boolean isFinished() {
        return !invoker.isOnline();
    }
    
}

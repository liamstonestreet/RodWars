package me.liam.rodwars.particleEffects.seekingRod;

import lombok.Setter;
import me.liam.rodwars.ParticleEffectTask;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

public class SeekingProjectile extends ParticleEffectTask {
    
    private final WitherSkull proj;
    private final LivingEntity target;
    @Setter private double speed = 0.1f;

    
    public SeekingProjectile(double effectDuration, WitherSkull proj, LivingEntity target) {
        super(effectDuration);
        this.proj = proj;
        this.target = target;
    }
    
    @Override
    protected void loop() {
        if (seconds >= 5) {
            proj.remove();
            cancel();
            return;
        }
        if (target != null && !target.isDead()) {
            // The location of the target's eye (if applicable)
            Location towards = target.getEyeLocation();
            Vector newVelocity = getParticleUtil().getTowards(proj.getLocation(), towards).normalize().multiply(speed);
            proj.setVelocity(newVelocity);
            
            // increment speed
            double maxSpeed = 10f;
            if (speed < maxSpeed) {
                speed *= 1.1;
            }
        }
    }
    
    @Override
    protected void terminate() {
        proj.getLocation().getWorld().spawnParticle(Particle.FLASH, proj.getLocation(), 0);
        proj.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, proj.getLocation(), 1);
    }
    
    @Override
    public boolean isFinished() {
        return proj.isDead() || target != null && target.isDead();
    }
}

package me.liam.rodwars.particleEffects;

import lombok.Setter;
import me.liam.rodwars.ParticleEffectTask;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static java.lang.Math.PI;

/**
 * This class takes an already-existing entity and adds a helix projectile to it.
 *
 * ***/


public class RotatingHelixAboutEntity extends ParticleEffectTask {
    
    private final Player invoker;
    private final Entity proj;
    @Setter private Particle particle = Particle.FLAME;
    @Setter private Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1f);
    @Setter private int numParticles = 2;
    @Setter private double thetaIncrement;
    private double radius = 1.5; // TODO add this and maybe other variables to constructor, so these values can be customizable.
    private double theta = 0;
    
    public RotatingHelixAboutEntity(Player invoker, double effectDuration, double thetaIncrement, Entity proj) {
        super(effectDuration);
        this.invoker = invoker;
        this.proj = proj;
        this.thetaIncrement = thetaIncrement;
    }
    
    @Override
    protected void loop() {
        getParticleUtil().spawnHelixAboutProjectile(theta, radius, proj, particle, dustOptions, numParticles, true);
        //getParticleUtil().spawnRotatedCircle(2, proj.getLocation(), proj.getLocation(), particle, (int) (8 * radius));
        radius += 0.005;
        theta += PI / 8;
    }
    
    @Override
    protected void terminate() {
    
    }
    
    @Override
    public boolean isFinished() {
        return proj.isDead() || proj.isOnGround();
    }
    
    
}

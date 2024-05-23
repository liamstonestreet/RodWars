package me.liam.rodwars;

import lombok.Getter;
import me.liam.rodwars.utils.ParticleUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class is used to create particle effects that have a bounding box.
 * This allows particle effects to effectively 'hit' other things.
 * We use invisible entities to manage movement and bounding boxes of the particle effects.
 *
 * The way it works now is: particle effects have cooldowns, not rods.
 * - Rods create particle effects which have cooldowns.
 * - If a particle effect is spawned without a rod name specified in the constructor, there is no cooldown.
 *
 */

public abstract class ParticleEffectTask extends BukkitRunnable {
    
    
    @Getter protected final ParticleUtil particleUtil; // particle util!!!
    @Getter protected double seconds; // seconds passed since start of the particle effect.
    @Getter protected final double effectDuration; // duration of the effect in seconds.
    @Getter protected final long delay; // delay (in ticks) from starting the effect.
    @Getter protected final long period; // number of times the effect loops per second.
    
    public ParticleEffectTask(double effectDuration) {
        this(effectDuration, 0, 20);
    }
    
    public ParticleEffectTask(double effectDuration, long delay, long frequency) {
        this.seconds = 0;
        this.effectDuration = effectDuration;
        this.delay = delay;
        this.period = 20 / frequency;
        this.particleUtil = new ParticleUtil();
    }
    
    /**
     * The code that will continue to run every.
     */
    protected abstract void loop();
    /**
     * This method handles how to end the task. EX: removing entities, removing things from lists, etc.
     */
    protected abstract void terminate();
    /**
     * The method that determines whether the effect is finished or not. Often times will be based on duration.
     */
    public abstract boolean isFinished();
    
    
    @Override
    public void run() {
        if (isFinished() || seconds > effectDuration) {
            cancel();
            return;
        }
        try {
            loop();
        } catch (Exception e) {
            // handle? or no need
        }
        seconds += 1.0 / 20;
    }
    
    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        terminate();
    }
    
    public void start() {
        // Initialize the source entity variable and spawn/edit the source entity.
        runTaskTimer(RodWars.getInstance(), delay, period);
    }
    
}


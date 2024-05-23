package me.liam.rodwars;

import lombok.Getter;
import me.liam.rodwars.utils.ChatUtil;
import org.bukkit.entity.Player;

public abstract class RodParticleEffectTask extends ParticleEffectTask {
    
    @Getter protected final Player invoker; // the one who invokes the effect.
    @Getter protected final Rod usedRod; // the rod that the player used.
    @Getter protected final double cooldownDuration; // duration of the cooldown.
    
    public RodParticleEffectTask(Player invoker, double effectDuration, double cooldownDuration, String rodName) {
        super(effectDuration);
        this.invoker = invoker;
        this.usedRod = Rod.getRod(rodName);
        this.cooldownDuration = cooldownDuration;
    }
    
    @Override
    public void start() {
        // Check cooldown
        if (usedRod != null) {
            if (usedRod.getCooldown().hasCooldown(invoker)) {
                invoker.sendMessage(ChatUtil.colorize("&cYou are on cooldown!"));
                return;
            }
            usedRod.getCooldown().addCooldown(invoker, cooldownDuration); // add cooldown
            System.out.println("Poo");
        }
        
        // start!
        super.start();
    }
}

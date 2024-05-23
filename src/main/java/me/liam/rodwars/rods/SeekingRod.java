package me.liam.rodwars.rods;

import me.liam.rodwars.Rod;
import me.liam.rodwars.RodItem;
import me.liam.rodwars.particleEffects.RotatingHelixAboutEntity;
import me.liam.rodwars.particleEffects.seekingRod.SeekingProjectile;
import me.liam.rodwars.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.Set;

import static java.lang.Math.PI;

public class SeekingRod extends Rod {
    
    private LivingEntity target; // must be marked by melee-ing them!!!
    
    public SeekingRod() {
        super(Material.BLAZE_ROD, "Seeking Rod", "&b<<&5&l Seeking Rod &b>>");
    }
    
    // Resetting mark
    @Override
    protected void handleShiftRightClickAir(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (target == null) {
            ChatUtil.sendColor(p, "&cYou haven't marked anything.");
        } else {
            target = null;
            ChatUtil.sendColor(p, "&3&lSuccessfully reset your seeking mark!");
        }
    }
    
    // Resetting mark
    @Override
    protected void handleShiftRightClickBlock(PlayerInteractEvent e) {
        // might call duplicate event.
        handleShiftRightClickAir(e);
    }
    
    // Shooting the wither skull!
    @Override
    protected void handleRightClickAir(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        // 0.5: check if target is dead.
        if (target != null && target.isDead()) {
            target = null;
        }
    
        // COOLDOWN MANAGEMENT
        if (getCooldown().hasCooldown(p)) {
            ChatUtil.sendColor(p, "&cOn cooldown!");
            return;
        }
        // add cooldown!!!
        getCooldown().addCooldown(p, 8);
        
        // 1: spawn an invisible snowball entity in front of player
        Location loc = getParticleUtil().getInfrontOf(p, 2);
        WitherSkull dummy = (WitherSkull) getParticleUtil().spawnEntity(loc, EntityType.WITHER_SKULL);
        dummy.setShooter(p);
        dummy.setGravity(false);
        dummy.setIsIncendiary(true);
        dummy.setYield(100f);
    
        // 2: play initial sounds
        w.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 0.5f);
        w.playSound(loc, Sound.BLOCK_CHAIN_BREAK, 0.6f, 1);
        w.playSound(loc, Sound.BLOCK_BELL_RESONATE, 0.6f, 0.7f);
        
        // 3: create a rotating helix effect
//        RotatingHelixAboutEntity helix = new RotatingHelixAboutEntity(p, 5, PI / 16, dummy);
//        helix.setParticle(Particle.REDSTONE);
//        helix.setDustOptions(new Particle.DustOptions(Color.SILVER, 2));
//        helix.setNumParticles(2);
//        helix.start();
    
        // 4: continuously update trajectory of projectile (to follow nearest target)
        SeekingProjectile seek = new SeekingProjectile(5, dummy, target);
        seek.start();
    }
    
    @Override
    protected Set<Listener> getListeners() {
        Listener l = new Listener() {
            // Marking Entities
            @EventHandler
            public void onLeftClick(EntityDamageByEntityEvent e) {
                // only for players
                //if (!(e.getDamager() instanceof Player hunter) || !(e.getEntity() instanceof Player victim)) return;
                if (!(e.getDamager() instanceof Player hunter) || !(e.getEntity() instanceof LivingEntity victim)) return;
                ItemStack item = hunter.getInventory().getItemInMainHand();
                if (!RodItem.isMatchingRod(item, "Seeking Rod")) return;
                
                if (target == null || !target.getUniqueId().equals(victim.getUniqueId())) {
                    target = victim;
                    ChatUtil.sendColor(hunter, "&6&lSuccessfully marked &4&l'" + victim.getName() + "'");
                    ChatUtil.sendColor(victim, "&c&lYou have been marked!");
                }
            }
        };
        return Set.of(l);
    }
    
    /** ////////// PRIVATE METHODS /////////// */
    
}

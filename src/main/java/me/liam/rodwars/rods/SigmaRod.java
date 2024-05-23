package me.liam.rodwars.rods;

import me.liam.rodwars.Rod;
import me.liam.rodwars.RodItem;
import me.liam.rodwars.RodWars;
import me.liam.rodwars.utils.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SigmaRod extends Rod {
    
    // stores each players' 'CHARGE'. When it accumulates enough, players can unleash a right-click ability.
    private final HashMap<UUID, Double> charges;
    private final double chargeThreshold = 75;
    
    public SigmaRod() {
        super(Material.BLAZE_ROD, "Sigma Rod", "&b<< &1&lSigma Rod &b>>");
        charges = new HashMap<>();
    }
    
    @Override
    protected void handleRightClickAir(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        double currCharge = charges.getOrDefault(id, 0d);
        // 1: check if player has enough charge
        if (!charges.containsKey(id)/* || charges.get(id) < chargeThreshold*/) {
            return;
        }
        // 2: do the effect
        
        // initial sounds
        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 0.5f);
        
        for (Entity en : p.getNearbyEntities(10, 10, 10)) {
            if (!(en instanceof LivingEntity le)) {
                continue;
            }
            // push the entity away!
            // magnitude of push is calculated based on charge
            // if max charge is attained, special effect occurs along with the push
            Vector pushAway = RodWars.getInstance().getParticleUtil().getTowards(p.getEyeLocation(), le.getEyeLocation());
            pushAway.multiply(currCharge == 0 ? new Vector(0, 0, 0)
                    : new Vector(1 + (currCharge / chargeThreshold), 1d, 1 + (currCharge / chargeThreshold)));
            pushAway.add(new Vector(0, 0.9 * currCharge / chargeThreshold, 0));
            le.setVelocity(pushAway);
            le.playHurtAnimation((float) (5 * (currCharge / chargeThreshold)));
            
            // sounds
            float pitch = (float) (0.7 + 0.3 * Math.random());
            le.getWorld().playSound(le.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, pitch);
            le.getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
        }
        
        // reset the charge to 0
        charges.put(id, 0d);
    }
    
    @Override
    protected Set<Listener> getListeners() {
        Listener l = new Listener() {
            
            // Handling Left Click
            @EventHandler
            public void onHit(EntityDamageByEntityEvent e) {
                
                // entity type base cases
                if (!(e.getEntity() instanceof LivingEntity victim) || !(e.getDamager() instanceof Player p)) {
                    return;
                }
                // if item used is not sigma rod, do nothing.
                ItemStack item = p.getInventory().getItemInMainHand();
                UUID id = p.getUniqueId();
                if (!RodItem.isMatchingRod(item, getName())) {
                    return;
                }
                
                // DO STUFF!!!
                
                // 1: get the current charge
                double charge = e.getDamage() * 5; // we will make the damage equal to the charge
                e.setDamage(charge);
                double currCharge = !charges.containsKey(id) ? charge : Math.min(charges.get(id) + charge, chargeThreshold);
                
                // add the charge!
                charges.put(id, currCharge);
                
                // send player their charge (formatted to 2 decimal places)
                String message = currCharge == chargeThreshold ? String.format("&2&lReady (%.2f)", currCharge) :
                        String.format("&6Charge: &6&l%.2f", currCharge);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatUtil.colorize(message)).build());
            }
            
            // HANDLES DISPLAYING CHARGE (action bar message)
            @EventHandler
            public void onItemSwitch(PlayerItemHeldEvent e) {
                Player p = e.getPlayer();
                UUID id = p.getUniqueId();
                //ItemStack item = p.getInventory().getItemInMainHand();
                ItemStack item = p.getInventory().getItem(e.getNewSlot());
                if (!RodItem.isMatchingRod(item, getName())) {
                    // reset the action bar message
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(" ").build());
                    return;
                }
                
                // display charge as action bar message
                BukkitRunnable br = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (p.getInventory().getHeldItemSlot() != e.getNewSlot()) {
                            cancel();
                            return;
                        }
                        double currCharge = !charges.containsKey(id) ? 0 : charges.get(id);
                        // send player their charge (formatted to 2 decimal places)
                        String message = currCharge == chargeThreshold ? String.format("&2&lReady (%.2f)", currCharge) :
                                String.format("&6Charge: &6&l%.2f", currCharge);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatUtil.colorize(message)).build());
                    }
                };
                
                // send the message over and over until player swaps items again.
                br.runTaskTimer(RodWars.getInstance(), 0, 40);
            }
            
        };
        return Set.of(l);
    }
}

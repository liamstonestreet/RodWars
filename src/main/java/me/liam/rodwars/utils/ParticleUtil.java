package me.liam.rodwars.utils;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtil extends MathUtil {
    
    public void spawnSphere(Location center, double radius, int points, Particle particle) {
        spawnSphere(center, radius, points, particle, null);
    }
    
    public void spawnSphere(Location center, double radius, int points, Particle particle, Particle.DustOptions dustOptions) {
        World world = center.getWorld();
        if (world == null) return; // Check if the world is not null
    
        double increment = (2 * Math.PI) / points;
        for (int i = 0; i < points; i++) {
            for (int j = 0; j < points; j++) {
                double theta = i * increment;
                double phi = j * increment;
                double x = center.getX() + (radius * Math.sin(theta) * Math.cos(phi));
                double y = center.getY() + (radius * Math.cos(theta));
                double z = center.getZ() + (radius * Math.sin(theta) * Math.sin(phi));
                if (dustOptions != null && particle == Particle.REDSTONE) {
                    world.spawnParticle(particle, new Location(world, x, y, z), 1, dustOptions);
                } else {
                    world.spawnParticle(particle, new Location(world, x, y, z), 1);
                }
            }
        }
    }
    
    public void spawnHelixAboutProjectile(double theta, double radius, Entity e, Particle particle,
                                          Particle.DustOptions dustOptions, int numParticles, boolean connectingLines){
        // pitch: around x-axis
        // yaw: around y-axis
        // roll: around z-axis
        Location aLoc = e.getLocation();
        double pitch = PI - Math.toRadians(aLoc.getPitch());
        double yaw = Math.toRadians(aLoc.getYaw()) + PI/2;
        
        List<Location> locs = new ArrayList<>();
        
        // HELIX
        for (double d = 0; d < 2*PI; d += 2*PI/numParticles){
            double x = 0;
            double y = radius * Math.sin(theta+d);
            double z = radius * Math.cos(theta+d);
            Vector vec = new Vector(x, y, z);
            vec = rotateVector(vec, pitch, yaw);
            //locs.add(displayParticleOffset(particle, dustOptions, vec, aLoc, true));
            locs.add(displayParticleOffset(particle, dustOptions, vec, aLoc, true));
        }
        
        // CONNECTING PARTICLE LINES
        if (connectingLines) {
            int size = locs.size();
            for (int i = 0; i < size; i++){
                Location loc1 = locs.get(i);
                // if on last iteration, make particle line from last location to first location
                Location loc2 = (i == size-1) ? locs.get(0) : locs.get(i+1);
                createParticleLine(loc1, loc2, 8);
            }
        }
    }
    
    
    public void spawnRotatedCircle(double radius, Location center, Location dir, Particle particle, double amount, Particle.DustOptions dustOptions){
        // testing
//        double pitch = Math.toRadians((dir.getPitch() + 90) * -1);
//        double yaw = Math.toRadians(dir.getYaw() <= 0 ? dir.getYaw() * -1 : 360 - dir.getYaw());
        // base case
        if (radius <= 0) {
            return;
        }
        
        double pitch = PI - Math.toRadians(dir.getPitch() * -1);
        double yaw = PI/2 + Math.toRadians(dir.getYaw() * -1);
        
        // CIRCLE
        for (double d = 0; d < PI * 2; d += PI / amount) {
            double x = 0;
            double y = radius * Math.sin(d);
            double z = radius * Math.cos(d);
            
            Vector vec = new Vector(x, y, z);
            vec = rotateVector(vec, pitch, yaw);
            
            // display particle at offsetted location
            displayParticleOffset(particle, dustOptions, vec, center, true);
        }
    }
    
    
    private Location displayParticleOffset(Particle p, Particle.DustOptions dustOptions, Vector offset, Location center, boolean visible){
        if (dustOptions == null) {
            dustOptions = new Particle.DustOptions(Color.RED, 1f);
        }
        Location offsettedLoc = center.clone().add(offset);
        center.add(offset);
        if (visible){
            if  (p == Particle.REDSTONE){
                center.getWorld().spawnParticle(p, center, 0, dustOptions);
            } else if (p == Particle.SCULK_CHARGE){
                center.getWorld().spawnParticle(p, center, 0, 1f);
            } else {
                center.getWorld().spawnParticle(p, center, 0);
            }
        }
        center.subtract(offset);
        return offsettedLoc;
    }
    
    private void createParticleLine(Location start, Location stop, double amount){
        // if either world is null or the two locations' worlds are different
        if (start.getWorld() == null || stop.getWorld() == null /*|| !Objects.equals(start.getWorld(), stop.getWorld())*/) return;
        World w = start.getWorld();
        
        Vector direction = stop.clone().subtract(start).toVector().normalize();
        double distance = start.distance(stop);
        for (double d = 0; d < distance; d += distance/amount){
            double divisor = 2*distance/amount;
            Particle.DustOptions dustOptions = new Particle.DustOptions(d % divisor == 0 ? Color.TEAL : Color.WHITE, 2);
            Location curr = start.clone().add(direction.clone().multiply(d));
            w.spawnParticle(Particle.REDSTONE, curr, 0, dustOptions);
        }
    }
    
    /**
     * This method returns the location in front of a player. Good for spawning projectiles there.
     * @param p the player.
     * @param numBlocks the number of blocks to put between player and returned location.
     */
    public Location getInfrontOf(Player p, int numBlocks) {
        return p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().clone().multiply(numBlocks));
    }
    
    public Vector getTowards(Location from, Location to) {
        return to.toVector().clone().subtract(from.toVector()).normalize();
    }
    
    public ItemDisplay spawnItemDisplay(Location loc, Material type) {
        ItemDisplay d = loc.getWorld().spawn(loc, ItemDisplay.class);
        d.setItemStack(new ItemStack(type));
        return d;
    }
    
    public Entity spawnEntity(Location loc, EntityType type) {
        return loc.getWorld().spawnEntity(loc, type);
    }
    
}

package me.liam.rodwars;

import lombok.Getter;
import me.liam.rodwars.utils.ChatUtil;
import me.liam.rodwars.utils.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public abstract class Rod {
    
    @Getter private final String name; // no color formatting. CANNOT have the '&' symbol!
    @Getter private final String displayName; // has color formatting
    @Getter private final RodItem rodItem; // the item itself
    @Getter private final Cooldown cooldown; // a cooldown object. Handles cooldowns for all rod users.
    @Getter private static final HashMap<String, Rod> allRods = new HashMap<>(); // Keeps track of all rods.
    
    // UTIL //
    @Getter private final ParticleUtil particleUtil;
    
    public Rod(Material type, String name) {
        this(type, name, name);
    }
    
    public Rod(Material type, String name, String displayName) {
        this.name = name;
        this.displayName = ChatUtil.colorize(displayName);
        this.rodItem = new RodItem(type).displayName(displayName);
        this.cooldown = new Cooldown();
        this.particleUtil = new ParticleUtil();
        allRods.put(name, this);
    }
    
    // These methods will automatically be handled in a listener. Not abstract because not all rods will need these.
    protected void handleLeftClickAir(PlayerInteractEvent e) {}
    protected void handleShiftLeftClickAir(PlayerInteractEvent e) {}
    protected void handleLeftClickBlock(PlayerInteractEvent e) {}
    protected void handleShiftLeftClickBlock(PlayerInteractEvent e) {}
    
    protected void handleRightClickAir(PlayerInteractEvent e) {}
    protected void handleShiftRightClickAir(PlayerInteractEvent e) {}
    protected void handleRightClickBlock(PlayerInteractEvent e) {}
    protected void handleShiftRightClickBlock(PlayerInteractEvent e) {}
    
    // All listeners returned by this method will be registered automatically.
    protected Set<Listener> getListeners() {return Set.of();}
    protected void registerListeners() {
        // Step 1: Handle all clicks from the 4 above abstract methods
        Listener click = new Listener() {
            @EventHandler
            public void onClick(PlayerInteractEvent e) {
                Player p = e.getPlayer();
                ItemStack item = p.getInventory().getItemInMainHand();
                if (!RodItem.isMatchingRod(item, getName())) return;
                // Handle different clicks
                switch (e.getAction()) {
                    case RIGHT_CLICK_AIR -> {
                        if (p.isSneaking()) {
                            handleShiftRightClickAir(e);
                        } else {
                            handleRightClickAir(e);
                        }
                    }
                    case RIGHT_CLICK_BLOCK -> {
                        if (p.isSneaking()) {
                            handleShiftRightClickBlock(e);
                        } else {
                            handleRightClickBlock(e);
                        }
                    }
                    case LEFT_CLICK_AIR -> {
                        if (p.isSneaking()) {
                            handleShiftLeftClickAir(e);
                        } else {
                            handleLeftClickAir(e);
                        }
                    }
                    case LEFT_CLICK_BLOCK -> {
                        if (p.isSneaking()) {
                            handleShiftLeftClickBlock(e);
                        } else {
                            handleLeftClickBlock(e);
                        }
                    }
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(click, RodWars.getInstance());
        
        // Step 2: Handle all other listeners created
        for (Listener l : getListeners()) {
            Bukkit.getPluginManager().registerEvents(l, RodWars.getInstance());
        }
        ChatUtil.log(Level.INFO, "Registered all events for class '" + getClass().getSimpleName() + "'");
    }
    
    /** //////////////////////// STATIC METHODS ///////////////////////// **/
    
    /**
     * Registers all listeners associated with rods that have been previously instantiated.
     */
    public static void registerAllListeners() {
        for (Rod r : allRods.values()) {
            r.registerListeners();
        }
    }
    
    /**
     * Returns the rod with the specified name.
     */
    
    public static Rod getRod(String rodName) {
        return allRods.getOrDefault(rodName, null);
    }
    
    /**
     * Same as getRod(), but parameter is the display name instead of the rod name.
     */
    
    public static Rod getRodFromDisplayName(String displayName) {
        displayName = displayName.toLowerCase();
        for (Rod rod : allRods.values()) {
            if (rod.getDisplayName().equals(displayName)) {
                return rod;
            }
        }
        return null;
    }
    
    /**
     * @assumes the package with the Rod classes is directly in the 'me.Liam.rodwars' package
     * @param packageName the name of the package with all the Rod classes inside
     */
    public static void buildAllRods(String packageName) {
        String sourcePath = "me.liam.rodwars";
        String absolutePath = "C:\\Users\\Liam\\Desktop\\Non-School\\Minecraft\\CODING Related\\" +
                "IntelliJ Workspaces\\RodWars\\src\\main\\java\\me\\liam\\rodwars\\";
        try {
            File directory = new File(absolutePath + packageName);
            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    if (file.endsWith(".java")) {
                        // removes the .java suffix.
                        String className = packageName + '.' + file.replaceAll("\\.java", "");
                        Class<?> clazz = Class.forName(sourcePath + "." + className);
                        
                        // check if clazz extends Rod
                        if (clazz.getSuperclass() == Rod.class && Rod.class.isAssignableFrom(clazz)) {
                            Constructor<?> constructor = clazz.getConstructor();
                            constructor.newInstance(); // NEW INSTANCE!
                            ChatUtil.log(Level.INFO, "Built the rod '" + clazz.getSimpleName() + "'");
                        }
                    }
                }
            }
        } catch (Exception e) {
            ChatUtil.log(Level.WARNING, "Error occurred while trying to build rods! Try reloading the server. " +
                    "Error: " + e.getLocalizedMessage());
            //e.printStackTrace();
        }
    }
    
    /** ///////////////////// PRIVATE METHODS ///////////////////////// **/

    
}

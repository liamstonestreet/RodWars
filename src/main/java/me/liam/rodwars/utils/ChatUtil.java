package me.liam.rodwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

import java.util.logging.Level;

public class ChatUtil {
    
    public static void sendColor(Entity e, String str) {
        e.sendMessage(colorize(str));
    }
    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    
    /**
     * The equivalent of bukkit logging, but with built-in styled prefix.
     * @param level message level
     * @param msg the message
     */
    public static void log(Level level, String msg) {
        Bukkit.getLogger().log(level, "[RodWars] " + msg);
    }
    
    
    
}

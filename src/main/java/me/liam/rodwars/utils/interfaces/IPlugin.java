package me.liam.rodwars.utils.interfaces;

import me.liam.rodwars.RodWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;

public interface IPlugin {
    
    PluginManager pm = Bukkit.getServer().getPluginManager();
    ConsoleCommandSender sender = Bukkit.getServer().getConsoleSender();
    
    void cmd(); // Where all commands AND tab-completes are registered
    void listen(); // Where all listeners are registered
    default void init() {}
    
    default void sendStatus(String status){
        // This method detects the inputted status; if "enabled", then print in green text. Else, print in red text.
        sender.sendMessage("");
        if (status.equalsIgnoreCase("enabled")){
            sender.sendMessage(ChatColor.GREEN + getPlugin().getName() + " has been enabled!");
        } else if (status.equalsIgnoreCase("disabled")){
            sender.sendMessage(ChatColor.RED + getPlugin().getName() + " has been disabled!");
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + getPlugin().getName() + " status: " + status);
        }
        sender.sendMessage("");
    }
    
    default void enablePlugin(){ // onEnable mother code
        init(); // MUST ALWAYS BE FIRST (because cmd() and listen() might need initialization done first)
        cmd();
        listen();
        sendStatus("enabled");
    }
    default void disablePlugin(){ // onDisable mother code
        sendStatus("disabled");
    }
    
    // Helper plugin. Could also use RodWars.getInstance()
    static RodWars getPlugin(){
        return RodWars.getPlugin(RodWars.class);
    }
    
}

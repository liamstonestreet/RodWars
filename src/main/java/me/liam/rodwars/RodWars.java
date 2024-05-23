package me.liam.rodwars;

import lombok.Getter;
import me.liam.rodwars.listeners.PlayerJoin;
import me.liam.rodwars.listeners.PlayerRespawn;
import me.liam.rodwars.utils.ChatUtil;
import me.liam.rodwars.utils.MathUtil;
import me.liam.rodwars.utils.ParticleUtil;
import me.liam.rodwars.utils.interfaces.ICommand;
import me.liam.rodwars.utils.interfaces.IPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RodWars extends JavaPlugin implements IPlugin {
    
    // UTIL variables
    @Getter private MathUtil mathUtil;
    @Getter private ParticleUtil particleUtil;
    
    public static RodWars getInstance() {
        return getPlugin(RodWars.class);
    }
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        enablePlugin();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
        disablePlugin();
    }
    
    @Override
    public void cmd() {
        ICommand.registerAllCommands(getInstance(), "commands");
    }
    
    @Override
    public void listen() {
        Rod.registerAllListeners();
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(), this);
    }
    
    @Override
    public void init() {
        // MAKING ALL RODS (constructor necessary for rod to be in the HashMap in Rod.java class)
        Rod.buildAllRods("rods");
        mathUtil = new MathUtil();
        particleUtil = new ParticleUtil();
    }
}

package me.liam.rodwars.utils.interfaces;

import me.liam.rodwars.RodWars;
import me.liam.rodwars.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public interface ICommand {
    
    default String getRodNameCMD(String rawName) {
        return rawName.toUpperCase().replaceAll(" ", "_");
    }
    
    default String getRodNameRaw(String cmdName) {
        String result = "";
        String[] segments = cmdName.toLowerCase().split("_");
        for (String seg : segments) {
            seg = seg.substring(0, 1).toUpperCase() + seg.substring(1);
            result += seg + " ";
        }
        return result.trim();
    }
    
    String getCommand(); // this is necessary for the ICommand.registerAllCommands() method.
    
    default void sendUsage(CommandSender sender, String usage){
        sendColor(sender, "&cUsage: " + usage);
    }
    default void sendError(CommandSender sender, String msg){
        sendColor(sender, "&c" + msg);
    }
    default void sendColor(CommandSender sender, String msg){
        sender.sendMessage(ChatUtil.colorize(msg));
    }
    
    default boolean isPlayer(Entity e){ return e instanceof Player; }
    default boolean isPlayer(CommandSender sender){ return sender instanceof Player; }
    
    default String getBase(Command cmd){ return cmd.getName(); }
    default String getArgument(String[] args, int index){ return args[index]; }
    
    default boolean argEquals(String commandToMatch, String[] args, int index){ return args[index].equalsIgnoreCase(commandToMatch); }
    
    default Player playerFrom(CommandSender sender){ return (Player) sender; }
    default Player playerFrom(Entity e){ return (Player) e; }
    
    default List<String> autofix(List<String> tabs, String[] args, int length){
        List<String> result = new ArrayList<>();
        for (String tab : tabs){
            if (tab.toLowerCase().startsWith(args[length - 1].toLowerCase())){
                result.add(tab);
            }
        }
        return result;
    }
    
    /**
     * @apiNote This method registers all commands in the specified package.
     * @param instance the main plugin instance
     * @param packageName the package name (including path)
     */
    static void registerAllCommands(JavaPlugin instance, String packageName) {
        
        String sourcePath = "me.liam.rodwars";
        String absolutePath = "C:\\Users\\Liam\\Desktop\\Non-School\\Minecraft\\CODING Related\\" +
                "IntelliJ Workspaces\\RodWars\\src\\main\\java\\me\\liam\\rodwars\\";
        
        try {
            File directory = new File(absolutePath + packageName);
            if (directory.exists()) {
                String[] files = directory.list();
                if (files != null) {
                    for (String file : files) {
                        if (file.endsWith(".java")) {
                            // removes the .java suffix.
                            String className = packageName + '.' + file.replaceAll("\\.java", "");
                            Class<?> clazz = Class.forName(sourcePath + "." + className);
                            
                            // registers the command (if applicable)
                            if (CommandExecutor.class.isAssignableFrom(clazz)) {
                                Constructor<?> constructor = clazz.getConstructor();
                                CommandExecutor commandExecutor = (CommandExecutor) constructor.newInstance();
                                // invoking the getCommand method to get the command
                                String command = (String) clazz.getMethod("getCommand").invoke(commandExecutor);
                                // registering the command
                                instance.getCommand(command).setExecutor(commandExecutor);
                            }
                            
                            // registers the tab completer (if applicable)
                            if (TabCompleter.class.isAssignableFrom(clazz)) {
                                Constructor<?> constructor = clazz.getConstructor();
                                TabCompleter tabCompleter = (TabCompleter) constructor.newInstance();
                                // invoking the getCommand method to get the command
                                String command = (String) clazz.getMethod("getCommand").invoke(tabCompleter);
                                // registering the tab completer
                                instance.getCommand(command).setTabCompleter(tabCompleter);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

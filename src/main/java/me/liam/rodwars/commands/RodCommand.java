package me.liam.rodwars.commands;

import me.liam.rodwars.Rod;
import me.liam.rodwars.utils.interfaces.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RodCommand implements CommandExecutor, TabCompleter, ICommand {
    
    String invalidCommand = "Invalid command. Use \"/rodwars help\" to see all commands.";
    
    @Override
    public String getCommand() {
        return "rodwars";
    }
    
    public RodCommand() {
    
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        
        if (!isPlayer(cs)) return true;
        Player p = playerFrom(cs);
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    // TODO handle the help command
                } else {
                    sendError(p, invalidCommand);
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("give")) {
                    String rodNameCMD = args[1];
                    
                    // fix the formatting
                    String rodNameRaw = getRodNameRaw(rodNameCMD);
                    if (!Rod.getAllRods().containsKey(rodNameRaw)) {
                        sendError(p, "That rod does not exist.");
                        break;
                    } else {
                        ItemStack rodItem = Rod.getAllRods().get(rodNameRaw).getRodItem().toItemStack();
                        p.getInventory().addItem(rodItem);
                        sendColor(p, "&aSuccessfully gave the rod!");
                    }
                } else {
                    sendError(p, invalidCommand);
                }
                break;
            default:
                sendError(p, invalidCommand);
                break;
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        int length = args.length;
        List<String> tabComplete = new ArrayList<>();
        
        switch (length) {
            case 1:
                tabComplete = autofix(List.of("help", "give"), args, length);
                break;
            case 2:
                if (args[0].equalsIgnoreCase("give")) {
                    List<String> rods = new ArrayList<>(Rod.getAllRods().keySet().stream().toList());
                    rods.replaceAll(name -> name.toUpperCase(Locale.ROOT).replaceAll(" ", "_"));
                    tabComplete = autofix(rods, args, length);
                }
                break;
            default:
                break;
        }
        return tabComplete;
    }
}

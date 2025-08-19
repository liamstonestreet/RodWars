package me.liam.rodwars;

import me.liam.rodwars.utils.ChatUtil;
// import net.kyori.adventure.text.Component;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RodItem {
    
    // instance fields
    private final ItemStack rodItem; // valid types: Blaze Rod, End Rod, Lightning Rod, and Fishing Rod.
    private final Enchantment glowEnchant = Enchantment.BINDING_CURSE;
    private final ItemMeta meta;
    
    public RodItem(Material type) {
        // Can only be one of these 4 types. If not, it defaults to Blaze Rod.
        type = Arrays.asList(Material.BLAZE_ROD, Material.END_ROD, Material.LIGHTNING_ROD, Material.FISHING_ROD)
                .contains(type) ? type : Material.BLAZE_ROD;
        rodItem = new ItemStack(type);
        meta = rodItem.getItemMeta();
        // add default item flags (By default, it is all item flags).
        meta.addItemFlags(ItemFlag.values());
        // save changes to the item
        rodItem.setItemMeta(meta);
    }
    
    /**
     *
     * @param displayName the display name with color formatting. EX: &a or &b, etc
     * @return the modified, updated RodItem instance
     */
    public RodItem displayName(String displayName) {
        displayName = ChatUtil.colorize(displayName);
        meta.setDisplayName(displayName);
        rodItem.setItemMeta(meta);
        //this.meta = rodItem.getItemMeta(); // update meta global variable
        return this;
    }
    
    public RodItem lore(String... lines) {
        List<String> lore = new ArrayList<>(Arrays.asList(lines));
        meta.setLore(lore);
        rodItem.setItemMeta(meta);
        return this;
    }
    
    public RodItem glow(boolean glow) {
        if (glow) {
            // check if already glowing
            if (rodItem.getEnchantments().isEmpty()) {
                // not glowing, so add glow.
                rodItem.addUnsafeEnchantment(glowEnchant, 0);
            }
        } else {
            rodItem.removeEnchantments();
        }
        return this;
    }
    
    public ItemStack toItemStack() {
        return rodItem;
    }
    
    public static boolean isMatchingRod(ItemStack item, String rodName) {
        ItemStack rodItem = Rod.getRod(rodName).getRodItem().toItemStack();
        return rodItem.isSimilar(item);
    }
    
    
    
}

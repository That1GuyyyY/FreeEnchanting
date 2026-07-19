package me.that1guyyyy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchantListener implements Listener {
    private static FreeEnchanting plugin;

    public EnchantListener(FreeEnchanting plugin) {
        EnchantListener.plugin = plugin;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        boolean requiresXp = plugin.getConfig().getBoolean("requires-xp", false);
        if (!requiresXp) {
            event.setExpLevelCost(0); 
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.ENCHANTING) return;
        boolean requiresLapis = plugin.getConfig().getBoolean("requires-lapis", true);
        
        if (!requiresLapis) {
            ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI, 3);
            event.getInventory().setItem(1, lapis);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§8NoXP Settings Menu")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            Player player = (Player) event.getWhoClicked();
            Material clicked = event.getCurrentItem().getType();

            if (clicked == Material.EXPERIENCE_BOTTLE) {
                boolean current = plugin.getConfig().getBoolean("requires-xp");
                plugin.getConfig().set("requires-xp", !current);
                plugin.saveConfig();
                openSettingsMenu(player);
            } else if (clicked == Material.LAPIS_LAZULI) {
                boolean current = plugin.getConfig().getBoolean("requires-lapis");
                plugin.getConfig().set("requires-lapis", !current);
                plugin.saveConfig();
                openSettingsMenu(player);
            } else if (clicked == Material.ENCHANTING_TABLE) {
                player.closeInventory();
                player.openEnchanting(player.getLocation(), true);
            }
            return;
        }

        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            boolean requiresLapis = plugin.getConfig().getBoolean("requires-lapis", true);
            if (!requiresLapis && event.getRawSlot() == 1) {
                event.setCancelled(true); 
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() != InventoryType.ENCHANTING) return;
        boolean requiresLapis = plugin.getConfig().getBoolean("requires-lapis", true);
        if (!requiresLapis) {
            event.getInventory().setItem(1, null); 
        }
    }

    public static void openSettingsMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "§8NoXP Settings Menu");

        ItemStack xpItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta xpMeta = xpItem.getItemMeta();
        xpMeta.setDisplayName("§aXP Requirement");
        boolean currentXp = plugin.getConfig().getBoolean("requires-xp");
        xpMeta.setLore(Arrays.asList("§7Status: " + (currentXp ? "§cENABLED" : "§aDISABLED"), "§eClick to toggle!"));
        xpItem.setItemMeta(xpMeta);
        gui.setItem(2, xpItem);

        ItemStack lapisItem = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta lapisMeta = lapisItem.getItemMeta();
        lapisMeta.setDisplayName("§bLapis Requirement");
        boolean currentLapis = plugin.getConfig().getBoolean("requires-lapis");
        lapisMeta.setLore(Arrays.asList("§7Status: " + (currentLapis ? "§cENABLED" : "§aDISABLED"), "§eClick to toggle!"));
        lapisItem.setItemMeta(lapisMeta);
        gui.setItem(4, lapisItem);

        ItemStack testItem = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta testMeta = testItem.getItemMeta();
        testMeta.setDisplayName("§d§lTEST CONFIG");
        testMeta.setLore(Arrays.asList("§7Click to open a temporary", "§7enchanting table UI!"));
        testItem.setItemMeta(testMeta);
        gui.setItem(6, testItem);

        player.openInventory(gui);
    }
}


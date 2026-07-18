package me.that1guyyyy;

import org.bukkit.plugin.java.JavaPlugin;

public class FreeEnchanting extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EnchantListener(), this);
    }
}

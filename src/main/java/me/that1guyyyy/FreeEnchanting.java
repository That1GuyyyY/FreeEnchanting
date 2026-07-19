package me.that1guyyyy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FreeEnchanting extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        saveDefaultConfig(); 
        getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
        getCommand("noxp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.isOp()) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        EnchantListener.openSettingsMenu(player);
        return true;
    }
}

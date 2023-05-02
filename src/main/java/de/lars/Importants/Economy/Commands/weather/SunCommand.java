package de.lars.Importants.Economy.Commands.weather;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SunCommand implements CommandExecutor {
    private final Importants plugin;

    public SunCommand(Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Importants.weather")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        Player player = (Player) sender;
        World world = null;
        world = player.getWorld();
        world.setStorm(false);
        world.setThundering(false);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                + plugin.getMessageHandler()
                .getMessage("weather_clear")
                .orElse("§aThe weather has been set to clear"));
        return false;
    }
}

package de.lars.Importants.Economy.Commands.weather;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ThunderCommand implements CommandExecutor {
    private final Importants plugin;

    public ThunderCommand(Importants plugin) {
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
        world.setStorm(true);
        world.setThundering(true);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("weather_thunder")
                .orElse("§aThe weather has been set to thunder"));
        return false;
    }
}

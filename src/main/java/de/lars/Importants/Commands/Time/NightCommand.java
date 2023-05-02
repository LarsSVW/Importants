package de.lars.Importants.Commands.Time;

import de.lars.Importants.Importants;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCommand implements CommandExecutor {

    private final Importants plugin;


    public NightCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Importants.time")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        World world = null;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else if (sender instanceof BlockCommandSender) {
            world = ((BlockCommandSender) sender).getBlock().getWorld();
        }
        world.setTime(13000);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("time_night")
                .orElse("§aTime set to night"));
        return true;
    }
}


package de.lars.Importants.Commands.SitCommands;

import de.lars.Importants.Importants;
import de.lars.Importants.listener.SeatListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {
    private final Importants plugin;

    public SitCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("Importants.sit")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                Player player = (Player) sender;
                Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                SeatListener.getInstance().sitOnStair(loc, player);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("sit_msg")
                        .orElse("§aYou took a seat."));
                return true;
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return false;
            }
        }

        return false;
    }
}
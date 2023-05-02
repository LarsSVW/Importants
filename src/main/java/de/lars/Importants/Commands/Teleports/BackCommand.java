package de.lars.Importants.Commands.Teleports;

import de.lars.Importants.Importants;
import de.lars.Importants.listener.BackListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackCommand implements CommandExecutor {

    private final Map<UUID, Location> lastLocations;
    private final Importants plugin;

    public BackCommand(final Importants plugin) {

        this.plugin = plugin;
        this.lastLocations = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new BackListener(this), plugin);
    }

    public void setLastLocation(Player player, Location location) {
        this.lastLocations.put(player.getUniqueId(), location);
    }

    public Location getLastLocation(Player player) {
        return this.lastLocations.get(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("Importants.back")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Location lastLocation = getLastLocation(player);

        if (lastLocation == null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_previous_location")
                    .orElse("§cYou have no previous location to go to!"));
            return true;
        }

        player.teleport(lastLocation);
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("teleported_back")
                .orElse("§aTeleported back to your previous location!"));
        return true;
    }
}


package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TNTCommand implements CommandExecutor {
    private final Importants plugin;


    public TNTCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if sender is a player and has permission
        if (!(sender instanceof Player) || !sender.hasPermission("Importants.tnt")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getTargetBlock(null, 100).getLocation().add(0, 1, 0);

        loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("Tnt_spawned")
                .orElse("§cTnt spawned at the location youre looking at."));

        return true;
    }
}
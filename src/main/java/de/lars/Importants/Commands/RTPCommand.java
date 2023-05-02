package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;


public class RTPCommand implements CommandExecutor {

    private final Importants plugin;

    public RTPCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("not_a_player")
                .orElse("§cOnly players can execute this command."));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("Importants.rtp")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return false;
        }

        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("Random-Teleport-allow", true)) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("rtp_disabled")
                    .orElse("§cThe RTP function is currently disabled."));
            return false;
        }

        int maxBlocks = config.getInt("Max-Block-Teleportation", 5000);

        Random random = new Random();
        int x = random.nextInt(maxBlocks * 2) - maxBlocks;
        int z = random.nextInt(maxBlocks * 2) - maxBlocks;
        int y = player.getWorld().getHighestBlockYAt(x, z);

        Location location = new Location(player.getWorld(), x, y, z);
        player.teleport(location);

        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("rtp_teleported")
                .orElse("§eYou got teleported to a random location"));
        return true;
    }
}
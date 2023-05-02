package de.lars.Importants.Commands.Spawn;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class HubCommand implements CommandExecutor {
    private final Importants plugin;

    public HubCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }
        Player p = (Player)sender;
        File file = new File("plugins//Importants//hub.yml");
        if (!file.exists()) {
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_hub_set")
                    .orElse("§cNo hub is set! Please set a HubPoint!"));
            return true;
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Location loc = p.getLocation();
        double x = cfg.getDouble("X");
        double y = cfg.getDouble("Y");
        double z = cfg.getDouble("Z");
        double yaw = cfg.getDouble("Yaw");
        double pitch = cfg.getDouble("Pitch");
        String worldname = cfg.getString("Worldname");
        World welt = Bukkit.getWorld(worldname);
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        loc.setYaw((float)yaw);
        loc.setPitch((float)pitch);
        loc.setWorld(welt);
        p.teleport(loc);
        p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("hub_teleport")
                .orElse("§aSuccessfully teleported to the hub."));
        return true;

    }
}


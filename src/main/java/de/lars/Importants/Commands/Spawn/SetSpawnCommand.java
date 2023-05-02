package de.lars.Importants.Commands.Spawn;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {
    private final Importants plugin;

    public SetSpawnCommand(final Importants plugin) {
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
        Player p = (Player) sender;
        if (!p.hasPermission("Importants.setspawn")) {
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        File file = new File("plugins//Importants//spawn.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException iOException) {
            }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Location loc = p.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        double yaw = loc.getYaw();
        double pitch = loc.getPitch();
        String worldname = loc.getWorld().getName();
        cfg.set("X", Double.valueOf(x));
        cfg.set("Y", Double.valueOf(y));
        cfg.set("Z", Double.valueOf(z));
        cfg.set("Yaw", Double.valueOf(yaw));
        cfg.set("Pitch", Double.valueOf(pitch));
        cfg.set("Worldname", worldname);
        try {
            cfg.save(file);
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("spawn_set")
                    .orElse("§aSpawn successfully set!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
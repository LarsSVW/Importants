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

public class SetAllCommand implements CommandExecutor {
    private final Importants plugin;

    public SetAllCommand(final Importants plugin) {
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
        if (!p.hasPermission("Importants.setspawn")) {
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        File file = new File("plugins//Importants//hub.yml");
        File file1 = new File("plugins//Importants//spawn.yml");
        File file2 = new File("plugins//Importants//lobby.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException iOException) {}
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
        if (!file1.exists())
            try {
                file1.createNewFile();
            } catch (IOException iOException) {}
        YamlConfiguration cfg1 = YamlConfiguration.loadConfiguration(file);
        Location loc1 = p.getLocation();
        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();
        double yaw1 = loc1.getYaw();
        double pitch1 = loc1.getPitch();
        String worldname1 = loc1.getWorld().getName();
        cfg1.set("X", Double.valueOf(x1));
        cfg1.set("Y", Double.valueOf(y1));
        cfg1.set("Z", Double.valueOf(z1));
        cfg1.set("Yaw", Double.valueOf(yaw1));
        cfg1.set("Pitch", Double.valueOf(pitch1));
        cfg1.set("Worldname", worldname1);
        if (!file2.exists())
            try {
                file2.createNewFile();
            } catch (IOException iOException) {}
        YamlConfiguration cfg2 = YamlConfiguration.loadConfiguration(file);
        Location loc2 = p.getLocation();
        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();
        double yaw2 = loc2.getYaw();
        double pitch2 = loc2.getPitch();
        String worldname2 = loc2.getWorld().getName();
        cfg2.set("X", Double.valueOf(x2));
        cfg2.set("Y", Double.valueOf(y2));
        cfg2.set("Z", Double.valueOf(z2));
        cfg2.set("Yaw", Double.valueOf(yaw2));
        cfg2.set("Pitch", Double.valueOf(pitch2));
        cfg2.set("Worldname", worldname2);
        try {
            cfg.save(file);
            cfg1.save(file1);
            cfg2.save(file2);
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("all_set")
                    .orElse("§aSuccessfully set Spawn,Lobby,Hub Point!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
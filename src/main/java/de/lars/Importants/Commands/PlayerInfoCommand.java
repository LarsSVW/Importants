package de.lars.Importants.Commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import javax.swing.*;

public class PlayerInfoCommand implements CommandExecutor {

    private final Importants plugin;

    public PlayerInfoCommand(Importants plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /pinfo [Playername]");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("Could not find player: " + args[0]);
            return true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        Date date = new Date(player.getFirstPlayed());
        String firstPlayed = sdf.format(date);

        long totalPlayTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) ;
        long days = totalPlayTime / 86400;
        long hours = (totalPlayTime % 86400) / 3600;
        long minutes = ((totalPlayTime % 86400) % 3600) / 60;
        long seconds = ((totalPlayTime % 86400) % 3600) % 60;
        String playTime = days + "d " + hours + "h " + minutes + "m " + seconds + "s";

        Location location = player.getLocation();
        String worldName = location.getWorld().getName();


        int health = (int) player.getHealth();
        int maxHealth = (int) player.getMaxHealth();
        String healthString = health + "/" + maxHealth;

        float saturation = player.getSaturation();

        sender.sendMessage(
                plugin.getMessageHandler()
                .getMessage("player_info_title")
                .orElse("§6§l-----§e§lPlayer Info§6§l-----"));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_name")
                .orElse("§eName: §f%player%").replace("%player%", player.getName()));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_playtime")
                .orElse("§eOverall playtime: %playtime%").replace("%playtime%", playTime));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_online")
                .orElse("§eFirst join: §f%date%").replace("%date%", firstPlayed));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_location")
                .orElse("§eLocation:") + " " + plugin.getMessageHandler()
                .getMessage("player_info_location_x")
                .orElse("§eX: %x%").replace("%x%", (player.getLocation().getX())  + " " + plugin.getMessageHandler()
                        .getMessage("player_info_location_y")
                        .orElse("§eY: %y%").replace("%y%", (player.getLocation().getY())  + " " + plugin.getMessageHandler()
                                .getMessage("player_info_location_z")
                                .orElse("§eZ: %z%").replace("%z%", String.valueOf(player.getLocation().getY())))));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_world")
                .orElse("§eWorld player is in: §f%world%").replace("%world%", worldName));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_health")
                .orElse("§ePlayers health §f%health%").replace("%health%", healthString));
        sender.sendMessage( plugin.getMessageHandler()
                .getMessage("player_info_saturation")
                .orElse("§ePlayers saturation §f%saturation%").replace("%saturation%", String.valueOf(saturation)));

        return true;
    }

}
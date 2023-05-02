package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class VanishCommand implements CommandExecutor {

    private final Importants plugin;
    private final Set<Player> vanishedPlayers;

    public VanishCommand(final Importants plugin) {
            this.plugin = plugin;
        this.vanishedPlayers = new HashSet<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return false;
        }
        if (!(sender.hasPermission("Importants.vanish"))) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // Spieler hat keine Argumente eingegeben -> sich selbst vanishen/unvanishen
            if (vanishedPlayers.contains(player)) {
                // Spieler ist bereits vanished -> unvanish
                vanishedPlayers.remove(player);
                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                    p.showPlayer(plugin, player);
                });
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("unvanished")
                                .orElse("§aYou are visible again for other players."));
            } else {
                // Spieler ist nicht vanished -> vanish
                vanishedPlayers.add(player);
                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                    p.hidePlayer(plugin, player);
                });
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("vanished")
                                .orElse("§aYou are now invisible for other players."));
            }
        } else {
            // Spieler hat einen Spielername eingegeben -> den entsprechenden Spieler vanishen/unvanishen
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("player_not_found")
                                .orElse("§cPlayer not found."));
                return true;
            }

            if (vanishedPlayers.contains(target)) {
                // Spieler ist bereits vanished -> unvanish
                vanishedPlayers.remove(target);
                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                    p.showPlayer(plugin, target);
                });
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("unvanished_other")
                                .orElse("§aPlayer %player% is visible again for other players.").replace("%player%", target.getName()));
            } else {
                // Spieler ist nicht vanished -> vanish
                vanishedPlayers.add(target);
                Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                    p.hidePlayer(plugin, target);
                });
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("vanished_other")
                                .orElse("§aYou vanished %player%.").replace("%player%", target.getName()));
                target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("vanished_by_other")
                                .orElse("§aYou were vanished by %player%.").replace("%player%", player.getName()));
            }
            return true;
        }
        return false;
    }
}
package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GodModeCommand implements CommandExecutor {
    private final Importants plugin;

    public GodModeCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    private final Map<Player, Double> originalDamages = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        if (!sender.hasPermission("Importants.godmode")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            if (originalDamages.containsKey(player)) {
                 player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("godmode_deactivated")
                        .orElse("§aYour godmode has been disabled"));
                double originalDamage = originalDamages.remove(player);
                player.setInvulnerable(false);
                player.setAbsorptionAmount(0.0);
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(originalDamage);
            } else {
                // Spieler hat keinen Godmode - aktivieren
                double originalDamage = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                originalDamages.put(player, originalDamage);
                player.setInvulnerable(true);
                player.setAbsorptionAmount(Float.MAX_VALUE);
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.MAX_VALUE);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("godmode_activated")
                        .orElse("§aYour godmode has been enabled"));

                // Godmode auch für andere Spieler aktivieren
                for (Player p : player.getWorld().getPlayers()) {
                    if (p != player) {
                        p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("godmode_activated_other")
                                .orElse("§a%player% activated the godmode").replace("%player%", player.getName()));
                        p.hidePlayer(player);
                    }
                }
            }
        } else {
            // Spielername angegeben - Godmode für den angegebenen Spieler aktivieren/deaktivieren
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cThe Player is not online"));
                return true;
            }

            if (originalDamages.containsKey(target)) {
                // Spieler hat bereits Godmode - deaktivieren
                target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("godmode_deactivated")
                        .orElse("§aYour godmode has been disabled"));
                double originalDamage = originalDamages.remove(target);
                target.setInvulnerable(false);
                target.setAbsorptionAmount(0.0);
                target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(originalDamage);

                // Godmode auch für andere Spieler deaktivieren
                for (Player p : target.getWorld().getPlayers()) {
                    if (p != target) {
                        p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("godmode_deactivated_other")
                                .orElse("§a%player% deactivated the godmode").replace("%player%", player.getName()));
                        p.showPlayer(target);
                    }
                }
            } else {
                // Spieler hat keinen Godmode - aktivieren
                double originalDamage = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                originalDamages.put(target, originalDamage);
                target.setInvulnerable(true);
                target.setAbsorptionAmount(Float.MAX_VALUE);
                target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.MAX_VALUE);
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("godmode_activated_other_sender")
                        .orElse("§aGodmode activated for %player%").replace("%player%", target.getName()));
                // Godmode auch für andere Spieler aktivieren
                for (Player p : ((Player) sender).getWorld().getPlayers()) {
                    if (p != sender && p != target) {
                        p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("godmode_activated_other_target")
                                .orElse("§aActivated the godmode for %player%").replace("%player%", target.getName()));
                        p.hidePlayer(target);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
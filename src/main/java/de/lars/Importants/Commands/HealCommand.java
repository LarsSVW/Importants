package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    private final Importants plugin;

    public HealCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Nur Spieler können sich selbst heilen
        if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));            return true;
        }

        // Rechte überprüfen
        if (!sender.hasPermission("importants.heal")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Player player = (Player) sender;

        // /heal ohne Argumente heilt den Spieler selbst
        if (args.length == 0) {
            double maxHealth = player.getMaxHealth();

            player.setHealth(maxHealth);
            player.setFoodLevel(20);
            player.setSaturation(20.0F);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("healed")
                    .orElse("§aYou healed yourself."));
            return true;
        }

        // /heal <spielername> heilt den genannten Spieler
        if (args.length == 1) {
            if (!sender.hasPermission("importants.heal.others")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cThe Player is not online"));
                return true;
            }
            double maxHealth = target.getMaxHealth();

            target.setHealth(maxHealth);
            target.setFoodLevel(20);
            target.setSaturation(20.0F);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("healed_other")
                    .orElse("§aYou healed %player%.").replace("%player%", target.getName()));
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("healed_other_target")
                    .orElse("§aYou got healed by %player%.").replace("%player%", sender.getName()));
            return true;
        }

        // /heal mit mehr als einem Argument gibt eine Fehlermeldung aus
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("wrong_usage")
                .orElse("§cWrong usage! Correct usage:" )+  " /heal [Playername]");
        return true;
    }
}
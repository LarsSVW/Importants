package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;

public class ExHealthCommand implements CommandExecutor {
    private final Importants plugin;

    public ExHealthCommand(Importants plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        Player player = (Player) sender;

        // Check if player has permission
        if (!player.hasPermission("Importants.extrahealth")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        // Check if argument is a valid number
        if (args.length < 1) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("provide_max_hearts")
                    .orElse("§cPlease provide a amount for the maximum amount of hearts."));
            return true;
        }

        int health;
        try {
            health = Integer.parseInt(args[args.length - 1]);
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_amount")
                    .orElse("§cInvalid amount please provide a correct amount."));
            return true;
        }

        if (args.length > 1) { // Set another player's max health
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cThe Player is not online."));
                return true;
            }

            target.setMaxHealth(health);
            target.setHealth(health);

            target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("your_max_hearts")
                    .orElse("§cYour maximum of hearts are now %health%").replace("%health%", String.valueOf(health)));
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("other_max_hearts")
                    .orElse("§cThe maximum of hearts for %player% are now %health%").replace("%health%", String.valueOf(health)).replace("%player%", target.getName()));
        } else { // Set player's max health
            player.setMaxHealth(health);
            player.setHealth(health);

            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("your_max_hearts")
                    .orElse("§cYour maximum of hearts are now %health%").replace("%health%", String.valueOf(health)));        }

        return true;
    }
}
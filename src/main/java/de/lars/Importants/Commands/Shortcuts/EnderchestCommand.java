package de.lars.Importants.Commands.Shortcuts;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderchestCommand implements CommandExecutor {
    private final Importants plugin;

    public EnderchestCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ec")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 1 && player.hasPermission("Importants.spy")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("not_online")
                            .orElse("§cThe player is not online."));
                    return true;
                }
                player.openInventory(target.getEnderChest());
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("ec_other")
                        .orElse("§aYou opened the enderchest of %player%").replace("%player%", target.getName()));
                return true;
            } else if (args.length == 0 && player.hasPermission("Importants.ec")) {
                player.openInventory(player.getEnderChest());
                return true;
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
        }
        return false;
    }
}

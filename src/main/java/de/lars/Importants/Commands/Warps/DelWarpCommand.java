package de.lars.Importants.Commands.Warps;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {

    private final Importants plugin;

    public DelWarpCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Importants.warpadmin")) {
                if (args.length == 1) {
                    String warpName = args[0].toLowerCase();
                    if (plugin.getWarpManager().warpExists(warpName)) {
                        plugin.getWarpManager().removeWarp(warpName);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("warp_deleted")
                                .orElse("§aWarp successfully deleted. Deleted Warp: %warp%").replace("%warp%", warpName));
                        return true;
                    } else {
                        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("warp_not_exist")
                                .orElse("§cThe warp %warp% does not exist").replace("%warp%", warpName));
                        return false;
                    }
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("warp_no_name")
                            .orElse("§ePlease provide a name for your warp."));
                    return false;
                }
            } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return false;
            }
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return false;
        }
    }
}

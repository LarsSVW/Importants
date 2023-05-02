package de.lars.Importants.Commands.Teleports;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDeclineCommand implements CommandExecutor {
    private final Importants plugin;

    public TpDeclineCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Importants.tpa")) {
                if (TpahereCommand.tpahereRequests.containsKey(player.getUniqueId())) {
                    Player target = TpahereCommand.tpahereRequests.get(player.getUniqueId());
                    if (target.isOnline()) {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_declined")
                                .orElse("§eYou denied the tpa request from %player%.").replace("%player%", target.getName()));
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_declined_target")
                                .orElse("§e%player% denied your tpa request").replace("%player%", player.getName()));
                        TpahereCommand.tpahereRequests.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cThe Player is not online"));
                    }
                } else if (TpaCommand.tpaRequests.containsKey(player.getUniqueId())) {
                    Player requester = TpaCommand.tpaRequests.get(player.getUniqueId());
                    if (requester.isOnline()) {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_declined")
                                .orElse("§eYou denied the tpa request from %player%.").replace("%player%", requester.getName()));
                        requester.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_declined_target")
                                .orElse("§e%player% denied your tpa request").replace("%player%", player.getName()));
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cThe Player is not online"));
                    }
                }
            } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));            }
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));        }
        return true;
    }
}
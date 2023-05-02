package de.lars.Importants.Commands.Teleports;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand implements CommandExecutor {
    private final Importants plugin;

    public TpAcceptCommand(final Importants plugin) {
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
                        player.teleport(target);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpahere_accept")
                                .orElse("§aYou got teleported to %player%").replace("%player%", target.getName()));
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_accepted")
                                .orElse("§a%player% accepted your TPA request").replace("%player%", player.getName()));
                        TpahereCommand.tpahereRequests.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cThe Player is not online! "));
                    }
                } else if (TpaCommand.tpaRequests.containsKey(player.getUniqueId())) {
                    Player requester = TpaCommand.tpaRequests.get(player.getUniqueId());
                    if (requester.isOnline()) {
                        requester.teleport(player);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_teleported_here")
                                .orElse("§a%player% got teleported to you!").replace("%player%", requester.getName()));
                        requester.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_accepted")
                                .orElse("§a%player% accepted your TPA request").replace("%player%", player.getName()));
                        TpaCommand.tpaRequests.remove(requester.getUniqueId());
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cThe Player is not online! "));
                    }
                    }
                } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                }
            } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            }
            return true;
        }
    }
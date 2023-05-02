package de.lars.Importants.Commands.Teleports;


import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {
    private final Importants plugin;

    public TpaCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("Importants.tpa")){
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_received")
                                .orElse("§a%target% wants to teleport to you.Type /tpaccept or /tpdecline to answer the request.").replace("%target%", player.getName()));

                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_send")
                                .orElse("§aTpa request sent to %player%.").replace("%player%", target.getName()));
                        tpaRequests.put(target.getUniqueId(), player);
                    } else {
                            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                    + plugin.getMessageHandler()
                                    .getMessage("not_online")
                                    .orElse("§cThe Player is not online"));
                    }
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("wrong_usage")
                            .orElse("§cWrong usage! Correct usage:" )+  " /tpa [Player]");
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


    public static Map<UUID, Player> tpaRequests = new HashMap<>();
}


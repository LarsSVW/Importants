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

public class TpahereCommand implements CommandExecutor {
    private final Importants plugin;

    public TpahereCommand(final Importants plugin) {
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
                                .getMessage("tpa_here_received")
                                .orElse("§e%player% wants you to teleport to him! Type /tpaccept or /tpdecline to answer the request.").replace("%player%", player.getName()));
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("tpa_here_send")
                                .orElse("§eTpa request sent to %player%").replace("%player%", target.getName()));
                        tpahereRequests.put(target.getUniqueId(), player);
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
                            .orElse("§cWrong usage! Correct usage:" )+  " /tpahere [Player]");
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


    public static Map<UUID, Player> tpahereRequests = new HashMap<>();
}


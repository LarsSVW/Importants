package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanIPCommand implements CommandExecutor {
    private final Importants plugin;

    public BanIPCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("Importants.ban")) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /ban-ip [Player] [Reason]");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_online")
                    .orElse("§cPlayer not found"));
            return false;
        }

        String ipAddress = target.getAddress().getHostString();
        if (Bukkit.getBanList(BanList.Type.IP).isBanned(ipAddress)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("already_banned")
                    .orElse("§cThis Player is already banned!"));
            return true;
        }

        String reason = args.length > 1 ? String.join(" ", args).substring(args[0].length() + 1) :plugin.getMessageHandler().getMessage("ban_message").orElse("You have been banned");
        target.kickPlayer(reason);
        Bukkit.getBanList(BanList.Type.IP).addBan(ipAddress, reason, null, null);
        return true;
    }
}
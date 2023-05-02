package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    private final Importants plugin;

    public BanCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (!sender.hasPermission("Importants.ban")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + " /ban [Player [Reason]");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cPlayer not found"));
                return true;
            }
            String reason = "";
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    reason += args[i] + " ";
                }
                reason = reason.trim();
            }
            target.kickPlayer(reason);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("banned_successfully")
                    .orElse("§aYou banned %player% successfully").replace("%player%", target.getName()));
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getDisplayName(), reason, null, null);
            return true;
        }
        return false;
    }

}

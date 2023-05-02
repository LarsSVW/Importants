package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand implements CommandExecutor {

    private final Importants plugin;

    public UnbanCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                    .orElse("§cWrong usage! Correct usage:") + " /unban [Player/IP]");
            return true;
        }

        String nameOrIp = args[0];

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        if (banList.isBanned(nameOrIp)) {
            banList.pardon(nameOrIp);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("unbanned")
                    .orElse("§a%player% has been unbanned").replace("%playerorIP%", nameOrIp));
            return true;
        }

        banList = Bukkit.getBanList(BanList.Type.IP);
        if (banList.isBanned(nameOrIp)) {
            banList.pardon(nameOrIp);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("unbanned_ip")
                    .orElse("§aAll players with the IP adress %ip% have been unbanned").replace("%ip%", nameOrIp));
            return true;
        }
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("ip_not_found")
                .orElse("§cPlayer or IP not found in the ban list."));
        return true;
    }
}
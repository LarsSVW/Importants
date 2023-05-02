package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanIpCommand implements CommandExecutor {
    private final Importants plugin;

    public UnbanIpCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        if (!sender.hasPermission("Importants.ban")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("provide_ip")
                    .orElse("§cPlease provide an IP address."));
            return false;
        }

        String ip = args[0];
        BanList banList = Bukkit.getBanList(BanList.Type.IP);


        if (!banList.isBanned(ip)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("ip_not_found")
                    .orElse("§cPlayer or IP not found in the ban list."));
            return true;
        }

        banList.pardon(ip);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("unbanned_ip")
                .orElse("§aAll players with the IP adress %ip% have been unbanned").replace("%ip%", ip));
        return true;
    }
}
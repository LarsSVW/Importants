package de.lars.Importants.Commands.Message;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {
    private final Importants plugin;
    public BroadcastCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("Importants.broadcast")) {
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
                    .orElse("§cWrong usage! Correct usage:") + " /broadcast [Message]");
            return true;
        }

        String message = String.join(" ", args);

            Bukkit.getServer().broadcastMessage(plugin.getMessageHandler().getMessage("broadcast_design").orElse("§a#==================================#"));
            Bukkit.getServer().broadcastMessage(plugin.getMessageHandler()
                    .getMessage("broadcast_prefix")
                    .orElse("§6§lFor all Players"));
            Bukkit.getServer().broadcastMessage(plugin.getMessageHandler().getMessage("broadcast_design").orElse("§a#==================================#"));
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));

        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("broadcast_send")
                .orElse("§aBroadcast successfully sent."));
        return true;
    }
}
package de.lars.Importants.Commands.Message;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnounceCommand implements CommandExecutor {
    private final Importants plugin;

    public AnnounceCommand(final Importants plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Importants.announce")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:" )+  " /announce [Message]");
            return true;
        }

        String message = String.join(" ", args);
        Bukkit.getServer().broadcastMessage(plugin.getMessageHandler().getMessage("announce_design").orElse("§a#==================================#"));
        Bukkit.getServer().broadcastMessage(plugin.getMessageHandler()
                .getMessage("announce_prefix")
                .orElse("§6§lANNOUNCE"));
        Bukkit.getServer().broadcastMessage(plugin.getMessageHandler().getMessage("announce_design").orElse("§a#==================================#"));
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
}
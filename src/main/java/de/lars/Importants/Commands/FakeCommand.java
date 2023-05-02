package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FakeCommand implements CommandExecutor {
    private final Importants plugin;

    public FakeCommand(Importants plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /fake [firstjoin|join|leave] NAME");
            return true;
        }

        String playerName = args[1];

        if (args[0].equalsIgnoreCase("join")) {
            Bukkit.broadcastMessage( plugin.getMessageHandler().getMessage("join-message").orElse("§a%player% joined the Server.").replace("%player%", playerName));
        } else if (args[0].equalsIgnoreCase("leave")) {
            Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("leave-message").orElse("§a%player% left the Server.").replace("%player%", playerName));
        } else if (args[0].equalsIgnoreCase("firstjoin")) {
            Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("firstjoin-message").orElse("§a%player% joined the Server the first time.").replace("%player%", playerName));
        } else  {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /fake [firstjoin|join|leave] NAME");
        }

        return true;
    }
}
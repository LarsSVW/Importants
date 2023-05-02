package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
    private final Importants plugin;

    public HelpCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("Importants.help")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("announce_design").orElse("§a#==================================#"));
            sender.sendMessage(plugin.getMessageHandler()
                    .getMessage("prefix")
                    .orElse("§0§l[§eImportants§0§l]"));
            sender.sendMessage(plugin.getMessageHandler().getMessage("announce_design").orElse("§a#==================================#"));
            sender.sendMessage(plugin.getMessageHandler().getMessage("help_message").orElse("§aIf you need help with my plugin, please join my discord.: (discord.lars-svw.de) If you have additional requests or suggestions for improvement, you can also clarify these via my Discord."));
            return true;
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return false;
        }
    }
}

package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KickCommand implements CommandExecutor {
    private final Importants plugin;

    public KickCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission
        if (!sender.hasPermission("Importants.kick")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        // Check if correct number of arguments are provided
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") +  " /kick [Player] [Reason]");
            return true;
        }

        // Get target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_online")
                    .orElse("§cPlayer not found"));
            return true;
        }

        // Build kick reason
        String reason = plugin.getMessageHandler().getMessage("kick_reason").orElse("§cYou have been kicked by an Operator");
        if (args.length > 1) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        // Kick the target player
        target.kickPlayer(ChatColor.RED + plugin.getMessageHandler().getMessage("kicked_msg").orElse("You have been kicked for %reason%").replace("%reason%", reason));

        // Broadcast kick message
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("kick_success")
                .orElse("§aYou successfully kicked %target%").replace("%target%", target.getName()));
        return true;
    }
}

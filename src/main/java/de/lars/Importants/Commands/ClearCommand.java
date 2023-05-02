package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand implements CommandExecutor {
    private final Importants plugin;

    public ClearCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Überprüfe, ob der Sender die Berechtigung hat
        if (!sender.hasPermission("Importants.clear")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        // Wenn keine Argumente angegeben sind, lösche das Inventar des Senders
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }

            Player player = (Player) sender;
            player.getInventory().clear();
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("inventory_cleared")
                    .orElse("§aYour Inventory got cleared."));
            return true;
        }

        // Wenn ein Spielername angegeben wurde, lösche das Inventar des Spielers
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("clear_player_not_found")
                    .orElse("§cPlayer %player% not found").replace("%player%", args[0]));
            return true;
        }

        target.getInventory().clear();
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("cleared_player")
                .orElse("§cInventory of %player% got cleard").replace("%player%", target.getName()));
        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("inventory_cleared")
                .orElse("§aYour Inventory got cleared."));
        return true;
    }
}
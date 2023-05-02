package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {
    private final Importants plugin;

    public FeedCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("feed")) {
            return false;
        }

        if (!sender.hasPermission("importants.feed")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }
            Player player = (Player) sender;
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("feed_self")
                    .orElse("§aYou fed yourself"));
        } else {
            if (!sender.hasPermission("importants.feed.others")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cThe Player is not online"));
                return true;
            }

            target.setFoodLevel(20);
            target.setSaturation(20);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("feed_other")
                    .orElse("§aYou feeded %player%").replace("%player%", target.getName()));
            target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("feed_other_target")
                    .orElse("§aYou have been fed"));
        }
        return true;
    }
}

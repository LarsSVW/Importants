package de.lars.Importants.Commands.Moderation.Warn;

import de.lars.Importants.Commands.Moderation.Warn.WarnManager;
import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class WarnCommand implements CommandExecutor {

    private final Importants plugin;
    private final WarnManager warnManager;

    public WarnCommand(Importants plugin) {
        this.plugin = plugin;
        this.warnManager = new WarnManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has permission to use the command
        if (!sender.hasPermission("importants.warn")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("no_permission")
                .orElse("§cYou have no authorization to do so."));
            return true;
        }

        // Check if the command was used correctly
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/warn [add/check/remove] player [reason/number/all] ");
            return true;
        }

        if (args[0].equalsIgnoreCase("check")) { // Check if the user wants to check warnings for a player
            if (args.length != 2) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/warn check player");
                return true;
            }

            Player target = plugin.getServer().getPlayer(args[1]); // Get the target player
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cPlayer not found"));
                return true;
            }

            int warnings = warnManager.getWarnings(target.getUniqueId());
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("warnings_amount")
                    .orElse("§a%player% has %amount% warnings.").replace("%player%", target.getName()).replace("%amount%", String.valueOf(warnings)));
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) { // Check if the user wants to check warnings for a player
            if (args.length < 3) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/warn add player reason");
                return true;
            }

            Player target = plugin.getServer().getPlayer(args[1]); // Get the target player
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cPlayer not found"));
                return true;
            }

            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

            // Add the warning to the warn manager
            warnManager.addWarning(target, sender.getName(), reason);

            // Notify the sender and the target player of the warning
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("warned_someone")
                    .orElse("§aYou have warned %target% for %reason%").replace("%target%", target.getName()).replace("%reason%", reason));
            target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("warning_receive")
                    .orElse("§aYou have been warned by %player% for %reason%").replace("%player%", sender.getName()).replace("%reason%", reason));
        }

        if (args[0].equalsIgnoreCase("remove")) { // Check if the user wants to remove warnings for a player
            if (args.length < 3) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/warn remove [Number/All] Player");
                return true;
            }

            Player target = plugin.getServer().getPlayer(args[2]); // Get the target player
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cPlayer not found"));
                return true;
            }

            String warningNumberString = args[1];
            int warnings = warnManager.getWarnings(target.getUniqueId());

            if (warningNumberString.equalsIgnoreCase("all")) { // Remove all warnings for the player
                warnManager.removeWarnings(target.getUniqueId());
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("all_warnings_removed")
                        .orElse("§aAll warnings have been removed for %player%").replace("%player%", target.getName()));
                return true;
            } else {
                try {
                    int warningNumber = Integer.parseInt(warningNumberString);
                    if (warningNumber <= 0) {
                        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("warning_must_positive")
                                .orElse("§aWarning number has to be positive."));
                        return true;
                    }
                    if (warningNumber > warnings) {
                        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("warnings_amount_only")
                                .orElse("§aPlayer %player% has only %amount% warnings").replace("%player%", target.getName()).replace("%amount%", String.valueOf(warnings)));
                        return true;
                    }
                    warnManager.setWarnings(target.getUniqueId(), warnings - warningNumber);
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("amount_warnings_removed")
                            .orElse("§aRemoved %amount% warnings from player %player%").replace("%player%", target.getName()).replace("%amount%", String.valueOf(warningNumber)));
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("warning_must_positive")
                            .orElse("§aWarning number has to be positive."));
                    return true;
                }
            }
        }
        return false;
    }
}
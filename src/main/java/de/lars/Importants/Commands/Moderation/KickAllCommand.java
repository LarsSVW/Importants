package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KickAllCommand implements CommandExecutor {
    private final Importants plugin;

    public KickAllCommand(Importants plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("Importants.kickall")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        String reason = plugin.getMessageHandler().getMessage("kick_reason").orElse("§cYou have been kicked by an Operator");
        if (args.length > 0) {
            reason = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(reason);
            }


            return true;
        }
    }
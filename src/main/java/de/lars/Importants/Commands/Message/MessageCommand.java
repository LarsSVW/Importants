package de.lars.Importants.Commands.Message;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageCommand implements CommandExecutor {
    private final Importants plugin;
    public MessageCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Importants.msg")) {
                if (args.length >= 2) {
                    Player target = player.getServer().getPlayer(args[0]);
                    if (target != null) {
                        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                        sender.sendMessage(plugin.getMessageHandler()
                                        .getMessage("msg_design_sender")
                                        .orElse("§0§l[§aYou §7-> §a%target%§0§l] §f%message%").replace("%sender%", player.getName()).replace("%message%", ChatColor.translateAlternateColorCodes('&',  message)).replace("%target%", target.getName()));
                        target.sendMessage(plugin.getMessageHandler().getMessage("msg_design_target")
                                .orElse("§0§l[§a%sender% §7-> §aYou§0§l] §f%message%").replace("%sender%", player.getName()).replace("%message%", ChatColor.translateAlternateColorCodes('&',  message)).replace("%target%", target.getName()));
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cPlayer not found"));
                    }
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("wrong_usage")
                            .orElse("§cWrong usage! Correct usage:") +  " /msg [Playername] [Message]");
                }
            } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            }
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly Players can execute this Command"));
        }
        return true;
    }
}

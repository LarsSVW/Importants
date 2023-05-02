package de.lars.Importants.Commands;


import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {
    private final Importants plugin;

    public NickCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("Importants.nick")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:" )+  " /nick [new name]");
            return true;
        }

        String nick;
        Player target;

        if (args.length == 1) {
            nick = args[0];
            target = player;
        } else {
            target = Importants.getInstance().getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cThe Player is not online"));
                return true;
            }
            nick = args[1];
        }

        target.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("new_nick")
                .orElse("§a%player% new nickname is %nick%").replace("%player%", target.getName()).replace("%nick%", nick));
        return true;
    }
}
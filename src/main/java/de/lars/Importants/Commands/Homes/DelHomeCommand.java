package de.lars.Importants.Commands.Homes;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final Importants plugin;

    public DelHomeCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!sender.hasPermission("Importants.delhome")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
            if (args.length == 1) {
                String homeName = args[0].toLowerCase();
                if (plugin.getHomeManager().homeExists(playerName, homeName)) {
                    plugin.getHomeManager().deleteHome(playerName, homeName);
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                            plugin.getMessageHandler()
                                    .getMessage("home_delete")
                                    .orElse("§aHome %home% deleted successfully").replace("%home%", homeName));
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                            plugin.getMessageHandler()
                                    .getMessage("home_not_found")
                                    .orElse("§cHome %home% not found").replace("%home%", homeName));
                }
            } else {
                return false;
            }
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
        }
        return true;
    }
}
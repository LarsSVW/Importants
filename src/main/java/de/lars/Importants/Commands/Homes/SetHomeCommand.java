package de.lars.Importants.Commands.Homes;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private final Importants plugin;

    public SetHomeCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!sender.hasPermission("Importants.sethome")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
            if (args.length == 1) {
                String homeName = args[0].toLowerCase();
                int maxHomes = 3; // default number of homes
                if (player.hasPermission("Importants.home.premium")) {
                    maxHomes = 10; // premium users can set 10 homes
                }
                if (player.isOp()) {
                    maxHomes = 10000; // op users can set unlimited homes
                }
                if (plugin.getHomeManager().getHomesList(playerName).size() >= maxHomes) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                            plugin.getMessageHandler()
                                    .getMessage("maximum_homes")
                                    .orElse("§cYou have reached the maximum number of homes!"));
                    return true;
                }
                Location homeLocation = player.getLocation();
                plugin.getHomeManager().setHome(playerName, homeName, homeLocation);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("home_set")
                                .orElse("§aHome %home% set successfully").replace("%home%", homeName));
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

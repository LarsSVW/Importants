package de.lars.Importants.Commands.Homes;

import de.lars.Importants.Importants;
import de.lars.Importants.Managers.HomeManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final HomeManager homeManager;
    private final Importants plugin;
    public HomeCommand(HomeManager homeManager, Importants plugin) {
        this.homeManager = homeManager;
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
        if (!sender.hasPermission("Importants.home")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // list homes
            List<String> homes = homeManager.getHomesList(player.getName());
            if (homes.isEmpty()) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("no_homes_set")
                                .orElse("§cYou have not set any homes"));
            } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("your_homes")
                                .orElse("§aYour Homes:"));
                for (String home : homes) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                            plugin.getMessageHandler()
                                    .getMessage("homes_list")
                                    .orElse("§a- %home%").replace("%home%", home));
                }
            }
            return true;
        } else if (args.length == 1) {
            // teleport to home
            String homeName = args[0];
            if (!homeManager.homeExists(player.getName(), homeName)) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                                .getMessage("home_not_found")
                                .orElse("§cHome %home% not found").replace("%home%", homeName));
                return true;
            }
            Location homeLocation = homeManager.getHomeLocation(player.getName(), homeName);
            player.teleport(homeLocation);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                    plugin.getMessageHandler()
                            .getMessage("teleported_home")
                            .orElse("§aYou have been teleported to your home %home%.").replace("%home%", homeName));
            return true;
        } else {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                    plugin.getMessageHandler()
                            .getMessage("wrong_usage")
                            .orElse("§aWrong usage! Correct usage:") + " " + "/home [HomeName]" );
            return true;
        }
    }
}
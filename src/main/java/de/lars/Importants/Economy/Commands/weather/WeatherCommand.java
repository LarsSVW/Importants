package de.lars.Importants.Economy.Commands.weather;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class WeatherCommand implements CommandExecutor, TabCompleter {
    private final Importants plugin;

    public WeatherCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Importants.weather")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }

        if (args.length != 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:" )+  " /weather [clear|rain|thunder]");
            return true;
        }

        World world = null;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            world = player.getWorld();
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        String arg = args[0].toLowerCase();
        switch (arg) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("weather_clear")
                        .orElse("§aThe weather has been set to clear"));
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("weather_rain")
                        .orElse("§aThe weather has been set to rain"));
                break;
            case "thunder":
                world.setStorm(true);
                world.setThundering(true);
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("weather_thunder")
                        .orElse("§aThe weather has been set to thunder"));
                break;
            default:
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:" )+  " /weather [clear|rain|thunder]");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("clear");
            completions.add("rain");
            completions.add("thunder");
        }
        return completions;
    }
}
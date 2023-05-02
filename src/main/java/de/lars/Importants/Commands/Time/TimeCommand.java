package de.lars.Importants.Commands.Time;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {

    private final Importants plugin;


    public TimeCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Importants.time")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:" )+  " /time [day|night]");
            return true;
        }

        String time = args[0].toLowerCase();
        World world = null;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else if (sender instanceof BlockCommandSender) {
            world = ((BlockCommandSender) sender).getBlock().getWorld();
        }

        if (world == null) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("world_not_found")
                    .orElse("§cWorld not found!"));
            return true;
        }

        if (time.equals("day")) {
            world.setTime(0);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("time_day")
                    .orElse("§aTime set to day"));
            return true;
        } else if (time.equals("night")) {
            world.setTime(13000);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("time_night")
                    .orElse("§aTime set to night"));
            return true;
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:" )+  " /time [day|night]");
            return true;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("day");
            completions.add("night");
        }
        return completions;
    }
}

package de.lars.Importants.Commands.Message;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class FilterCommand implements CommandExecutor {
    private final Importants plugin;

    private Configuration config;

    public FilterCommand(Importants plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
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
        if (!sender.hasPermission("Importants.filter")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/filter add [word|command] [Word|Command]");            return true;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase("command")) {
                if (args[2] != null) {
                    plugin.addBlockedCommand(args[2]);
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("addedcmd")
                            .orElse("§aYou added %cmd% to the CMD-Blacklist").replace("%cmd%", args[2]));
                } else {
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("command_null")
                            .orElse("§cThe command name cannot be null."));
                }
            } else if (args[1].equalsIgnoreCase("word")){
                if (args[2] != null) {
                    plugin.addBlockedWord(args[2]);
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("addedword")
                            .orElse("§aYou added %word% to the Word-Blacklist").replace("%word%", args[2]));
                } else {
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("word_null")
                            .orElse("§cThe word cannot be null."));
                }
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/filter add [word|command] [Word|Command]");
            }
        }
        return true;
    }
}
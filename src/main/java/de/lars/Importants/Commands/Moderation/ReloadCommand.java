package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand implements CommandExecutor {

    private final Importants plugin;
    private final Configuration config;

    public ReloadCommand(Importants plugin, Configuration config) {
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ireload")) {
            if (!sender.hasPermission("Importants.reload")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            this.config.load();
            plugin.getMessageHandler().reloadMessages();
            plugin.getKitManager().saveKits();
            plugin.getServer().getPluginManager().enablePlugin(plugin);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("reload_success")
                    .orElse("§aReloaded all plugin files successfully."));
            return true;
        }
        return false;
    }

}
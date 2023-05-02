package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KillallCommand implements CommandExecutor {
    private final Importants plugin;

    public KillallCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("Importants.killall")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("killall")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("mobs")) {
                for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                    if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                        entity.remove();
                    }
                }
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("all_mobs_killed")
                        .orElse("§aAll mobs have been killed"));
                return true;
            } else if (args.length == 1) {
                String mobType = args[0];
                EntityType entityType = EntityType.valueOf(mobType.toUpperCase());
                for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                    if (entity.getType() == entityType) {
                        entity.remove();
                    }
                }
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("all_mobs_killed_type")
                        .orElse("§aAll %mobs% have been killed")
                        .replace("%mobs%", entityType.getName()));
                return true;
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage.") + "/killall mobs or /killall <mob type>");
                return false;
            }
        }
        return false;
    }
}
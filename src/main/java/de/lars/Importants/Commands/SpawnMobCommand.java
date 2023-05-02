package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnMobCommand implements CommandExecutor {

    private final Importants plugin;


    public SpawnMobCommand(final Importants plugin) {
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

        if (!sender.hasPermission("Importants.spawnmob")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/spawnmob [Mobname] [amount]");
            return true;
        }

        EntityType entityType = null;

        try {
            entityType = EntityType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("Invalid_Mob")
                    .orElse("§cYou specified a invalid Mob!"));
            return true;
        }

        int amount = 1;

        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("Invalid_Amount")
                        .orElse("§cYou specified a wrong Amount!"));
                return true;
            }
        }

        Location location = player.getLocation();

        for (int i = 0; i < amount; i++) {
            Entity entity = location.getWorld().spawnEntity(location, entityType);
            if (entity == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("Failed_to_spawn")
                        .orElse("§cFailed to spawn the Mob at your location."));
            }
        }

        player.sendMessage(ChatColor.GREEN + "Spawned " + amount + " " + entityType.name().toLowerCase() + "(s).");
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("Spawned")
                .orElse("Spawned ") + amount + " " + entityType.name());

        return true;
    }
}

package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FireballCommand implements CommandExecutor {

    private final Importants plugin;

    public FireballCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("Importants.fireball")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();

        Fireball fireball = player.launchProjectile(Fireball.class, direction.multiply(2));
        fireball.setIsIncendiary(true);
        fireball.setYield(2);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);

        return true;
    }

}
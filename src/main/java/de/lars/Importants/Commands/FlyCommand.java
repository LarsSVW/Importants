package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    private final Importants plugin;

    public FlyCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("Importants.fly")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));            return true;
        }

        if (args.length == 0) {
            toggleFly(player);
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_online")
                    .orElse("§cThe Player is not online"));
            return true;
        }

        toggleFly(target);
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("fly_message")
                .orElse("§aThe Player´s flying status is %flyingstatus%").replace("%flyingstatus%", target.isFlying() ? "activated." : "deactivated"));
        return true;
    }

    private void toggleFly(Player player) {
        if (player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("fly_self_deactivated")
                    .orElse("§aYour flyingmode is deactivated"));
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("fly_self_activated")
                    .orElse("§aYour flyingmode is activated"));
        }
    }
}
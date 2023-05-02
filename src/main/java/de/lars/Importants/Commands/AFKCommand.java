package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class AFKCommand implements CommandExecutor {

    private final HashMap<Player, Integer> afkTaskMap = new HashMap<>();
    private final Importants plugin;

    public AFKCommand(final Importants plugin) {
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
        if (!sender.hasPermission("Importants.afk")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        Player player = (Player) sender;
        if (afkTaskMap.containsKey(player)) {
            int taskId = afkTaskMap.remove(player);
            Bukkit.getScheduler().cancelTask(taskId);
            player.setPlayerListName(player.getName());

            // Nachricht senden
            Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("afk_remove")
                    .orElse("§7%player% is not afk anymore.").replace("%player%", player.getName()));

        } else {
            // Spieler ist nicht als AFK markiert, also AFK-Status setzen
            int taskId = new BukkitRunnable() {
                @Override
                public void run() {
                    player.setPlayerListName(ChatColor.GRAY +plugin.getMessageHandler().getMessage("afk_tablist").orElse("§7AFK %player%").replace("%player%", player.getName()));
                    Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("afk")
                            .orElse("§7%player% is now afk").replace("%player%", player.getName()));
                }
            }.runTaskLater(JavaPlugin.getPlugin(Importants.class), 20 * 30).getTaskId();

            afkTaskMap.put(player, taskId);

            // Nachricht senden
            Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("afk")
                    .orElse("§7%player% is now afk").replace("%player%", player.getName()));
        }

        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (afkTaskMap.containsKey(player)) {
            int taskId = afkTaskMap.remove(player);
            Bukkit.getScheduler().cancelTask(taskId);
            player.setPlayerListName(player.getName());

            // Nachricht senden
            Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("afk_remove")
                    .orElse("§7%player% is not afk anymore.").replace("%player%", player.getName()));        }
    }
}

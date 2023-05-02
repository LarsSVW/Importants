package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import jdk.jfr.internal.tool.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;

public class AFKListener implements Listener {
    public AFKListener(Configuration config, Importants plugin) {
        this.config = config;
        this.plugin = plugin;
    }
    private HashMap<Player, Integer> afkTaskMap = new HashMap<>();
    private HashSet<Player> afkPlayers = new HashSet<>();
    private final Configuration config;
    private final Importants plugin;

    @EventHandler
    public void onPlayerMoveAFK(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("Importants.afk")) {
            return;
        }

        int taskId = afkTaskMap.getOrDefault(player, -1);
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                player.setPlayerListName(ChatColor.GRAY +plugin.getMessageHandler().getMessage("afk_tablist").orElse("§7AFK %player%").replace("%player%", player.getName()));
                Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("afk")
                        .orElse("§7%player% is now afk").replace("%player%", player.getName()));

                afkPlayers.add(player);
                afkTaskMap.remove(player);
            }
        }.runTaskLater(JavaPlugin.getPlugin(Importants.class), 20L *config.getInt("afk_time")).getTaskId();

        afkTaskMap.put(player, taskId);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("Importants.afk")) {
            return;
        }

        int taskId = afkTaskMap.getOrDefault(player, -1);
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            player.setPlayerListName(player.getName());
            afkTaskMap.remove(player);

            if (afkPlayers.contains(player)) {
                Bukkit.broadcastMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("afk_remove")
                        .orElse("§7%player% is not afk anymore.").replace("%player%", player.getName()));
                afkPlayers.remove(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int taskId = afkTaskMap.getOrDefault(player, -1);
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            afkTaskMap.remove(player);

            if (afkPlayers.contains(player)) {
                afkPlayers.remove(player);
            }
        }
    }
}


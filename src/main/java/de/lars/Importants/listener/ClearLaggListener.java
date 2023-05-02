package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClearLaggListener implements Listener {
    private final Map<Item, Long> itemTimeMap = new HashMap<>();
    private int clearLaggTime;

    public ClearLaggListener(FileConfiguration config) {
        this.clearLaggTime = config.getInt("clearlaggtime");

        new BukkitRunnable() {
            @Override
            public void run() {
                clearLagg();
            }
        }.runTaskTimer(Importants.getPlugin(Importants.class), clearLaggTime * 20L, clearLaggTime * 20L); // Clear lag every clearLaggTime seconds

        Bukkit.getPluginManager().registerEvents(this, Importants.getPlugin(Importants.class));
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                itemTimeMap.put(event.getEntity(), System.currentTimeMillis());
            }
        }.runTaskLater(Importants.getPlugin(Importants.class), 20L); // Delay adding item to the map for 1 second
    }

    private void clearLagg() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<Item, Long>> iterator = itemTimeMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Item, Long> entry = iterator.next();
            Item item = entry.getKey();
            long timeDropped = entry.getValue();
            if (currentTime - timeDropped >= clearLaggTime * 1000L) {
                item.remove();
                iterator.remove();
            }
        }
    }
}
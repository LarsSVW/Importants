package de.lars.Importants.listener.ChatManagerListenerPackage;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class AutoBroadcastListener implements Runnable {
    private final Importants plugin;
    private final Configuration config;

    public AutoBroadcastListener(Importants plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public void run() {
        ConfigurationSection autoBroadcastSection = config.getConfigurationSection("AutoBroadcast");
        if (autoBroadcastSection != null) {
            for (String key : autoBroadcastSection.getKeys(false)) {
                ConfigurationSection messageSection = autoBroadcastSection.getConfigurationSection(key);
                if (messageSection != null) {
                    String message = messageSection.getString("Message");
                    int interval = messageSection.getInt("Interval");
                    if (message != null && !message.isEmpty()) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                        plugin.getLogger().info("Sent auto-broadcast: " + message);
                    }
                    if (interval > 0) {
                        plugin.getServer().getScheduler().runTaskLater(plugin, this, interval * 20L);
                        break;
                    }
                }
            }
        }
    }
}
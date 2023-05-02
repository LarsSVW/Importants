package de.lars.Importants.Commands.Moderation.Warn;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarnManager implements Listener {

    private final Map<UUID, Integer> warningsCache;
    private final FileConfiguration warningsConfig;
    private final Importants plugin;

    public WarnManager(Importants plugin) {
        this.warningsCache = new HashMap<>();
        this.plugin = plugin;

        // Load the warnings from the warnings.yml file
        File warningsFile = new File(plugin.getDataFolder(), "warnings.yml");
        if (!warningsFile.exists()) {
            plugin.saveResource("warnings.yml", false);
        }
        this.warningsConfig = YamlConfiguration.loadConfiguration(warningsFile);
        loadWarnings();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public int getWarnings(UUID playerId) {
        return warningsCache.getOrDefault(playerId, 0);
    }

    public void setWarnings(UUID playerId, int warningCount) {
        warningsCache.put(playerId, warningCount);
        warningsConfig.set(playerId.toString(), warningCount);
        saveWarnings();
    }

    public void removeWarnings(UUID playerId) {
        warningsCache.remove(playerId);
        warningsConfig.set(playerId.toString(), null);
        saveWarnings();
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == plugin) {
            loadWarnings();
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            saveWarnings();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!warningsCache.containsKey(playerId)) {
            warningsCache.put(playerId, 0);
        }
    }

    private void loadWarnings() {
        ConfigurationSection section = warningsConfig.getConfigurationSection("");
        if (section != null) {
            for (String playerIdString : section.getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(playerIdString);
                    int warningCount = warningsConfig.getInt(playerIdString);
                    warningsCache.put(playerId, warningCount);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    private void saveWarnings() {
        try {
            warningsConfig.save(new File(plugin.getDataFolder(), "warnings.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addWarning(Player player, String reason, String source) {
        UUID playerId = player.getUniqueId();
        int warningCount = getWarnings(playerId) + 1;
        setWarnings(playerId, warningCount);
    }
}
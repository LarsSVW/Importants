package de.lars.Importants.Managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HomeManager {

    private final Plugin plugin;
    private final Map<String, Map<String, Location>> homes;

    public HomeManager(Plugin plugin) {
        this.plugin = plugin;
        this.homes = new ConcurrentHashMap<>();
        loadHomes();
    }

    public boolean homeExists(String playerName, String homeName) {
        return homes.containsKey(playerName) && homes.get(playerName).containsKey(homeName);
    }

    public Location getHomeLocation(String playerName, String homeName) {
        if (!homeExists(playerName, homeName)) {
            return null;
        }
        return homes.get(playerName).get(homeName);
    }

    public List<String> getHomesList(String playerName) {
        if (!homes.containsKey(playerName)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(homes.get(playerName).keySet());
    }

    public void setHome(String playerName, String homeName, Location location) {
        homes.computeIfAbsent(playerName, k -> new ConcurrentHashMap<>()).put(homeName, location);
        saveHomes();
    }

    public void deleteHome(String playerName, String homeName) {
        homes.computeIfPresent(playerName, (k, v) -> {
            v.remove(homeName);
            return v;
        });
        saveHomes();
    }

    private void loadHomes() {
        File homesFile = new File(plugin.getDataFolder(), "Homes.yml");
        if (!homesFile.exists()) {
            return;
        }
        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        for (String playerName : homesConfig.getKeys(false)) {
            Map<String, Location> playerHomes = new ConcurrentHashMap<>();
            for (String homeName : homesConfig.getConfigurationSection(playerName).getKeys(false)) {
                Location homeLocation = (Location) homesConfig.get(playerName + "." + homeName);
                playerHomes.put(homeName, homeLocation);
            }
            homes.put(playerName, playerHomes);
        }
    }

    public void saveHomes() {
        File homesFile = new File(plugin.getDataFolder(), "Homes.yml");
        FileConfiguration homesConfig = new YamlConfiguration();
        for (Map.Entry<String, Map<String, Location>> playerEntry : homes.entrySet()) {
            String playerName = playerEntry.getKey();
            for (Map.Entry<String, Location> homeEntry : playerEntry.getValue().entrySet()) {
                String homeName = homeEntry.getKey();
                Location homeLocation = homeEntry.getValue();
                homesConfig.set(playerName + "." + homeName, homeLocation);
            }
        }
        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save Homes.yml: " + e.getMessage());
        }
    }

}
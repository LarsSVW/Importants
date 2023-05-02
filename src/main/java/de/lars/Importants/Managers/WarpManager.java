package de.lars.Importants.Managers;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpManager {

    private final Map<String, Location> warps;
    private final FileConfiguration warpsConfig;

    public WarpManager(JavaPlugin plugin) {
        this.warps = new HashMap<>();
        File warpsFile = new File(plugin.getDataFolder(), "warps.yml");
        if (!warpsFile.exists()) {
            plugin.saveResource("warps.yml", false);
        }
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
        loadWarps();
    }

    public boolean warpExists(String warpName) {
        return warps.containsKey(warpName);
    }

    public Location getWarpLocation(String warpName) {
        return warps.get(warpName);
    }

    public void addWarp(String warpName, Location warpLocation) {
        warps.put(warpName, warpLocation);
        saveWarps();
    }

    public void removeWarp(String warpName) {
        warps.remove(warpName);
        warpsConfig.set(warpName, null);
        saveWarps();
    }

    private void loadWarps() {
        if (warpsConfig.getKeys(false).size() > 0) {
            for (String warpName : warpsConfig.getKeys(false)) {
                World world = Bukkit.getWorld(warpsConfig.getString(warpName + ".world"));
                double x = warpsConfig.getDouble(warpName + ".x");
                double y = warpsConfig.getDouble(warpName + ".y");
                double z = warpsConfig.getDouble(warpName + ".z");
                float yaw = (float) warpsConfig.getDouble(warpName + ".yaw");
                float pitch = (float) warpsConfig.getDouble(warpName + ".pitch");
                Location warpLocation = new Location(world, x, y, z, yaw, pitch);
                warps.put(warpName, warpLocation);
            }
        }
    }

    public void saveWarps() {
        for (Map.Entry<String, Location> entry : warps.entrySet()) {
            String warpName = entry.getKey();
            Location warpLocation = entry.getValue();
            warpsConfig.set(warpName + ".world", warpLocation.getWorld().getName());
            warpsConfig.set(warpName + ".x", warpLocation.getX());
            warpsConfig.set(warpName + ".y", warpLocation.getY());
            warpsConfig.set(warpName + ".z", warpLocation.getZ());
            warpsConfig.set(warpName + ".yaw", warpLocation.getYaw());
            warpsConfig.set(warpName + ".pitch", warpLocation.getPitch());
        }
        try {
            warpsConfig.save(new File(Importants.getInstance().getDataFolder(), "warps.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


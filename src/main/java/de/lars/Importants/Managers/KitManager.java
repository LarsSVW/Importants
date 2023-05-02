package de.lars.Importants.Managers;

import de.lars.Importants.Commands.Kits.Kit;
import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitManager implements Listener {

    private final Map<String, ItemStack[]> kits;
    private final Map<String, String> kitCommands;
    private final FileConfiguration kitsConfig;
    private final Importants plugin;

    public KitManager(Importants plugin) {
        this.kits = new HashMap<>();
        this.kitCommands = new HashMap<>();
        this.plugin = plugin;

        // Load the kits from the kits.yml file
        File kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            plugin.saveResource("kits.yml", false);
        }
        this.kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);
        loadKits();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean kitExists(String kitName) {
        return kits.containsKey(kitName);
    }

    public ItemStack[] getKitItems(String kitName) {
        return kits.get(kitName);
    }

    public void createKit(String kitName, ItemStack[] items, String commandString) {
        kits.put(kitName, items);
        kitCommands.put(kitName, commandString);
        kitsConfig.set(kitName + ".items", items);
        kitsConfig.set(kitName + ".command", commandString);
        saveKits();
    }

    public void removeKit(String kitName) {
        kits.remove(kitName);
        kitCommands.remove(kitName);
        kitsConfig.set(kitName, null);
        saveKits();
    }

    public void giveKit(Player player, String kitName) {
        if (!kitExists(kitName)) {
            player.sendMessage(ChatColor.RED + "Kit '" + kitName + "' does not exist.");
            return;
        }

        ItemStack[] items = getKitItems(kitName);
        player.getInventory().addItem(items);
        String commandString = kitCommands.get(kitName);
        if (!commandString.isEmpty()) {
            commandString = commandString.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);
        }
        player.sendMessage(ChatColor.GREEN + "You received the kit '" + kitName + "'.");
    }

    private void loadKits() {
        kitsConfig.getKeys(false).forEach(kitName -> {
            ConfigurationSection kitSection = kitsConfig.getConfigurationSection(kitName);
            if (kitSection != null) {
                ItemStack[] items = ((List<ItemStack>) kitSection.getList("items")).toArray(new ItemStack[0]);
                String commandString = kitSection.getString("command", "");
                kits.put(kitName, items);
                kitCommands.put(kitName, commandString);
            }
        });
    }

    public void saveKits() {
        try {
            kitsConfig.save(new File(plugin.getDataFolder(), "kits.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Kit getKit(String kitName) {
        if (kitExists(kitName)) {
            ItemStack[] items = getKitItems(kitName);
            String commandString = kitCommands.get(kitName);
            return new Kit(kitName, items, commandString);
        }
        return null;
    }
    public void deleteKit(String kitName) {
        if (kitExists(kitName)) {
            removeKit(kitName);
        } else {
            plugin.getLogger().warning("Cannot delete kit '" + kitName + "'. Kit does not exist.");
        }
    }
}
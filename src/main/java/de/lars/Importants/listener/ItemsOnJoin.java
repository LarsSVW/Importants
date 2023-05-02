package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemsOnJoin implements Listener {

    private final File onJoinFile;
    private FileConfiguration onJoinConfig;
    private Importants plugin;

    public ItemsOnJoin(Importants plugin) {
        // Laden der onjoin.yml Datei
        this.plugin = plugin;
        onJoinFile = new File(plugin.getDataFolder(), "onjoin.yml");
        if (!onJoinFile.exists()) {
            plugin.saveResource("onjoin.yml", false);
        }
        onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Überprüfen, ob der Spieler zum ersten Mal joinen
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(new NamespacedKey(this.plugin, "first_join"), PersistentDataType.INTEGER)) {
            return;
        } else {
            dataContainer.set(new NamespacedKey(this.plugin, "first_join"), PersistentDataType.INTEGER, 1);
        }

        // Überprüfen, ob es Items in der onjoin.yml gibt
        if (onJoinConfig.contains("items")) {
            List<String> itemStrings = onJoinConfig.getStringList("items");
            for (String itemString : itemStrings) {
                ItemStack item = parseItem(itemString);
                player.getInventory().addItem(item);
            }
        }

        // Überprüfen, ob es Befehle in der onjoin.yml gibt
        if (onJoinConfig.contains("commands")) {
            List<String> commandStrings = onJoinConfig.getStringList("commands");
            for (String commandString : commandStrings) {
                String command = replacePlaceholders(commandString, player);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }

        // Überprüfen, ob es eine Nachricht an den Spieler in der onjoin.yml gibt
        if (onJoinConfig.contains("msg_player")) {
            List<String> messages = onJoinConfig.getStringList("msg_player");
            for (String message : messages) {
                String replacedMessage = replacePlaceholders(message, player);
                player.sendMessage(replacedMessage);
            }
        }
    }

    // Hilfsmethode zum Ersetzen von Platzhaltern in den Befehlen und Nachrichten
    private String replacePlaceholders(String input, Player player) {
        String output = input.replaceAll("%player%", player.getName());
        return output;
    }

    // Hilfsmethode zum Umwandeln der Item-Strings in ItemStack-Objekte
    private ItemStack parseItem(String input) {
        String[] parts = input.split(" ");
        String materialName = parts[0];
        int amount = Integer.parseInt(parts[1]);
        short data = 0;
        if (parts.length > 2) {
            data = Short.parseShort(parts[2]);
        }
        ItemStack item = new ItemStack(Material.valueOf(materialName), amount, data);
        return item;
    }
    public void reloadConfig() {
        onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
    }

    public void saveConfig() {
        try {
            onJoinConfig.save(onJoinFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
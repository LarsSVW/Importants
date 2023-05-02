package de.lars.Importants.listener.ChatManagerListenerPackage;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatManagerListener implements Listener {
    private final Importants plugin;
    private final Map<String, String> prefixes;

    public ChatManagerListener(Importants plugin) {
        this.plugin = plugin;
        this.prefixes = new HashMap<>();

        loadPrefixes();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void loadPrefixes() {
        prefixes.clear();
        FileConfiguration config = plugin.getConfig();
        prefixes.put("default", config.getString("ChatManager.default.prefix", ""));
        if (config.contains("ChatManager")) {
            for (String key : config.getConfigurationSection("ChatManager").getKeys(false)) {
                String permission = config.getString("ChatManager." + key + ".permission");
                String prefix = config.getString("ChatManager." + key + ".prefix");
                if (permission != null && prefix != null) {
                    prefixes.put(permission, ChatColor.translateAlternateColorCodes('&', prefix));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = prefixes.getOrDefault("default", "");

        if (player.isOp()) {
            prefix = ChatColor.translateAlternateColorCodes('&', "&4&lOwner");
        } else {
            for (String permission : prefixes.keySet()) {
                if (player.hasPermission(permission)) {
                    prefix = prefixes.get(permission) + " ";
                    break;
                }
            }
        }

        String message = event.getMessage();
        String formattedMessage = ChatColor.RESET + player.getDisplayName() + ChatColor.WHITE + ": " + message;
        event.setFormat(prefix + formattedMessage);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadPrefixes();
    }
}
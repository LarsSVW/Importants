package de.lars.Importants.Handlers;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.spigotmc.SpigotConfig.config;

public class MessageHandler {

    private final Importants plugin;
    private final Map<String, String> messages;

    private final Configuration messageConfig;

    public MessageHandler(final Importants plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        String language = plugin.getConfig().getString("language", "en.yml");
        this.messageConfig = new Configuration(new File(plugin.getDataFolder(), "messages.yml"));
        this.messageConfig.setTemplateName("/" + language);
        reloadMessages();
    }

    public void reloadMessages() {
        messages.clear();
        messageConfig.load();
        for (String key : messageConfig.getKeys(false)) {
            messages.put(key.toLowerCase(),
                    ChatColor.translateAlternateColorCodes('&', messageConfig.getString(key)));
        }
    }

    public Optional<String> getMessage(final String key) {
        return Optional.ofNullable(messages.get(key.toLowerCase()));
    }

}

package de.lars.Importants.listener.ChatManagerListenerPackage.ChatBot;


import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBotListener implements Listener {

    private final Importants plugin;
    private final FileConfiguration config;
    private final Map<String, Map<String, Long>> cooldowns;

    public ChatBotListener(Importants plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws InterruptedException {
        String message = event.getMessage();
        if (config.contains("ChatBot." + message)) {
            List<String> responses = config.getStringList("ChatBot." + message + ".response");
            if (!responses.isEmpty()) {
                String response = responses.get(0); // take the first response
                int cooldown = config.getInt("ChatBot." + message + ".cooldown");
                String permission = config.getString("ChatBot." + message + ".permission");
                Player player = event.getPlayer();

                Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(player.getName(), k -> new HashMap<>());
                long lastUse = playerCooldowns.getOrDefault(message, 0L);
                long now = System.currentTimeMillis();

                if (now - lastUse >= cooldown * 1000L) {
                    if (permission == null || player.hasPermission(permission)) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("chatbotprefix") + " " + response).replace("%player%", player.getName()));
                            playerCooldowns.put(message, now);
                        }, config.getInt("chatbot_answer_time")* 20);
                    } else {
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("no_permission")
                                .orElse("§cYou have no authorization to do so."));                    }
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("chatbot_cooldown")
                            .orElse("§cPlease wait %cooldown% seconds before using %message% again.").replace("%cooldown%", String.valueOf(cooldown- (now - lastUse) / 1000L)).replace("%messae%", message));
                }
            }
        }
    }
}
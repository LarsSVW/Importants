package de.lars.Importants.listener.ChatManagerListenerPackage;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.List;

public class LinkBlocker implements Listener {
    private final Importants plugin;

    private final List<String> whitelist;

    public LinkBlocker(FileConfiguration config, Importants plugin)
    {
        this.plugin = plugin;
        this.whitelist = config.getStringList("Link-Whitelist");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] words = message.split(" ");

        for (String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                if (!isOnWhitelist(word)) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("link_not_allowed")
                            .orElse("§cYoure not allowed to post links."));                       return;
                }
            } else if (word.contains(".de") || word.contains(".net") || word.contains(".eu") || word.contains(".org") || word.contains(".at") || word.contains(".ch") ||
                    word.contains(".tk") || word.contains(".bl") || word.contains(".com"))
                if (!isOnWhitelist(word)) {
                    event.setCancelled(true);
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("link_not_allowed")
                            .orElse("§cYoure not allowed to post links."));
                    return;
                }
        }
    }






    private boolean isOnWhitelist(String link) {
        for (String whitelistedLink : whitelist) {
            if (link.startsWith(whitelistedLink)) {
                return true;
            }
        }
        return false;
    }
}
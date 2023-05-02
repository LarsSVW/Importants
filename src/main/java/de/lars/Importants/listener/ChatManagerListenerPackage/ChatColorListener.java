package de.lars.Importants.listener.ChatManagerListenerPackage;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatColorListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("Importants.color")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }

}
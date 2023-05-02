package de.lars.Importants.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColorListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("Importants.Colorchat")) {
            for (int i = 0; i < 4; i++) {
                String line = event.getLine(i);
                if (line != null) {
                    event.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
                }
            }
        }
    }
}

package de.lars.Importants.listener;

import de.lars.Importants.Economy.EconomyService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerJoinListener  implements Listener {
    private final EconomyService economyService;

    public PlayerJoinListener(EconomyService economyService){
        this.economyService = economyService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        String playername = event.getPlayer().getName();
        economyService.createPlayerAccount(playername, event.getPlayer().getUniqueId().toString());
    }
}

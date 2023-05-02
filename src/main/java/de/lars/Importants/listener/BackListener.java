package de.lars.Importants.listener;

import de.lars.Importants.Commands.Teleports.BackCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackListener implements Listener {

    private final BackCommand backCommand;


    public BackListener(BackCommand backCommand) {
        this.backCommand = backCommand;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        this.backCommand.setLastLocation(player, event.getFrom());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.backCommand.setLastLocation(player, player.getLocation());
    }
}


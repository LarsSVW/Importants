package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final Importants plugin;

    public DeathListener(Importants plugin){
        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null){
            double health = killer.getHealth();
            double maxhealth = killer.getMaxHealth();
            double hearts = health/maxhealth * 20.0 /2;
            String message = String.format("%s hat dich getötet und hatte noch %.1f Herzen übrig.", killer.getName(), hearts);
            player.sendMessage(message);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("death_listener")
                    .orElse("§a%player% killed you and got %hearts% left.").replace("%player%", killer.getName()).replace("%hearts%",String.valueOf(hearts)));
        }
    }
}

package de.lars.Importants.Commands.Spawn;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class SpawnListener implements Listener {
        private final Importants plugin;

        public SpawnListener(Importants plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            if (player.hasPlayedBefore()) {
                if (plugin.getConfig().getBoolean("OnJoin")) {
                    player.performCommand("spawn");
                }
            } else {
                if (plugin.getConfig().getBoolean("OnFirstJoinSpawn")) {
                    player.performCommand("spawn");
                }
            }
        }
    }
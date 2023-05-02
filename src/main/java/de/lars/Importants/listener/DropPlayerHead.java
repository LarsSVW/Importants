package de.lars.Importants.listener;

import de.lars.Importants.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class DropPlayerHead implements Listener {
    private Random random;
    private Configuration config;
    private double dropChance;
    public DropPlayerHead(Configuration config){
        this.config = config;
        dropChance = config.getDouble("drop-chance", 0.5);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Check if the killed entity was a player and the killer was also a player
        if (config.getBoolean("drop_player_head", true)) {
            if (event.getEntityType() == EntityType.PLAYER && event.getEntity().getKiller() instanceof Player) {
                Player killed = (Player) event.getEntity();
                Player killer = (Player) event.getEntity().getKiller();
                if (random.nextDouble() < dropChance) {
                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                    skullMeta.setOwningPlayer(killed);
                    skull.setItemMeta(skullMeta);
                    killed.getWorld().dropItemNaturally(killed.getLocation(), skull);
                    if (config.getBoolean("send-killer-msg", true)) {
                        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("killed_player").replace("%player%", killed.getName())));
                        if (config.getBoolean("send-killed-msg", true)) {
                            killed.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("got_killed").replace("%player%", killer.getName())));
                        }
                    } else if (config.getBoolean("send-killed-msg", true)) {
                        killed.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("got_killed").replace("%player%", killer.getName())));
                    }

                }
            }
        }
    }
}

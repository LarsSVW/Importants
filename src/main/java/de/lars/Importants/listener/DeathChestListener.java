package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.*;

public class DeathChestListener  implements Listener {
    private boolean enableDeathChest;
    private int maxItemsPerChest;

    private final Configuration config;
    private final Importants plugin;

    public DeathChestListener(Configuration config, Importants plugin) {
        this.plugin = plugin;
        this.config = config;

            enableDeathChest = config.getBoolean("deathchest_enable", true);
            maxItemsPerChest = config.getInt("max_items_per_chest", 27);
        }
        @EventHandler
        public void onPlayerDeath (PlayerDeathEvent event){
            if (!enableDeathChest) {
                return;
            }
            Player player = event.getEntity();
            Location loc = player.getLocation();

            // Create chest at death location
            int numItems = player.getInventory().getSize();
            int numChests = (int) Math.ceil((double) numItems / maxItemsPerChest);
            ItemStack[] items = player.getInventory().getContents();
            for (int i = 0; i < numChests; i++) {
                loc.getBlock().setType(Material.CHEST);
                Inventory chestInv = ((org.bukkit.block.Chest) loc.getBlock().getState()).getInventory();
                for (int j = i * maxItemsPerChest; j < (i + 1) * maxItemsPerChest && j < numItems; j++) {
                    ItemStack item = items[j];
                    if (item != null) {
                        chestInv.addItem(item);
                    }
                }
                loc = loc.add(0, 0, 1); // Move to next block to avoid overlapping chests
            }

            // Clear player's inventory
            player.getInventory().clear();

            // Notify player of death chest
            ItemStack deathChest = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = deathChest.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Death Chest");
            deathChest.setItemMeta(meta);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("deathchest_on_death")
                    .orElse("§cYou have died! Your items have been placed in a Death chest at %x% %y% %z%")
                    .replace("%x",String.valueOf((int) loc.getX())).replace("%y%", String.valueOf((int) loc.getY())).replace("%z%", String.valueOf((int) loc.getZ())));
            player.sendMessage(ChatColor.RED + "You have died! Your items have been placed in a Death Chest at" +
                            (int) loc.getX() + " " +
                            (int) loc.getY() + " " +
                            (int) loc.getZ());
            player.getInventory().addItem(deathChest);

            // Cancel item drop
            event.getDrops().clear();
        }
    }

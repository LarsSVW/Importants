package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerListener implements Listener {
    private Importants plugin;

    public SpawnerListener(Importants plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block.getType().equals(Material.SPAWNER) && player.hasPermission("Importants.dropSpawners.drop")) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
            ItemMeta spawnerMeta = spawnerItem.getItemMeta();
            spawnerMeta.setDisplayName(spawner.getSpawnedType().name() + " Spawner");
            spawnerItem.setItemMeta(spawnerMeta);
            spawnerItem.setDurability(spawner.getSpawnedType().getTypeId());
            block.getWorld().dropItemNaturally(block.getLocation(), spawnerItem);
        }
    }

    @EventHandler
    public void onSpawnerPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        if (block.getType().equals(Material.SPAWNER) && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && player.hasPermission("spawners.place")) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            String displayName = item.getItemMeta().getDisplayName();
            String typeName = displayName.substring(0, displayName.indexOf(" Spawner"));
            spawner.setSpawnedType(org.bukkit.entity.EntityType.valueOf(typeName));
            spawner.update();
        }
    }

}

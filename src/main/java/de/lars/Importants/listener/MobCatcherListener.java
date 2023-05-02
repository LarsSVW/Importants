package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MobCatcherListener implements Listener {

    private HashMap<String, ItemStack> caughtMobs = new HashMap<>();
    private HashMap<String, Boolean> hasMobInside = new HashMap<>();

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.VILLAGER_SPAWN_EGG && item.getItemMeta().getDisplayName().equals("Mob Catcher") && !hasMobInside.getOrDefault(player.getUniqueId().toString(), false)) {
            Entity entity = event.getRightClicked();
            if (entity instanceof Mob) {
                Mob mob = (Mob) entity;
                EntityType mobType = mob.getType();
                ItemStack newEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG, 1);
                ItemMeta eggMeta = newEgg.getItemMeta();
                eggMeta.setDisplayName(mobType.toString() + " Catched");
                eggMeta.setCustomModelData(mobType.ordinal());
                eggMeta.setUnbreakable(true);
                eggMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                newEgg.setItemMeta(eggMeta);
                player.getInventory().setItemInMainHand(newEgg);
                entity.remove();
                caughtMobs.put(player.getUniqueId().toString(), newEgg);
                hasMobInside.put(player.getUniqueId().toString(), true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.VILLAGER_SPAWN_EGG) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getDisplayName().equalsIgnoreCase("Mob Catcher")) {
                event.setCancelled(true);
            }
            if (itemMeta.hasCustomModelData()) {
                int mobTypeId = itemMeta.getCustomModelData();
                EntityType mobType = EntityType.values()[mobTypeId];
                if (mobType != null && hasMobInside.getOrDefault(player.getUniqueId().toString(), false)) {
                    event.setCancelled(true);
                    Location location = event.getClickedBlock().getLocation().add(0, 1, 0);
                    World world = location.getWorld();
                    Entity entity = world.spawnEntity(location, mobType);
                    entity.setGravity(true);
                    ItemStack newEgg = new ItemStack(Material.VILLAGER_SPAWN_EGG, 1);
                    ItemMeta eggMeta = newEgg.getItemMeta();
                    eggMeta.setDisplayName("Mob Catcher");
                    eggMeta.setUnbreakable(true);
                    eggMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    newEgg.setItemMeta(eggMeta);
                    entity.setCustomName(mobType.toString());
                    entity.setCustomNameVisible(true);
                    event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                    player.getInventory().removeItem(item);
                    if (caughtMobs.containsKey(player.getUniqueId().toString()) && caughtMobs.get(player.getUniqueId().toString()).equals(item)) {
                        player.getInventory().removeItem(item);
                        caughtMobs.remove(player.getUniqueId().toString());
                        ItemStack newCatcher = new ItemStack(Material.VILLAGER_SPAWN_EGG, 1);
                        ItemMeta catcherMeta = newCatcher.getItemMeta();
                        catcherMeta.setDisplayName("Mob Catcher");
                        catcherMeta.setUnbreakable(true);
                        catcherMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        newCatcher.setItemMeta(catcherMeta);
                        player.getInventory().addItem(newCatcher);
                        hasMobInside.put(player.getUniqueId().toString(), false);
                    }
                    if (itemMeta.getDisplayName().equals("Mob Catcher")) {
                        event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        HumanEntity p = e.getWhoClicked();
        ItemStack itemclicked = e.getCurrentItem();
        if (p instanceof Player){
            if(e.getInventory().getType().equals(InventoryType.ANVIL)){
                if((itemclicked.getType().equals(Material.VILLAGER_SPAWN_EGG))){
                    if(itemclicked.getItemMeta().getDisplayName().equals("Mob Catcher")){
                        e.setCancelled(true);
                        p.closeInventory();
                    }
                }
            }
        }
    }
}
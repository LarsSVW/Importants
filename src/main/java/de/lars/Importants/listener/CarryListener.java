package de.lars.Importants.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.security.acl.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarryListener implements Listener {

    private Map<UUID, Entity> carriedEntities = new HashMap<>();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof Mob && player.hasPermission("Importants.ride")) {
            if (entity.isInsideVehicle() || entity.getPassenger() != null) {
                return;
            }
            if (player.isSneaking() && event.getHand() == EquipmentSlot.HAND) {
                player.addPassenger(entity);
                carriedEntities.put(player.getUniqueId(), entity);
            } else {
                Entity passenger = player.getPassenger();
                if (passenger != null && passenger.equals(entity)) {
                    player.eject();
                    carriedEntities.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking() && carriedEntities.containsKey(player.getUniqueId())) {
            Entity carriedEntity = carriedEntities.get(player.getUniqueId());
            carriedEntities.remove(player.getUniqueId());
            carriedEntity.leaveVehicle();
            carriedEntity.teleport(player.getLocation().add(player.getLocation().getDirection().multiply(2)));
        }
    }
}
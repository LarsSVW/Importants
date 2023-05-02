package de.lars.Importants.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class SeatListener implements Listener {
    private static SeatListener instance;

    public SeatListener() {}

    public static SeatListener getInstance() {
        if (instance == null) {
            instance = new SeatListener();
        }
        return instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) {
                return;
            }

            Material material = block.getType();
            if (material == Material.OAK_STAIRS || material == Material.ACACIA_STAIRS ||
                    material == Material.BIRCH_STAIRS || material == Material.DARK_OAK_STAIRS ||
                    material == Material.JUNGLE_STAIRS || material == Material.SPRUCE_STAIRS ||
                    material == Material.COBBLESTONE_STAIRS || material == Material.BRICK_STAIRS ||
                    material == Material.STONE_BRICK_STAIRS || material == Material.NETHER_BRICK_STAIRS ||
                    material == Material.PURPUR_STAIRS || material == Material.RED_SANDSTONE_STAIRS ||
                    material == Material.SANDSTONE_STAIRS || material == Material.QUARTZ_STAIRS ||
                    material == Material.POLISHED_BLACKSTONE_STAIRS) {
                Location location = block.getLocation();
                location.add(0.5, 0.55, 0.5);
                sitOnStair(location, event.getPlayer());
            } else if (material == Material.OAK_SLAB || material == Material.ACACIA_SLAB ||
                    material == Material.BIRCH_SLAB || material == Material.DARK_OAK_SLAB ||
                    material == Material.JUNGLE_SLAB || material == Material.SPRUCE_SLAB ||
                    material == Material.STONE_SLAB || material == Material.SMOOTH_STONE_SLAB ||
                    material == Material.PURPUR_SLAB || material == Material.RED_SANDSTONE_SLAB ||
                    material == Material.SANDSTONE_SLAB || material == Material.QUARTZ_SLAB ||
                    material == Material.POLISHED_BLACKSTONE_SLAB) {
                Location location = block.getLocation();
                if (isTopSlab(block)) {
                    location.add(0.5, 0.75, 0.5);
                } else {
                    location.add(0.5, 0.25, 0.5);
                }
                sitOnStair(location, event.getPlayer());
            }
        }
    }

    private boolean isTopSlab(Block block) {
        BlockData data = block.getBlockData();
        if (data instanceof Slab) {
            Slab slab = (Slab) data;
            return slab.getType() == Slab.Type.TOP;
        }
        return false;
    }

    public void sitOnStair(Location location, Player player) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.addPassenger(player);
    }
    private ArmorStand spawnArmorStand(Location loc) {
        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        return armorStand;
    }
    public void lieDown(Location loc, Player player) {
        ArmorStand stand = spawnArmorStand(loc);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setSmall(true);
        stand.setMarker(true);

        // Position des ArmorStands anpassen
        stand.teleport(loc.clone().add(0, -0.9, 0));

        Location playerLoc = loc.clone().add(0, -0.5, 0);
        player.teleport(playerLoc);
        player.setSleepingIgnored(true);
        player.setBedSpawnLocation(playerLoc, true);

        Vector direction = player.getLocation().getDirection();
        double yaw = Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
        if (yaw < 0) {
            yaw += 360;
        }
        float pitch = player.getLocation().getPitch() + 90;
        stand.setHeadPose(new EulerAngle(Math.toRadians(pitch), Math.toRadians(yaw), 0));
        player.setPassenger(stand);
    }

    public void crawl(Location loc, Player player) {
        ArmorStand stand = spawnArmorStand(loc);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setSmall(true);
        stand.setMarker(true);

        Location playerLoc = loc.clone().add(0, 0.1, 0);
        player.teleport(playerLoc);
        player.setSleepingIgnored(true);
        player.setBedSpawnLocation(playerLoc, true);

        Vector direction = player.getLocation().getDirection();
        double yaw = Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
        if (yaw < 0) {
            yaw += 360;
        }
        float pitch = player.getLocation().getPitch() + 90;
        stand.teleport(loc);
        stand.setHeadPose(new EulerAngle(Math.toRadians(pitch), Math.toRadians(yaw), 0));
        player.setPassenger(stand);
        player.setSneaking(true);
    }
}
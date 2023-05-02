package de.lars.Importants.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TreeCuttingListener implements Listener {

    // Define a list of all blocks considered as logs
    private static final List<Material> LOG_BLOCKS = Arrays.asList(Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG);
    private static final List<Material> AXE_BLOCKS = Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Check if player has permission and block is a log
        if (!player.hasPermission("Importants.cutting") || !LOG_BLOCKS.contains(block.getType())) {
            return;
        }

        // Check if player is sneaking, break only one block
        if (player.isSneaking()) {
            event.setCancelled(true);
            block.breakNaturally();
            return;
        }

        // Check if player is using an axe
        if (!AXE_BLOCKS.contains(player.getInventory().getItemInMainHand().getType())) {
            return;
        }

        // Find the tree and break all its logs
        Set<Block> connectedLogs = new HashSet<>();
        Deque<Block> queue = new ArrayDeque<>();
        queue.add(block);

        // Breadth-first search for all connected logs
        while (!queue.isEmpty() && connectedLogs.size() < 512) { // limit search to 512 blocks
            Block current = queue.poll();
            if (LOG_BLOCKS.contains(current.getType()) && current.getData() == block.getData() && !connectedLogs.contains(current)) {
                connectedLogs.add(current);
                queue.add(current.getRelative(1, 0, 0));
                queue.add(current.getRelative(-1, 0, 0));
                queue.add(current.getRelative(0, 1, 0));
                queue.add(current.getRelative(0, -1, 0));
                queue.add(current.getRelative(0, 0, 1));
                queue.add(current.getRelative(0, 0, -1));
            }
        }

        // Break all connected logs and deduct durability from axe
        ItemStack axe = player.getInventory().getItemInMainHand();
        for (Block connectedLog : connectedLogs) {
            connectedLog.breakNaturally(axe);
            axe.setDurability((short) (axe.getDurability() + 1));
        }
    }
}
package de.lars.Importants.listener;

import de.lars.Importants.Importants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OreMiningListener implements Listener {


    private Importants plugin; // Das Plugin-Objekt

    public OreMiningListener(Importants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Prüfen, ob der Spieler eine Spitzhacke in der Hand hat
        Material handItemType = player.getItemInHand().getType();
        switch (handItemType) {
            case DIAMOND_PICKAXE:
                mineOre(event, player, block,
                        Material.DIAMOND_ORE,
                        Material.DEEPSLATE_DIAMOND_ORE,
                        Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.GOLD_ORE,
                        Material.DEEPSLATE_GOLD_ORE,
                        Material.DEEPSLATE_EMERALD_ORE,
                        Material.EMERALD_ORE,
                        Material.REDSTONE_ORE,
                        Material.DEEPSLATE_REDSTONE_ORE,
                        Material.COPPER_ORE,
                        Material.DEEPSLATE_COPPER_ORE,
                        Material.LAPIS_ORE,
                        Material.DEEPSLATE_LAPIS_ORE);
                break;
            case IRON_PICKAXE:
                mineOre(event, player, block,
                        Material.DIAMOND_ORE,
                        Material.DEEPSLATE_DIAMOND_ORE,
                        Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.GOLD_ORE,
                        Material.DEEPSLATE_GOLD_ORE,
                        Material.DEEPSLATE_EMERALD_ORE,
                        Material.EMERALD_ORE,
                        Material.REDSTONE_ORE,
                        Material.DEEPSLATE_REDSTONE_ORE,
                        Material.COPPER_ORE,
                        Material.DEEPSLATE_COPPER_ORE,
                        Material.LAPIS_ORE,
                        Material.DEEPSLATE_LAPIS_ORE);
                break;
            case STONE_PICKAXE:
                mineOre(event, player, block,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.IRON_ORE,
                        Material.DEEPSLATE_IRON_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COPPER_ORE,
                        Material.DEEPSLATE_COPPER_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE);
                break;
            case GOLDEN_PICKAXE:
                mineOre(event, player, block,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE,
                        Material.COAL_ORE,
                        Material.DEEPSLATE_COAL_ORE);
                break;
            case WOODEN_PICKAXE:
                mineOre(event, player, block, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE);
                break;
            default:
                return;
        }
    }

    private void mineOre(BlockBreakEvent event, Player player, Block block, Material oreType, Material deepslateOreType, Material ironOre, Material deepslateIronOre, Material coalOre, Material deepslateCoalOre, Material goldOre, Material deepslateGoldOre, Material deepslateEmeraldOre, Material emeraldOre, Material redstoneOre, Material deepslateRedstoneOre, Material copperOre, Material deepslateCopperOre, Material lapisOre, Material deepslateLapisOre) {
        // Prüfen, ob der Spieler die Berechtigung hat
        if (player.hasPermission("Importants.ores.drop")) {

            // Prüfen, ob der abgebaute Block ein Erz-Block ist
            if (block.getType() == oreType || block.getType() == deepslateOreType) {

                // Alle angrenzenden Erz-Blöcke abbauen
                int durability = 0;
                for (BlockFace face : BlockFace.values()) {
                    Block adjacentBlock = block.getRelative(face);
                    if (adjacentBlock.getType() == block.getType()) {
                        adjacentBlock.breakNaturally(player.getInventory().getItemInMainHand());
                        durability++;
                    }
                }
                block.breakNaturally(player.getInventory().getItemInMainHand());
                durability++;

                // Durability der Spitzhacke abziehen
                ItemStack item = player.getInventory().getItemInMainHand();
                item.setDurability((short) (item.getDurability() + durability));


                event.setCancelled(true);
            }
        }
    }
}
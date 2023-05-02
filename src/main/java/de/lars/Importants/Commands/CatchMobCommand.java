package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CatchMobCommand implements CommandExecutor {

    private final Importants plugin;
    public CatchMobCommand(Importants plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has permission to use the command
            if (!player.hasPermission("Importants.catchmob.use")) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }

            // Give the player a Mob Catcher egg
            ItemStack egg = new ItemStack(Material.VILLAGER_SPAWN_EGG);
            ItemMeta eggMeta = egg.getItemMeta();
            eggMeta.setDisplayName(plugin.getMessageHandler()
                    .getMessage("mob_catcher_name")
                    .orElse("§cMob catcher"));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getMessageHandler()
                    .getMessage("mob_catcher_lore")
                    .orElse("§aUse this egg to catch mobs"));
            eggMeta.setLore(lore);
            egg.setItemMeta(eggMeta);
            player.getInventory().addItem(egg);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("received_mob_catcher")
                    .orElse("§aYou have received a Mob catcher egg."));
            return true;
        }

        return false;
    }
}
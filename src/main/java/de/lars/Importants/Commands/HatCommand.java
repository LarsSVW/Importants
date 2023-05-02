package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HatCommand implements CommandExecutor {
    private final Importants plugin;

    public HatCommand(Importants plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("Importants.hat")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_item_hand")
                    .orElse("§cYou have no item in your hand."));
            return true;
        }

        if (player.getInventory().getHelmet() != null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("already_hat")
                    .orElse("§cYou already got a hat! Please remove the hat before youre trying to wear a new one."));
            return true;
        }

        ItemStack hat = item.clone();
        ItemMeta meta = hat.getItemMeta();
        meta.setUnbreakable(false);
        hat.setItemMeta(meta);

        player.getInventory().setHelmet(hat);
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                + plugin.getMessageHandler()
                .getMessage("hat_wear")
                .orElse("§aItem successfully put on as hat."));

        return true;
    }
}
package de.lars.Importants.Economy.Commands;

import de.lars.Importants.Economy.EconomyService;
import de.lars.Importants.Importants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuickSellCommand implements CommandExecutor {

    private final Importants plugin;
    private final EconomyService economyService;

    public QuickSellCommand(Importants plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly Players can execute this Command"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || !(args[0].equalsIgnoreCase("Hand") || args[0].equalsIgnoreCase("Inventory"))) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("qs_inv_hand")
                    .orElse("§aPlease provide if you want to sell items out of your hand or out of your inventory!"));
            return false;
        }

        boolean sellFromHand = args[0].equalsIgnoreCase("Hand");

        ItemStack[] items = sellFromHand ? new ItemStack[] { player.getItemInHand() } : player.getInventory().getContents();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection quickSellSection = config.getConfigurationSection("Quicksell");

        double totalPrice = 0;
        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) continue;
            String materialName = item.getType().name().toUpperCase();
            if (quickSellSection.contains(materialName)) {
                int amount = item.getAmount();
                int quickSellPrice = quickSellSection.getInt(materialName + ".Price", 0);
                totalPrice += amount * quickSellPrice;
                if (sellFromHand) player.setItemInHand(new ItemStack(Material.AIR));
                else player.getInventory().removeItem(item);
            }
        }

        if (totalPrice > 0) {
            economyService.deposit(player.getUniqueId(), totalPrice);
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("qs_sold")
                    .orElse("§aYou sold Items for %price% %s%").replace("%price%", String.valueOf(totalPrice)).replace("%s%", economyService.getCurrency().getDisplayName()));
        } else {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("qs_no_sell")
                    .orElse("§aYou have no items in your inventory which you can sell."));
        }

        return true;
    }
}
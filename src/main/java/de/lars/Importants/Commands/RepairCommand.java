package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {

    private final Importants plugin;


    public RepairCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Überprüfe, ob der Sender ein Spieler ist
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));
            return true;
        }

        Player player = (Player) sender;

        // Überprüfe, ob der Spieler die Berechtigung hat, den Command auszuführen
        if (!player.hasPermission("Importants.repair")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        // Überprüfe, ob der Spieler das Item in der Hand hat und ob es repariert werden kann
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_item_in_hand")
                    .orElse("§cYou have to hold an item in your hand."));
            return true;
        }

        if (item.getDurability() == 0) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("already_repaired")
                    .orElse("§cThis Item is already repaired."));
            return true;
        }

        // Repariere das Item
        item.setDurability((short) 0);
        player.getInventory().setItemInMainHand(item);

        // Bestätige dem Spieler, dass das Item repariert wurde
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("hand_repaired")
                .orElse("§eThe Type of Item in your hand got repaired."));

        // Wenn /repair all ausgeführt wird, repariere alle Items im Inventar des Spielers
        if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
            for (ItemStack invItem : player.getInventory().getContents()) {
                if (invItem != null && invItem.getType() != Material.AIR && invItem.getDurability() > 0) {
                    invItem.setDurability((short) 0);
                }
            }
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("all_repaired")
                    .orElse("§eAll Items in your Inventory got repaired."));
        }

        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("hand");
            completions.add("all");
        }
        return completions;
    }
}
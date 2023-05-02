package de.lars.Importants.Commands.Kits;

import de.lars.Importants.Importants;
import de.lars.Importants.Managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitCreateCommand implements CommandExecutor, TabCompleter {

    private final KitManager kitManager;
    private final Importants plugin;

    public KitCreateCommand(KitManager kitManager, final Importants plugin) {
        this.kitManager = kitManager;
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

        if (!player.hasPermission("Importants.kit.create")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
        }
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /createkit [Name] [Slot/Inventory] [Command]");
            return true;
        }

        String kitName = args[0];
        String type = args[1].toLowerCase();
        ItemStack[] items;

        if (type.equals("slot")) {
            int slot = player.getInventory().getHeldItemSlot();
            ItemStack item = player.getInventory().getItem(slot);

            if (item == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_item")
                        .orElse("§cNo item in selected slot"));
                return true;
            }

            items = new ItemStack[]{item};
        } else if (type.equals("inventory")) {
            items = player.getInventory().getContents();
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_type")
                    .orElse("§cInvalid type: %type% (valid types: slot,inventory)").replace("%type%", type));
            return true;
        }

        String commandString = "";

        if (args.length >= 3) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
            commandString = builder.toString().trim();
        }
        kitManager.createKit(kitName, items, commandString);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("kit_created")
                .orElse("§aKit created: %kit%").replace("%kit%", kitName));
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("[NAME]");
        }
        if (args.length == 2) {
            completions.add("slot");
            completions.add("inventory");
        }
        if (args.length == 3) {
            completions.add("[COMMAND]");
        }
        return completions;
    }
}
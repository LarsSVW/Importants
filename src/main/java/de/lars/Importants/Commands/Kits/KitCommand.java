package de.lars.Importants.Commands.Kits;

import de.lars.Importants.Importants;
import de.lars.Importants.Managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final Importants plugin;

    public KitCommand(KitManager kitManager, final Importants plugin) {

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

        if (args.length != 1) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + " /kit [Name]");
            return true;
        }
        if (!sender.hasPermission("Importants.kit.use")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
        }

        String name = args[0];
        Kit kit = kitManager.getKit(name);

        if (kit == null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("kit_not_exist")
                    .orElse("§cThe kit %kit% does not exist.").replace("%kit%", name));
            return true;
        }

        ItemStack[] items = kit.getItems();
        for (ItemStack item : items) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }

        String commandString = kit.getCommand();
        if (!commandString.isEmpty()) {
            commandString = commandString.replace("%player%", player.getName());
            player.getServer().dispatchCommand(player.getServer().getConsoleSender(), commandString);
        }
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("kit_received")
                .orElse("§aYou received the kit %kit%.").replace("%kit%", name));
        return true;
    }
}
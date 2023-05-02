package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemRenameCommand implements CommandExecutor {
    private final Importants plugin;

    public ItemRenameCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("itemrename")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }
            if (sender.hasPermission("important.itemrename")) {


                Player player = (Player) sender;

                if (args.length == 0) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("no_itemname")
                            .orElse("§cPlease insert a new Itemname for your Item."));
                    return true;
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();

                StringBuilder nameBuilder = new StringBuilder();
                for (String arg : args) {
                    nameBuilder.append(arg).append(" ");
                }

                String name = ChatColor.translateAlternateColorCodes('&', nameBuilder.toString().trim());
                meta.setDisplayName(name);

                item.setItemMeta(meta);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("item_renamed")
                        .orElse("§aItem successfully renamed."));

                return true;
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
            }

            return false;
        }
        return false;
    }
}




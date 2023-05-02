package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadCommand implements CommandExecutor {


    private final Importants plugin;


    public PlayerHeadCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("phead")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("Importants.phead")) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (args.length == 0) {
                meta.setOwningPlayer(player);
                skull.setItemMeta(meta);
                player.getInventory().addItem(skull);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                        plugin.getMessageHandler()
                        .getMessage("own_head")
                        .orElse("§eYou got your own head."));
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    meta.setOwningPlayer(target);
                    skull.setItemMeta(meta);
                    player.getInventory().addItem(skull);
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " " +
                            plugin.getMessageHandler()
                            .getMessage("target_head")
                            .orElse("§aYou got the head of %player%").replace("%player%", target.getName()) );

                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") +" " +
                            plugin.getMessageHandler()
                            .getMessage("not_online")
                            .orElse("§cThe player is not online"));
                }
            } else {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("only_player")
                        .orElse("§cPlayer not found"));
            }
            return true;
        }
        return false;
    }
}

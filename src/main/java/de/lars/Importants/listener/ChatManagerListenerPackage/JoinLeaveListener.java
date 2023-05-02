package de.lars.Importants.listener.ChatManagerListenerPackage;

import de.lars.Importants.Importants;
import de.lars.Importants.config.Configuration;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JoinLeaveListener implements Listener {

    private final String firstJoinMessage;
    private final String joinMessage;
    private final String leaveMessage;

    private final Importants plugin;
    private final Configuration config;

    public JoinLeaveListener(Importants plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        firstJoinMessage = plugin.getMessageHandler().getMessage("firstjoin-message").orElse("§a%player% joined the Server the first time.");;
        joinMessage = plugin.getMessageHandler().getMessage("join-message").orElse("§a%player% joined the Server.");
        leaveMessage = plugin.getMessageHandler().getMessage("leave-message").orElse("§c%player% joined the Server.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<String> commands = config.getStringList("OnFirstJoin.command");
        List<String> items = config.getStringList("OnFirstJoin.item");
        if (player.hasPlayedBefore()) {
            event.setJoinMessage(joinMessage.replace("%player%", player.getName()));
        } else {
            event.setJoinMessage(firstJoinMessage.replace("%player%", player.getName()));
            for (String item : items) {
                String[] parts = item.split(" ");
                Material material = Material.getMaterial(parts[0]);
                int amount = Integer.parseInt(parts[1]);
                player.getInventory().addItem(new ItemStack(material, amount));
                for (String command : commands) {
                    String formattedCommand = command.replace("%player%", player.getName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), formattedCommand);
                    event.setJoinMessage(firstJoinMessage.replace("%player%", player.getName()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(leaveMessage.replace("%player%", player.getName()));
    }

}
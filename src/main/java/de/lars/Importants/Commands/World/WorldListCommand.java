package de.lars.Importants.Commands.World;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldListCommand implements CommandExecutor {
    private final Importants plugin;

    public WorldListCommand(Importants plugin){
        this.plugin = plugin;
    }
    private static final int WORLDS_PER_PAGE = 5;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));            return false;
        }

        Player player = (Player) sender;
        Server server = player.getServer();
        List<World> worlds = server.getWorlds();

        int maxPages = (int) Math.ceil((double) worlds.size() / WORLDS_PER_PAGE);
        int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;

        if (page < 1 || page > maxPages) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_page")
                    .orElse("§cInvalid page number"));
            return false;
        }

        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_list")
                .orElse("§a=== World List (Page %page% / %numpages%) ===").replace("%page%", String.valueOf(page)).replace("%numpages%", String.valueOf(maxPages)));
        for (int i = (page - 1) * WORLDS_PER_PAGE; i < Math.min(worlds.size(), page * WORLDS_PER_PAGE); i++) {
            player.sendMessage(ChatColor.YELLOW + "- " + worlds.get(i).getName());
        }

        return true;
    }
}
package de.lars.Importants.Commands.World;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldCreateCommand implements CommandExecutor {
    private Importants plugin;
    public WorldCreateCommand(Importants plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/world create NAME [normal|nether|end|flat|empty]");
            return false;
        }

        String worldName = args[1];
        World.Environment environment;
        switch (args[2].toLowerCase()) {
            case "normal":
                environment = World.Environment.NORMAL;
                break;
            case "nether":
                environment = World.Environment.NETHER;
                break;
            case "end":
                environment = World.Environment.THE_END;
                break;
            case "flat":
                environment = World.Environment.NORMAL;
                break;
            case "empty":
                environment = World.Environment.NORMAL;
                break;
            default:
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/world create NAME [normal|nether|end|flat|empty]");
                return false;
        }

        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(environment);
        if (args[2].equalsIgnoreCase("flat")) {
            creator.type(WorldType.FLAT);
        }
        if (environment == World.Environment.NORMAL && args.length >= 4 && args[3].equals("empty")) {
            creator.generator(new EmptyWorldGenerator());
        }

        World world = creator.createWorld();
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("wrong_usage")
                .orElse("§cWrong usage! Correct usage:") + "/world create NAME [normal|nether|end|flat|empty]");
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_created")
                .orElse("§aWorld %worldname% created successfully").replace("%worldname%", world.getName()));
        return true;
    }

}



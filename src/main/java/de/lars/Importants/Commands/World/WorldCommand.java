package de.lars.Importants.Commands.World;

import de.lars.Importants.Importants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WorldCommand implements CommandExecutor {
    private final Importants plugin;
    public WorldCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly players can execute this command."));            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/world [create|list|tp]");            return false;
        }
        if (!player.hasPermission("Importants.signedit")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return handleWorldCreate(player, args);
            case "list":
                return handleWorldList(player, args);
            case "tp":
                return handleWorldTp(player, args);
            case "delete":
                return handleWorldDelete(player, args);
            case "info":
                return handleWorldInfo(player, args);
            default:
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + "/world [create|list|tp]");
                return false;
        }
    }

    private boolean handleWorldCreate(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/world create [worldname] [normal|nether|end|flat|empty] ");
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
                player.sendMessage(ChatColor.RED + "Invalid world type. Available options: normal, nether, end, flat, empty");
                return false;
        }

        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(environment);

        if (args[2].equalsIgnoreCase("flat")) {
            creator.type(WorldType.FLAT);
            creator.generatorSettings("3;minecraft:air;127;decoration");
        } else if (args[2].equalsIgnoreCase("empty")) {
            creator.generator(new EmptyWorldGenerator());
        }

        World world = creator.createWorld();
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_created")
                .orElse("§aWorld %worldname% created successfully").replace("%worldname%", world.getName()));
        return true;
    }

    private boolean handleWorldList(Player player, String[] args) {
        int page = 1;
        if (args.length >= 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_page")
                    .orElse("§cInvalid page number."));
                return false;
            }
        }
        World[] worlds = player.getServer().getWorlds().toArray(new World[0]);
        int numPages = (worlds.length - 1) / 5 + 1;
        if (page < 1 || page > numPages) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_page")
                    .orElse("§cInvalid page number."));
            return false;
        }
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_list")
                .orElse("§a=== World List (Page %page% / %numpages%) ===").replace("%page%", String.valueOf(page)).replace("%numpages%", String.valueOf(numPages)));
        int startIndex = (page - 1) * 5;
        int endIndex = Math.min(startIndex + 5, worlds.length);
        for (int i = startIndex; i < endIndex; i++) {
            World world = worlds[i];
            player.sendMessage(ChatColor.GREEN + "- " + world.getName());
        }

        return true;
    }
    private boolean handleWorldDelete(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/world delete [worldname]");
            return false;
        }
        String worldName = args[1];
        World world = player.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("world_not_found")
                    .orElse("§cWorld not found: %worldname%").replace("%worldname%", worldName));
            return false;
        }
        player.getServer().unloadWorld(world, true);
        player.getServer().getWorlds().remove(world);
        File worldFolder = world.getWorldFolder();
        if (!deleteWorldFolder(worldFolder)) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("world_folder_not_found")
                    .orElse("§cFailed to delete world folder: %worldfolder%").replace("%worldfolder%", worldFolder.getName()));
            return false;
        }
        // Check if any players are in the world being deleted
        for (Player p : world.getPlayers()) {
            // Teleport them to the spawn of the default world
            World defaultWorld = Bukkit.getWorlds().get(0);
            p.teleport(defaultWorld.getSpawnLocation());
            p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("teleport_world_deleted")
                    .orElse("§aYou were teleported to the spawn of the default world because the world you were in was deleted."));
        }
        // Reload all worlds to update the world list
        Bukkit.getServer().reload();
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_deleted")
                .orElse("§aWorld deleted: %world%").replace("%world%", worldName));

        // Reload the plugin to make sure everything is up to date
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Importants");
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);

        return true;
    }

    private boolean deleteWorldFolder(File worldFolder) {
        if (!worldFolder.exists() || !worldFolder.isDirectory()) {
            return false;
        }
        String[] files = worldFolder.list();
        if (files == null) {
            return false;
        }
        for (String fileName : files) {
            File file = new File(worldFolder, fileName);
            if (file.isDirectory()) {
                deleteWorldFolder(file);
            } else {
                file.delete();
            }
        }
        return worldFolder.delete();
    }
    private boolean handleWorldTp(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/world tp [worldname]");
            return false;
        }
        String worldName = args[1];
        World world = player.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("world_not_found")
                    .orElse("§cWorld not found."));
            return false;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("teleported_world")
                .orElse("§aTeleported to world %world%").replace("%world%", world.getName()));
        return true;
    }
    private boolean handleWorldInfo(Player player, String[] args) {
        World world = player.getWorld();
        int numPlayers = world.getPlayers().size();
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_info_main")
                .orElse("§aWorld Info:"));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_info_name")
                .orElse("§eWorld Name: %world%").replace("%world%", world.getName()));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_info_spawn_cords")
                .orElse("§eSpawn Location: ") + " " +
                        plugin.getMessageHandler()
                        .getMessage("world_info_spawn_cords_1")
                        .orElse("§eX: %x%").replace("%x%", String.valueOf(world.getSpawnLocation().getX())) + " " +
                                        plugin.getMessageHandler()
                                                .getMessage("world_info_spawn_cords_2")
                                                .orElse("§eX: %y%").replace("%y%", String.valueOf(world.getSpawnLocation().getY())) + " " +
                                                        plugin.getMessageHandler()
                                                                .getMessage("world_info_spawn_cords_3")
                                                                .orElse("§eZ: %z%").replace("%z%", String.valueOf( world.getSpawnLocation().getZ())));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("world_info_player_cords")
                .orElse("§eYour Location: ") + " " +
                plugin.getMessageHandler()
                        .getMessage("world_info_player_cords_1")
                        .orElse("§eX: %x%").replace("%x%", String.valueOf((int)player.getLocation().getX())) + " " +
                plugin.getMessageHandler()
                        .getMessage("world_info_player_cords_2")
                        .orElse("§eX: %y%").replace("%y%", String.valueOf((int)player.getLocation().getY())) + " " +
                plugin.getMessageHandler()
                        .getMessage("world_info_player_cords_3")
                        .orElse("§eZ: %z%").replace("%z%", String.valueOf((int) player.getLocation().getZ())));
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("number_player")
                .orElse("§eNumber of Players: %num%").replace("%num%",String.valueOf(numPlayers)));
        return true;
    }

}
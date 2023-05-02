package de.lars.Importants.Commands.Shortcuts;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    private final Importants plugin;

    public GamemodeCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("gm")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly players can execute this command."));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("importants.gamemode")) {
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
                return true;
            }

            if (args.length < 1 || args.length > 2) { //prüft ob der command ein oder zwei Argumente hat
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") +  " /gm [GameMode] [PlayerName]");

                return true;
            }

            int gm = Integer.parseInt(args[0]);

            if (args.length == 1) {
                switch (gm) {
                    case 0:
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("survival_mode")
                                .orElse("§aYou are now in the survival mode"));
                        break;

                    case 1:
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("creative_mode")
                            .orElse("§aYou are now in the creative mode"));
                        break;

                    case 2:
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("adventure_mode")
                                .orElse("§aYou are now in the adventure mode"));
                        break;

                    case 3:
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("spectator_mode")
                                .orElse("§aYou are now in the spectator mode"));
                        break;

                    default:
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("invalid_gamemode")
                                .orElse("§aInvalid Gamemode! Please us a number between 0 and 3"));
                        break;
                }
            } else { //wenn zwei Argumente vorhanden sind, ändere den Gamemode des angegebenen Spielers
                if (!player.hasPermission("importants.gamemode.others")) { //prüft, ob der Spieler die Berechtigung hat, den Gamemode anderer Spieler zu ändern
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("no_permission")
                            .orElse("§cYou have no authorization to do so."));
                    return true;
                }

                Player target = sender.getServer().getPlayer(args[1]); //bekommt den Spieler mit dem angegebenen Namen

                if (target == null) { //wenn der Spieler nicht online ist, gebe eine Fehlermeldung aus
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("not_online")
                            .orElse("§cThe player is not online."));
                    return true;
                }

                switch (gm) { //überprüft welcher Gamemode gewählt wurde und wechselt entsprechend
                    case 0:
                        target.setGameMode(GameMode.SURVIVAL);
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("survival_mode")
                                .orElse("§aYou are now in the survival mode"));
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("survival_mode_other")
                                .orElse("§a%player% is now in the survival mode.").replace("%player%", target.getName()));
                        break;

                    case 1:
                        target.setGameMode(GameMode.CREATIVE);
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("creative_mode")
                                .orElse("§aYou are now in the creative mode"));
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("creative_mode_other")
                                .orElse("§a%player% is now in the creative mode.").replace("%player%", target.getName()));
                        break;

                    case 2:
                        target.setGameMode(GameMode.ADVENTURE);
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("adventure_mode")
                                .orElse("§aYou are now in the adventure mode"));
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("adventure_mode_other")
                                .orElse("§a%player% is now in the adventure mode.").replace("%player%", target.getName()));
                        break;

                    case 3:
                        target.setGameMode(GameMode.SPECTATOR);
                        target.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("spectator_mode")
                                .orElse("§aYou are now in the spectator mode"));
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("spectator_mode_other")
                                .orElse("§a%player% is now in the spectator mode.").replace("%player%", target.getName()));
                        break;

                    default:
                        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                                + plugin.getMessageHandler()
                                .getMessage("invalid_gamemode")
                                .orElse("§aInvalid Gamemode! Please us a number between 0 and 3"));
                        break;
                }

                return true;
            }

            return false;
        }
        return false;
    }
}
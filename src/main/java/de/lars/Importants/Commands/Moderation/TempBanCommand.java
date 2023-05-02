package de.lars.Importants.Commands.Moderation;

import de.lars.Importants.Importants;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TempBanCommand implements CommandExecutor, TabCompleter {
    private final Importants plugin;

    public TempBanCommand(final Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("Importants.ban")) {
            if (args.length < 3) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("wrong_usage")
                        .orElse("§cWrong usage! Correct usage:") + " /tempban [Player] [Time] [Unit] [Reason]");
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("unit_options")
                        .orElse("§cUnit options: s, m, h, d, w, mo"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("not_online")
                        .orElse("§cPlayer not found"));
                return true;
            }
            long duration = Long.parseLong(args[1]);
            TimeUnit unit;
            switch (args[2].toLowerCase()) {
                case "s":
                    unit = TimeUnit.SECONDS;
                    break;
                case "m":
                    unit = TimeUnit.MINUTES;
                    break;
                case "h":
                    unit = TimeUnit.HOURS;
                    break;
                case "d":
                    unit = TimeUnit.DAYS;
                    break;
                case "w":
                    unit = TimeUnit.DAYS;
                    duration *= 7;
                    break;
                case "mo":
                    unit = TimeUnit.DAYS;
                    duration *= 30;
                    break;
                default:
                    sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("invalid_unit")
                            .orElse("§cThis unit is invalid!"));
                    return true;
            }
            long expiration = System.currentTimeMillis() + unit.toMillis(duration);
            String reason = args.length > 3 ? String.join(" ", Arrays.copyOfRange(args, 3, args.length)) : "No reason specified";
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, new Date(expiration), sender.getName());
            target.kickPlayer(ChatColor.RED + plugin.getMessageHandler().getMessage("tempban_msg").orElse("You have been temporary banned from this server for %reason%").replace("%reason%", reason) + " " +
                    ChatColor.RED + plugin.getMessageHandler().getMessage("ban_expires").orElse("You ban expires in ") + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(expiration)) + " ");
        } else {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
        }
        return false;
    }
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
            List<String> completions = new ArrayList<>();
            if (args.length == 3) {
                completions.add("s");
                completions.add("m");
                completions.add("h");
                completions.add("d");
                completions.add("w");
                completions.add("mo");
            }
            return completions;
        }
    }
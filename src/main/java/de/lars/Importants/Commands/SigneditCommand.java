package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SigneditCommand implements CommandExecutor {
    private final Importants plugin;
    public SigneditCommand(final Importants plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("Importants.signedit")) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "signedit [Line] [Text]");
            return true;
        }

        Block targetBlock = player.getTargetBlockExact(5);
        if (!(targetBlock.getState() instanceof Sign)) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_sign")
                    .orElse("§cYoure not looking at a sign!"));
            return true;
        }

        Sign sign = (Sign) targetBlock.getState();
        int line;

        try {
            line = Integer.parseInt(args[0]) - 1;
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_line")
                    .orElse("§cYou provided a wrong line amount."));
            return true;
        }

        if (line < 0 || line > 3) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("line_amount")
                    .orElse("§cYou provided a wrong line amount."));
            return true;
        }

        String text = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        sign.setLine(line, text);
        sign.update();
        player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("sign_updated")
                .orElse("§aSign successfully updated!"));
        return true;
    }
}

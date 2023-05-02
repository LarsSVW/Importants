package de.lars.Importants.Economy.Commands;

import de.lars.Importants.Economy.EconomyService;
import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EconomyCommand implements CommandExecutor {

    private final EconomyService economyService;
    private final Importants plugin;

    public EconomyCommand(EconomyService economyService, Importants plugin)
    {
        this.plugin = plugin;
        this.economyService = economyService;
    }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("not_a_player")
                        .orElse("§cOnly Players can execute this Command"));                        return true;
        }
            if (!sender.hasPermission("Importants.economy")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
            }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:")+ " /economy [deposit|withdraw|balance|transfer|set] [amount] [Playername]");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_amount")
                    .orElse("§cInvalid amount please provide a correct amount."));               return true;
        }

        switch (args[0].toLowerCase()) {
            case "deposit":
                economyService.deposit(player.getUniqueId(), amount);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("economy_deposited")
                        .orElse("§aYou deposited %symbol% %amount% from the Server Bank account to your account.").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%", String.valueOf(amount)));
                break;
            case "withdraw":
                if (economyService.withdraw(player.getUniqueId(), amount)) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("economy_withdrew")
                            .orElse("§aYou withdrew %symbol% %amount% from your account.").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%", String.valueOf(amount)));
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("invalid_funds")
                            .orElse("§cYou don't have enough funds in your account to withdraw that amount."));
                }
                break;
            case "balance":
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("current_balance")
                        .orElse("§aYour current balance is %symbol% %amount%").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%",String.valueOf(economyService.getBalance(player.getUniqueId()))));
                break;
            case "transfer":
                if (args.length < 3) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("wrong_usage")
                            .orElse("§acWrong usage! Correct usage:")+ " /economy transfer [amount] [playername]");
                    return true;
                }
                Player targetPlayer2 = Bukkit.getPlayer(args[2]);
                if (targetPlayer2 == null) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("not_online")
                            .orElse("§cThe player is not Online."));
                    return true;
                }
                UUID targetPlayerId = targetPlayer2.getUniqueId();
                if (economyService.transfer(player.getUniqueId(), targetPlayerId, amount)) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("economy_payed")
                            .orElse("§aPayed %symbol% %amount% to %player%").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%",String.valueOf(amount)).replace("%player%", args[2]));
                } else {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("invalid_funds")
                            .orElse("§cYou don't have enough funds in your account to withdraw that amount."));                }
                break;
            case "set":
                if (args.length < 3) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                            + plugin.getMessageHandler()
                            .getMessage("wrong_usage")
                            .orElse("§acWrong usage! Correct usage:")+ " /economy set [player] [amount]");
                    return true;
                }
                double newAmount;
                try {
                    newAmount = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("invalid_amount")
                            .orElse("§cInvalid amount please provide a correct amount."));
                    return true;
                }
                Player targetPlayer = Bukkit.getPlayer(args[2]);
                if (targetPlayer == null) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + plugin.getMessageHandler()
                            .getMessage("not_online")
                            .orElse("§cThe player is not Online."));
                    return true;
                }
                economyService.setBalance(targetPlayer.getUniqueId(), newAmount);
                player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("economy_balance_set")
                        .orElse("§aSet balance of %player% to %symbol% %amount%").replace("%player%", targetPlayer.getName()).replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%", String.valueOf(newAmount)));
                return true;
        }
        return false;
    }
}

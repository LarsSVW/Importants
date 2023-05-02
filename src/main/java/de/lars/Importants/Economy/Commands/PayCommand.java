package de.lars.Importants.Economy.Commands;

import de.lars.Importants.Economy.EconomyService;
import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PayCommand implements CommandExecutor {
    private final EconomyService economyService;
    private final Importants plugin;

    public PayCommand(EconomyService economyService, Importants plugin)
    {
        this.plugin = plugin;
        this.economyService = economyService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("not_a_player")
                    .orElse("§cOnly Players can execute this Command"));               return true;
        }

        Player senderPlayer = (Player) sender;
        if (args.length != 2) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:")+ " /pay [Playername] [amount]");
            return true;
        }

        Player targetPlayer = senderPlayer.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("not_online")
                    .orElse("§cThe player is not Online."));            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_amount")
                    .orElse("§cInvalid amount please provide a correct amount."));                return true;
        }

        if (amount <= 0) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_amount")
                    .orElse("§cInvalid amount please provide a correct amount."));               return true;
        }

        if (senderPlayer.equals(targetPlayer)) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("economy_not_to_yourself")
                    .orElse("§cYou cant transfer money to yourself."));               return true;
        }

        if (!economyService.hasEnough(senderPlayer.getUniqueId(), amount)) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("invalid_funds")
                    .orElse("§cYou don't have enough funds in your account to do this."));
            return true;
        }

        double maxAmount = economyService.getBalance(senderPlayer.getUniqueId());
        if (amount > maxAmount) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("economy_max_amount")
                    .orElse("§cYou only can transfer upto %amount%.").replace("%amount%", String.valueOf(maxAmount)));
            return true;
        }

        if (economyService.transfer(senderPlayer.getUniqueId(), targetPlayer.getUniqueId(), amount)) {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("economy_payed")
                    .orElse("§aPayed %symbol% %amount% to %player%").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%",String.valueOf(amount)).replace("%player%", targetPlayer.getName()));
            targetPlayer.sendMessage(senderPlayer.getName() + " hat dir " + amount + " € überwiesen.");
            targetPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("economy_pay_received")
                    .orElse("§a%player% payed you %symbol% %amount%").replace("%player%", senderPlayer.getName()).replace("amount", String.valueOf(amount)).replace("%symbol%", economyService.getCurrency().getSymbol()));
            return true;
        } else {
            senderPlayer.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                    + plugin.getMessageHandler()
                    .getMessage("transaction_failed")
                    .orElse("§cTransaction failed!"));
            return true;
        }
    }
}
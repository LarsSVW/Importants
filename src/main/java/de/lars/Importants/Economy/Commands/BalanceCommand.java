package de.lars.Importants.Economy.Commands;

import de.lars.Importants.Commands.PlayerInfoCommand;
import de.lars.Importants.Economy.EconomyService;
import de.lars.Importants.Importants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
      private final EconomyService economyService;
      private final Importants plugin;

      public BalanceCommand(EconomyService economyService, Importants plugin) {
            this.plugin = plugin;
            this.economyService = economyService;
      }

      @Override
      public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                  sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                          + plugin.getMessageHandler()
                          .getMessage("not_a_player")
                          .orElse("§cOnly Players can execute this Command"));
                  return true;
            }

            if (args.length == 0) {
                  Player player = (Player) sender;
                  player.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                          + plugin.getMessageHandler()
                          .getMessage("current_balance")
                          .orElse("§aYour current balance is %symbol% %amount%").replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%",String.valueOf(economyService.getBalance(player.getUniqueId()))));
                  return true;
            } else if (args.length == 1) {
                  Player target = sender.getServer().getPlayer(args[0]);
                  if (target == null) {
                        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                                + plugin.getMessageHandler()
                                .getMessage("not_online")
                                .orElse("§cThe player is not Online."));
                        return true;
                  }
                  sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                          + plugin.getMessageHandler()
                          .getMessage("players_current_balance")
                          .orElse("§a%player%´s current balance is %symbol% %amount%").replace("%player%", target.getName()).replace("%symbol%", economyService.getCurrency().getSymbol()).replace("%amount%",String.valueOf(economyService.getBalance(target.getUniqueId()))));
                  return true;
            } else {
                  sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                          + plugin.getMessageHandler()
                          .getMessage("wrong_usage")
                          .orElse("§cWrong usage! Correct usage:")+ " /balance PLAYERNAME");
                  return true;
            }
      }
}
package de.lars.Importants.Commands.Kits;

import de.lars.Importants.Importants;
import de.lars.Importants.Managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

    public class KitDeleteCommand implements CommandExecutor {

        private final KitManager kitManager;
        private final Importants plugin;

        public KitDeleteCommand(KitManager kitManager, final Importants plugin) {
            this.kitManager = kitManager;
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 1) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("provide_del_name")
                        .orElse("§cPlease provide the name of the kit you want to delete"));
                return false;
            }
            if (!sender.hasPermission("Importants.kit.delete")) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                        + plugin.getMessageHandler()
                        .getMessage("no_permission")
                        .orElse("§cYou have no authorization to do so."));
            }

            String name = args[0];
            if (kitManager.getKit(name) == null) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("kit_not_exist")
                        .orElse("§cThe kit %kit% does not exist.").replace("%kit%", name));
                return false;
            }

            kitManager.deleteKit(name);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("kit_deleted")
                    .orElse("§cYou deleted kit %kit%.").replace("%kit%", name));
            return true;
        }
    }
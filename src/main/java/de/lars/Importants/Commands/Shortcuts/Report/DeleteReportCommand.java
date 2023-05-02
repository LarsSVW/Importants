package de.lars.Importants.Commands.Shortcuts.Report;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteReportCommand implements CommandExecutor {
    private final ReportManager reportManager;
    private final Importants plugin;


    public DeleteReportCommand(ReportManager reportManager, Importants plugin)
    {
        this.plugin = plugin;
        this.reportManager = reportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("Importants.reports.delete")) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/report delete [report_number] ");
            return true;
        }
        try {
            int reportNum = Integer.parseInt(args[0]);
            if (reportManager.deleteReport(reportNum)) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("report_deleted")
                        .orElse("§aReport #%reportnum% has been deleted").replace("%reportnum%", String.valueOf(reportNum)));
            } else {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("report_not_exist")
                        .orElse("§cReport #%reportnum% does not exist").replace("%reportnum%", String.valueOf(reportNum)));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("report_invalid_num")
                    .orElse("§cInvalid report number."));
            sender.sendMessage(ChatColor.RED + "Invalid report number.");
        }
        return true;
    }
}
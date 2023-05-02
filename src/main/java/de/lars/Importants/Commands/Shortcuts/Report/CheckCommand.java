package de.lars.Importants.Commands.Shortcuts.Report;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CheckCommand implements CommandExecutor {
    private static final String PERMISSION_CHECK = "Importants.reports.check";
    private final Importants plugin;
    private final ReportManager reportManager;

    public CheckCommand(ReportManager reportManager, Importants plugin) {
        this.plugin = plugin;
        this.reportManager = reportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION_CHECK)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        List<Report> reports = reportManager.getReports();

        if (reports.isEmpty()) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("no_reports")
                .orElse("§cThere are no reports."));
            return true;
        }

        int reportsPerPage = 5;
        int maxPages = (reports.size() - 1) / reportsPerPage + 1;

        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1 || page > maxPages) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("invalid_page")
                        .orElse("§cInvalid page number."));
                return true;
            }
        }

        int startIndex = (page - 1) * reportsPerPage;
        int endIndex = Math.min(startIndex + reportsPerPage, reports.size());

        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("report_list")
                .orElse("§aReport List (page %page% of %maxpage%) :").replace("%page%", String.valueOf(page)).replace("%maxpage%", String.valueOf(maxPages)));

        for (int i = startIndex; i < endIndex; i++) {
            Report report = reports.get(i);
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("reports")
                    .orElse("§aReport # %i%: %player% -  %reason%").replace("%i%", String.valueOf(i)).replace("%player%", Bukkit.getOfflinePlayer(report.getReportedPlayerId()).getName()).replace("%reason%", report.getReason()));
        }

        return true;
    }
}
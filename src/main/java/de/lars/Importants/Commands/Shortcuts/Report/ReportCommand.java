package de.lars.Importants.Commands.Shortcuts.Report;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private final ReportManager reportManager;
    private final String PERMISSION_REPORT = "Importants.reports.report";
    private final String PERMISSION_CHECK = "Importants.reports.check";
    private final String PERMISSION_DELETE = "Importants.reports.delete";
    private final Importants plugin;
    private final HashMap<UUID, Report> reportedPlayers = new HashMap<>();
    private int reportCounter = 0;

    public ReportCommand(ReportManager reportManager, Importants plugin)
    {
        this.plugin = plugin;
        this.reportManager = reportManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("wrong_usage")
                    .orElse("§cWrong usage! Correct usage:") + "/report [player] [reason] ");
            return true;
        }

        if (!sender.hasPermission(PERMISSION_REPORT)) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("no_permission")
                    .orElse("§cYou have no authorization to do so."));
            return true;
        }

        OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(args[0]);
        if (reportedPlayer == null || !reportedPlayer.hasPlayedBefore()) {
            sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                    + plugin.getMessageHandler()
                    .getMessage("not_online")
                    .orElse("§cPlayer not found"));
            return true;
        }

        String reason = String.join(" ", args).replaceFirst(args[0], "").trim();
        Report newReport = new Report(reportedPlayer.getUniqueId(), sender instanceof Player ? ((Player) sender).getUniqueId() : null, reason);
        reportManager.addReport(newReport);
        sender.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                + plugin.getMessageHandler()
                .getMessage("report_submitted")
                .orElse("§aReport submitted successfully! Thank you for keeping the Server save."));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(PERMISSION_CHECK)) {
                p.sendMessage(plugin.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]")+ " "
                        + plugin.getMessageHandler()
                        .getMessage("new_report")
                        .orElse("§aA new report has been submitted. Use /report check to view it."));
            }
        }
        return true;
    }
}
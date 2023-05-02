package de.lars.Importants.Commands.Shortcuts.Report;

import de.lars.Importants.Importants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportManager {
    //private Map<UUID, Report> reportedPlayers = new HashMap<>();
    private int reportCounter = 0;
    private List<Report> reports = new ArrayList<>();
    private final File reportsFile;

    private final Importants plugin;


    public ReportManager(Importants plugin) {
        this.plugin = plugin;
        this.reportsFile = new File(plugin.getDataFolder(), "reports.yml");
        loadReports();
    }

    public void addReport(Report report) {
        //reportedPlayers.put(report.getReportedPlayerId(), report);
        //addReportToList(report);
        reports.add(report);
        saveReports();
    }

    public Report getReport(UUID playerId) {
        return reports.stream().filter(report -> report.getReportedPlayerId().equals(playerId)).findFirst().orElse(null);
        //return reportedPlayers.get(playerId);
    }

    public List<Report> getAllReports() {
        return new ArrayList<>(reports);
    }

    public boolean deleteReport(int reportNum) {
        if (reportNum < 0 || reportNum > reports.size() - 1) {
            return false;
        }

        //Report reportToRemove = getReportByNum(reportNum);
        Report reportToRemove = reports.get(reportNum);
        if (reportToRemove == null) {
            return false;
        }

        //reportedPlayers.remove(reportToRemove.getReportedPlayerId());
        reports.remove(reportToRemove);
        saveReports();
        return true;
    }

    /*
    public Report getReportByNum(int reportNum) {
        int i = 1;
        for (Report report : reportedPlayers.values()) {
            if (i == reportNum) {
                return report;
            }
            i++;
        }
        return null;
    }
     */

    public List<Report> getReports() {
        return reports;
    }

    /*
    public void addReportToList(Report report) {
        reports.add(report);
    }

     */

    public void loadReports() {
        reports.clear();
        //reportedPlayers.clear();
        File reportFile = new File(plugin.getDataFolder(), "reports.yml");
        FileConfiguration reportConfig = new YamlConfiguration();
        try {
            reportConfig.load(reportFile);

            reportConfig.getKeys(false).forEach(key -> {
                ConfigurationSection section = reportConfig.getConfigurationSection(key);
                UUID reporter = UUID.fromString(section.getString("reporter"));
                UUID reported = UUID.fromString(section.getString("reported"));
                Report report = new Report(reporter, reported, section.getString("reason"));
                reports.add(report);
                //reportedPlayers.put(reported, report);
            });
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Failed to load reports.yml: " + exception.getMessage());
            exception.printStackTrace();
        }
    }


    public void saveReports() {
        File reportFile = new File(plugin.getDataFolder(), "reports.yml");
        FileConfiguration reportConfig = new YamlConfiguration();
        int idx = 0;
        for (Report current : reports) {
            ConfigurationSection section = reportConfig.createSection(Integer.toString(idx));
            section.set("reporter", current.getReporterId().toString());
            section.set("reported", current.getReportedPlayerId().toString());
            section.set("reason", current.getReason());
            idx += 1;
        }
        try {
            reportConfig.save(reportFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save Reports.yml: " + e.getMessage());
        }
    }
}
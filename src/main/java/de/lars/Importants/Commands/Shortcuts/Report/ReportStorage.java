package de.lars.Importants.Commands.Shortcuts.Report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lars.Importants.Importants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReportStorage {
    private final File dataFolder;

    public ReportStorage(Importants plugin) {
        this.dataFolder = plugin.getDataFolder();
    }

    public void saveReports(List<Report> reports) {
        try (Writer writer = new FileWriter(new File(dataFolder, "reports.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(reports, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Report> loadReports() {
        List<Report> reports = new ArrayList<>();
        try (Reader reader = new FileReader(new File(dataFolder, "reports.json"))) {
            Gson gson = new GsonBuilder().create();
            reports = gson.fromJson(reader, new ArrayList<Report>().getClass());
        } catch (FileNotFoundException e) {
            // ignore if file doesn't exist
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
package de.lars.Importants.Commands.Shortcuts.Report;

import java.util.UUID;

public class Report {
    private UUID reporterId;
    private UUID reportedPlayerId;
    private String reason;

    public Report(UUID reporterId, UUID reportedPlayerId, String reason) {
        this.reporterId = reporterId;
        this.reportedPlayerId = reportedPlayerId;
        this.reason = reason;
    }

    public UUID getReporterId() {
        return reporterId;
    }

    public UUID getReportedPlayerId() {
        return reportedPlayerId;
    }

    public String getReason() {
        return reason;
    }
}
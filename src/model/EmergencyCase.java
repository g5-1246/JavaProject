package model;

import java.time.LocalDateTime;

public class EmergencyCase {

    private int caseId;
    private int patientId;

    private String patientName;
    private String contact;
    private String location;
    private String severity;
    private String description;
    private String status;

    private LocalDateTime reportedAt;

    public EmergencyCase(int caseId, int patientId,
                         String patientName, String contact,
                         String location, String severity,
                         String description, String status,
                         LocalDateTime reportedAt) {

        this.caseId = caseId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.contact = contact;
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.status = status;
        this.reportedAt = reportedAt;
    }

    public int getCaseId() {
        return caseId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }
}
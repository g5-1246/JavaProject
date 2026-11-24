package service;

import model.EmergencyCase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class EmergencyService {

    private final List<EmergencyCase> cases = new ArrayList<>();

    private final Path file =
        Path.of("data", "emergencies.csv");

    public EmergencyService() {
        load();
    }

    public synchronized List<EmergencyCase> getAllCases() {
        return new ArrayList<>(cases);
    }

    public synchronized int getNextCaseId() {

        int max = 4000;

        for (EmergencyCase c : cases) {

            if (c.getCaseId() > max) {
                max = c.getCaseId();
            }
        }

        return max + 1;
    }

    public synchronized void addCase(EmergencyCase emergencyCase) {

        if (emergencyCase == null) {
            throw new IllegalArgumentException(
                "Emergency case cannot be empty."
            );
        }

        cases.add(emergencyCase);
        save();
    }

    public synchronized boolean updateStatus(
        int id, String status
    ) {

        for (int i = 0; i < cases.size(); i++) {

            EmergencyCase c = cases.get(i);

            if (c.getCaseId() == id) {

                EmergencyCase updated = new EmergencyCase(
                    c.getCaseId(),
                    c.getPatientId(),
                    c.getPatientName(),
                    c.getContact(),
                    c.getLocation(),
                    c.getSeverity(),
                    c.getDescription(),
                    status,
                    c.getReportedAt()
                );

                cases.set(i, updated);

                save();

                return true;
            }
        }

        return false;
    }

    private void save() {

        try {

            Files.createDirectories(file.getParent());

            List<String> lines = new ArrayList<>();

            lines.add(
                "caseId,patientId,patientName,contact,location," +
                "severity,description,status,reportedAt"
            );

            for (EmergencyCase c : cases) {

                String line =
                    c.getCaseId() + "," +
                    c.getPatientId() + "," +
                    CsvUtil.quote(c.getPatientName()) + "," +
                    CsvUtil.quote(c.getContact()) + "," +
                    CsvUtil.quote(c.getLocation()) + "," +
                    CsvUtil.quote(c.getSeverity()) + "," +
                    CsvUtil.quote(c.getDescription()) + "," +
                    CsvUtil.quote(c.getStatus()) + "," +
                    CsvUtil.quote(c.getReportedAt().toString());

                lines.add(line);
            }

            Files.write(
                file,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                "Unable to save emergency records.", e
            );
        }
    }

    private void load() {

        if (!Files.exists(file)) {
            return;
        }

        try {

            List<String> lines =
                Files.readAllLines(file, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {

                try {

                    List<String> v = CsvUtil.parse(lines.get(i));

                    if (v.size() != 9) {
                        continue;
                    }

                    EmergencyCase c = new EmergencyCase(
                        Integer.parseInt(v.get(0)),
                        Integer.parseInt(v.get(1)),
                        v.get(2),
                        v.get(3),
                        v.get(4),
                        v.get(5),
                        v.get(6),
                        v.get(7),
                        LocalDateTime.parse(v.get(8))
                    );

                    cases.add(c);

                } catch (RuntimeException ex) {

                    System.err.println(
                        "Skipping invalid emergency row " + (i + 1)
                    );
                }
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                "Unable to load emergency records.", e
            );
        }
    }
}
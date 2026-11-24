package service;

import model.MedicalRecord;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MedicalRecordService {

    private ArrayList<MedicalRecord> records = new ArrayList<>();

    private final String filePath = "data/medical_records.csv";

    public MedicalRecordService() {
        loadRecords();
    }

    // Add Medical Record
    public void addRecord(MedicalRecord record) {
        records.add(record);
        saveRecords();
    }

    // Get All Medical Records
    public ArrayList<MedicalRecord> getAllRecords() {
        return records;
    }

    // Find Record by ID
    public MedicalRecord findRecord(int recordId) {
        for (MedicalRecord record : records) {
            if (record.getRecordId() == recordId) {
                return record;
            }
        }
        return null;
    }

    // Delete Medical Record
    public boolean deleteRecord(int recordId) {
        MedicalRecord record = findRecord(recordId);

        if (record != null) {
            records.remove(record);
            saveRecords();
            return true;
        }

        return false;
    }

    // Update Medical Record
    public boolean updateRecord(MedicalRecord updatedRecord) {
        for (int i = 0; i < records.size(); i++) {

            if (records.get(i).getRecordId()
                    == updatedRecord.getRecordId()) {

                records.set(i, updatedRecord);
                saveRecords();
                return true;
            }
        }

        return false;
    }

    // Generate Next Record ID
    public int getNextRecordId() {
        int maxId = 4000;

        for (MedicalRecord record : records) {
            if (record.getRecordId() > maxId) {
                maxId = record.getRecordId();
            }
        }

        return maxId + 1;
    }

    // Save Records to CSV
    private void saveRecords() {
        try {
            Path path = Paths.get(filePath);

            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    path, StandardCharsets.UTF_8)) {

                for (MedicalRecord record : records) {

                    writer.write(
                            record.getRecordId() + "," +
                            record.getPatientId() + "," +
                            record.getDoctorId() + "," +
                            escape(record.getDiagnosis()) + "," +
                            escape(record.getTreatment()) + "," +
                            escape(record.getMedicines()) + "," +
                            escape(record.getRecordDate())
                    );

                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error saving medical records", e);
        }
    }

    // Handle commas and quotes in CSV values
    private String escape(String value) {
        if (value == null) {
            value = "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // Load Records from CSV
    private void loadRecords() {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                ArrayList<String> values = parseCSV(line);

                if (values.size() == 7) {

                    MedicalRecord record = new MedicalRecord(
                            Integer.parseInt(values.get(0)),
                            Integer.parseInt(values.get(1)),
                            Integer.parseInt(values.get(2)),
                            values.get(3),
                            values.get(4),
                            values.get(5),
                            values.get(6)
                    );

                    records.add(record);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException(
                    "Error loading medical records", e);
        }
    }

    // CSV Parser
    private ArrayList<String> parseCSV(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (ch == ',' && !insideQuotes) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(ch);
            }
        }

        values.add(value.toString());

        return values;
    }
}
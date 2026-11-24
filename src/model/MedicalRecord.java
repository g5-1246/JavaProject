package model;

public class MedicalRecord {

    private int recordId;
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private String treatment;
    private String medicines;
    private String recordDate;

    public MedicalRecord(int recordId, int patientId, int doctorId,
                         String diagnosis, String treatment,
                         String medicines, String recordDate) {

        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medicines = medicines;
        this.recordDate = recordDate;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getRecordDate() {
        return recordDate;
    }
}
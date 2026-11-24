package model;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String date;
    private String time;
    private String reason;
    private String status;

    public Appointment(int appointmentId, int patientId,
                       int doctorId, String date,
                       String time, String reason,
                       String status) {

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
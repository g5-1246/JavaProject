
package model;

public class Doctor {

    private int doctorId;
    private String name;
    private String specialization;
    private int experience;
    private String contact;
    private double consultationFee;

    public Doctor(int doctorId, String name, String specialization,
                  int experience, String contact, double consultationFee) {

        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.contact = contact;
        this.consultationFee = consultationFee;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public int getExperience() {
        return experience;
    }

    public String getContact() {
        return contact;
    }

    public double getConsultationFee() {
        return consultationFee;
    }
}

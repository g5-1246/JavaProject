package model;
public class Patient {

    // Patient attributes
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String address;

    // Constructor
    public Patient(int patientId, String name, int age,
                   String gender, String contact, String address) {

        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }
    
    // Getter methods

    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }
}

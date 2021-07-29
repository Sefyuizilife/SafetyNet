package com.safetynet.safetynetalerts.DTO;

import com.safetynet.safetynetalerts.entities.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicalRecordDTO {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.FRANCE);

    private String firstName;
    private String lastName;

    private LocalDate    birthdate;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecordDTO() {

    }

    public MedicalRecordDTO(Person person) {

        this.setFirstName(person.getFirstName());
        this.setLastName(person.getLastName());
        this.setBirthdate(person.getBirthDate());
        if (person.getMedicalRecord() != null) {
            this.setMedications(person.getMedicalRecord().getMedications());
            this.setAllergies(person.getMedicalRecord().getAllergies());
        }
    }

    public static List<MedicalRecordDTO> getMedicalRecordDTOs(List<Person> persons) {

        ArrayList<MedicalRecordDTO> medicalRecordDTOS = new ArrayList<>();
        for (Person person : persons) {
            medicalRecordDTOS.add(new MedicalRecordDTO(person));
        }


        return medicalRecordDTOS;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public LocalDate getBirthdate() {

        return birthdate;
    }

    public void setBirthdate(String birthdate) {

        this.birthdate = LocalDate.parse(birthdate, this.formatter);
    }

    public void setBirthdate(LocalDate birthdate) {

        this.birthdate = birthdate;
    }

    public List<String> getMedications() {

        return medications;
    }

    public void setMedications(List<String> medications) {

        this.medications = medications;
    }

    public List<String> getAllergies() {

        return allergies;
    }

    public void setAllergies(List<String> allergies) {

        this.allergies = allergies;
    }
}

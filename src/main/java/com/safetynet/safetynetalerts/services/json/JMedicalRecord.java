package com.safetynet.safetynetalerts.services.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class JMedicalRecord {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.FRANCE);

    @JsonIgnore
    private String firstName;

    @JsonIgnore
    private String lastName;

    private LocalDate    birthdate;
    private List<String> medications;
    private List<String> allergies;

    public JMedicalRecord() {

    }

    @JsonIgnore
    public String getFirstName() {

        return firstName;
    }

    @JsonIgnore
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

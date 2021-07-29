package com.safetynet.safetynetalerts.entities;

import com.safetynet.safetynetalerts.DTO.FireStationDTO;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {

    private List<String> medications = new ArrayList<>();
    private List<String> allergies   = new ArrayList<>();

    public MedicalRecord() {

    }

    public MedicalRecord(List<String> medications, List<String> allergies) {

        this.medications = medications;
        this.allergies   = allergies;
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

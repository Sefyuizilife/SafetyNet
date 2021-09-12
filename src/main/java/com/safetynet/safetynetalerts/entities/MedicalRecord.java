package com.safetynet.safetynetalerts.entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public String toString() {

        return Arrays.toString(this.getMedications().toArray()) + Arrays.toString(this.getAllergies().toArray());
    }

    public JSONObject toJson() {

        return new JSONObject() {{
            this.put("medications", new JSONArray(MedicalRecord.this.medications));
            this.put("allergies", new JSONArray(MedicalRecord.this.allergies));
        }};
    }
}

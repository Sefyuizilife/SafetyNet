package com.safetynet.safetynetalerts.services.json;

import java.util.ArrayList;
import java.util.List;

public class JSONData {

    private List<JPerson> jPersons = new ArrayList<>();

    private List<JFireStation> jFireStations = new ArrayList<>();

       private List<JMedicalRecord> jMedicalRecords = new ArrayList<>();

    public List<JPerson> getPersons() {

        return jPersons;
    }

    public void setPersons(List<JPerson> jPersons) {

        this.jPersons = jPersons;
    }

    public List<JFireStation> getFireStations() {

        return jFireStations;
    }

    public void setFireStations(List<JFireStation> JFireStations) {

        this.jFireStations = JFireStations;
    }

    public List<JMedicalRecord> getMedicalRecords() {

        return jMedicalRecords;
    }

    public void setMedicalRecords(List<JMedicalRecord> jMedicalRecords) {

        this.jMedicalRecords = jMedicalRecords;
    }

    @Override
    public String toString() {

        return "JSONDataObject [jPersons = " + jPersons + ", jFireStations = " + jFireStations + ", jMedicalRecords=" + jMedicalRecords + "]";
    }
}

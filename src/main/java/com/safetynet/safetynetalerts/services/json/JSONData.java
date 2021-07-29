package com.safetynet.safetynetalerts.services.json;

import com.safetynet.safetynetalerts.DTO.FireStationDTO;
import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.DTO.PersonDTO;

import java.util.ArrayList;
import java.util.List;

public class JSONData {

    private List<PersonDTO> personDTOS = new ArrayList<>();

    private List<FireStationDTO> fireStationDTOS = new ArrayList<>();

       private List<MedicalRecordDTO> medicalRecordDTOS = new ArrayList<>();

    public List<PersonDTO> getPersons() {

        return personDTOS;
    }

    public void setPersons(List<PersonDTO> personDTOS) {

        this.personDTOS = personDTOS;
    }

    public List<FireStationDTO> getFireStations() {

        return fireStationDTOS;
    }

    public void setFireStations(List<FireStationDTO> FireStationDTOS) {

        this.fireStationDTOS = FireStationDTOS;
    }

    public List<MedicalRecordDTO> getMedicalRecords() {

        return medicalRecordDTOS;
    }

    public void setMedicalRecords(List<MedicalRecordDTO> medicalRecordDTOS) {

        this.medicalRecordDTOS = medicalRecordDTOS;
    }

    @Override
    public String toString() {

        return "JSONDataObject [personDTOS = " + personDTOS + ", fireStationDTOS = " + fireStationDTOS + ", medicalRecordDTOS=" + medicalRecordDTOS + "]";
    }
}

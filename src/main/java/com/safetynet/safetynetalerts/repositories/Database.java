package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.DTO.FireStationDTO;
import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.DTO.PersonDTO;
import com.safetynet.safetynetalerts.services.json.JSONData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    private List<Person>      persons;
    private List<FireStation> fireStations;

    public Database(JSONData jsonData) {

        initDatabase(jsonData);
    }

    public void initDatabase(JSONData jsonData) {

        persons      = new ArrayList<>();
        fireStations = new ArrayList<>();

        LOGGER.info("Database - Initialization in progress...");

        List<PersonDTO>      personDTOS      = jsonData.getPersons();
        List<FireStationDTO> fireStationDTOS = jsonData.getFireStations();
        List<MedicalRecordDTO> jMedicalsRecord = jsonData.getMedicalRecords();

        int size = personDTOS.size();

        for (int i = 0 ; i < size ; i++) {

            this.persons.add(this.toPerson(personDTOS.get(i), jMedicalsRecord.get(i)));
        }

        fireStationDTOS.forEach(item -> fireStations.add(this.toFireStation(item)));

        LOGGER.info("Database - Successfully initialized");
    }

    public List<Person> getPersons() {

        return this.persons;
    }

    public void setPersons(List<Person> persons) {

        this.persons = persons;
    }

    public List<FireStation> getFireStations() {

        return this.fireStations;
    }

    public void setFireStations(List<FireStation> FireStations) {

        this.fireStations = FireStations;
    }

    private Person toPerson(PersonDTO personDTO, MedicalRecordDTO medicalRecordDTO) {

        Person        person        = new Person();
        MedicalRecord medicalRecord = new MedicalRecord(medicalRecordDTO.getMedications(), medicalRecordDTO.getAllergies());

        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setBirthDate(medicalRecordDTO.getBirthdate());
        person.setAddress(personDTO.getAddress());
        person.setZip(personDTO.getZip());
        person.setCity(personDTO.getCity());
        person.setEmail(personDTO.getEmail());
        person.setPhone(personDTO.getPhone());
        person.setMedicalRecord(medicalRecord);

        return person;
    }

    private FireStation toFireStation(FireStationDTO fireStationDTO) {

        FireStation fireStation = new FireStation();

        fireStation.setStation(fireStationDTO.getStation());
        fireStation.setAddress(fireStationDTO.getAddress());

        return fireStation;
    }

}

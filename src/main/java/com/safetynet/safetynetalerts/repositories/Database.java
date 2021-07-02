package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.services.json.JFireStation;
import com.safetynet.safetynetalerts.services.json.JMedicalRecord;
import com.safetynet.safetynetalerts.services.json.JPerson;
import com.safetynet.safetynetalerts.services.json.JSONData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    private List<Person> persons = new ArrayList<>();

    private List<FireStation> fireStations = new ArrayList<>();

    public Database(JSONData jsonData) {

        initDatabase(jsonData);
    }

    private void initDatabase(JSONData jsonData) {

        LOGGER.info("Database - Initialization in progress...");

        List<JPerson>        jPersons        = jsonData.getPersons();
        List<JFireStation>   jFireStations   = jsonData.getFireStations();
        List<JMedicalRecord> jMedicalsRecord = jsonData.getMedicalRecords();

        int size = jPersons.size();

        for (int i = 0 ; i < size ; i++) {

            this.persons.add(this.toPerson(jPersons.get(i), jMedicalsRecord.get(i)));
        }

        jFireStations.forEach(item -> fireStations.add(this.toFireStation(item)));

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

    private Person toPerson(JPerson jPerson, JMedicalRecord jMedicalRecord) {

        Person        person        = new Person();
        MedicalRecord medicalRecord = new MedicalRecord(jMedicalRecord.getMedications(), jMedicalRecord.getAllergies());

        person.setFirstName(jPerson.getFirstName());
        person.setLastName(jPerson.getLastName());
        person.setBirthDate(jMedicalRecord.getBirthdate());
        person.setAddress(jPerson.getAddress());
        person.setZip(jPerson.getZip());
        person.setCity(jPerson.getCity());
        person.setEmail(jPerson.getEmail());
        person.setPhone(jPerson.getPhone());
        person.setMedicalRecord(medicalRecord);

        return person;
    }

    private FireStation toFireStation(JFireStation jFireStation) {

        FireStation fireStation = new FireStation();

        fireStation.setStation(jFireStation.getStation());
        fireStation.setAddress(jFireStation.getAddress());

        return fireStation;
    }

}

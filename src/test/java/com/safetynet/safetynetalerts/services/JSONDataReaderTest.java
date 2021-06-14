package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.services.json.JSONData;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONDataReaderTest {

    @Test
    public void readJsonFile_shouldAllPersonFireStationMedicalRecordObjectsFromJSON_forAFoundFileJson() {

        JSONData jsonData = JSONDataReader.readJsonFile("src/main/resources/data.json");

        assertEquals(23, jsonData.getPersons().size());
        assertEquals(23, jsonData.getMedicalRecords().size());
        assertEquals(13, jsonData.getFireStations().size());
    }

    @Test
    public void readJsonFile_shouldABlankDatabase_forNotFoundFileJson() {

        JSONData jsonData = JSONDataReader.readJsonFile("notfound");

        assertEquals(0, jsonData.getPersons().size());
        assertEquals(0, jsonData.getMedicalRecords().size());
        assertEquals(0, jsonData.getFireStations().size());
    }

    @Test
    public void readJsonFile_shouldABlankDatabase_forMalformedJsonFile() {

        JSONData jsonData = JSONDataReader.readJsonFile("src/test/resources/malformed.json");

        assertEquals(0, jsonData.getPersons().size());
        assertEquals(0, jsonData.getMedicalRecords().size());
        assertEquals(0, jsonData.getFireStations().size());
    }

    @Test
    public void readJsonFile_shouldABlankDatabase_forBadJsonFile() {

        JSONData jsonData = JSONDataReader.readJsonFile("src/test/resources/bad.json");

        assertEquals(0, jsonData.getPersons().size());
        assertEquals(0, jsonData.getMedicalRecords().size());
        assertEquals(0, jsonData.getFireStations().size());
    }

    @Test
    public void readJsonFile_shouldABlankDatabase_forNoConfigurationFound() {

        JSONData jsonData = JSONDataReader.readJsonFile(null);

        assertEquals(0, jsonData.getPersons().size());
        assertEquals(0, jsonData.getMedicalRecords().size());
        assertEquals(0, jsonData.getFireStations().size());
    }
}

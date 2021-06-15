package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.services.json.JSONData;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private JSONData jsonData;

    @BeforeEach
    public void setUpPerTest() {

        this.jsonData = JSONDataReader.readJsonFile("src/test/resources/data.json");
    }

    @Test
    public void initDatabase_shouldDatabaseWithSameNumberDataAsJSONData_forJSonDataSupplied() {

        Database database = new Database(jsonData);

        assertEquals(this.jsonData.getPersons().size(), database.getPersons().size());
        assertEquals(this.jsonData.getFireStations().size(), database.fireStations.size());
    }

    @Test
    public void initDatabase_shouldDatabaseWithSameNumberDataAsJSONData_ForJSONDataBlank() {

        JSONData jsonData = new JSONData();
        Database database = new Database(jsonData);


        assertEquals(jsonData.getPersons().size(), database.getPersons().size());
        assertEquals(jsonData.getFireStations().size(), database.fireStations.size());
    }

}

package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.services.json.JSONData;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private JSONData jsonData;
    private Database database;

    @BeforeEach
    public void setUpPerTest() {

        this.jsonData = null;
        this.database = null;
    }

    @Test
    public void initDatabase_shouldDatabaseWithSameNumberDataAsJSONData_forJSonDataSupplied() {

        this.jsonData = JSONDataReader.readJsonFile("src/test/resources/data.json");
        this.database = new Database(this.jsonData);

        assertEquals(this.jsonData.getPersons().size(), this.database.getPersons().size());
        assertEquals(this.jsonData.getFireStations().size(), this.database.fireStations.size());
    }

    @Test
    public void initDatabase_shouldHaveADatabaseSizeOfZero_ForJSONDataBlank() {

        this.jsonData = JSONDataReader.readJsonFile("");
        this.database = new Database(this.jsonData);

        assertEquals(0, this.database.getPersons().size());
        assertEquals(0, this.database.fireStations.size());
    }

}

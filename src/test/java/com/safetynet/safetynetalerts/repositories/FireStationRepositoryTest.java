package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FireStationRepositoryTest {

    private FireStationRepository fireStationRepository;
    private Database              database;

    @BeforeEach
    public void setUpPerTest(@Autowired Database database, @Autowired FireStationRepository fireStationRepository) {

        this.database = database;
        database.initDatabase(JSONDataReader.readJsonFile("src/test/resources/data.json"));
        this.fireStationRepository = fireStationRepository;
    }

    @Test
    public void findAll_shouldReturnAllFireStations() {

        List<FireStation> fireStations = fireStationRepository.findAll();

        assertThat(fireStations.size()).isEqualTo(database.getFireStations().size());
    }


    @Test
    public void save_shouldReturnCreateFireStation_forNewFireStation() {

        int beforeSize = database.getFireStations().size();

        FireStation fireStation = new FireStation();
        fireStation.setStation(51L);
        fireStation.setAddress("5 rue des coquelicot");

        FireStation fireStationSaved = fireStationRepository.save(fireStation);

        assertThat(database.getFireStations().contains(fireStationSaved)).isTrue();
        assertThat(database.getFireStations().size()).isGreaterThan(beforeSize);
    }

    @Test
    public void update_shouldReturnUpdatedFireStation_forFireStationAlreadyExists() {

        int beforeSize = database.getFireStations().size();

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");
        fireStation.setStation(1L);

        FireStation fireStationUpdated = fireStationRepository.save(fireStation);

        assertThat(fireStationUpdated).isEqualTo(fireStation);
        assertThat(database.getFireStations().size()).isEqualTo(beforeSize);
    }

    @Test
    public void delete_shouldReturnDatabaseSmallerThanBefore_ForDeleteFireStation() {

        int beforeSize = database.getFireStations().size();

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        fireStationRepository.delete(fireStation);

        assertThat(database.getFireStations().contains(fireStation)).isFalse();
        assertThat(database.getFireStations().size()).isLessThan(beforeSize);
    }
}

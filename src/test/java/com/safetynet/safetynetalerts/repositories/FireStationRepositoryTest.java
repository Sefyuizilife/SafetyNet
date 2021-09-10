package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void findByAddress_shouldFireStation_forACorrespondingAddress() {

        String address = "644 Gershwin Cir";

        Optional<FireStation> oFireStation = fireStationRepository.findByAddress(address);

        assertThat(oFireStation.get().getAddress()).isEqualTo(address);
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
        fireStation.setStation(3L);
        fireStation.setAddress("1509 Culver St");

        this.fireStationRepository.delete(fireStation);

        assertThat(database.getFireStations().contains(fireStation)).isFalse();
        assertThat(database.getFireStations().size()).isLessThan(beforeSize);
    }

    @Test
    public void isExisting_shouldReturnTrue_ForExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setStation(3L);
        fireStation.setAddress("1509 Culver St");

        boolean isExisting = this.fireStationRepository.isExisting(fireStation);

        assertTrue(isExisting);
        assertThat(database.getFireStations().contains(fireStation)).isTrue();
    }

    @Test
    public void isExisting_shouldReturnFalse_ForNotExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setStation(987654321L);
        fireStation.setAddress("1509 Culver St");

        boolean isExisting = this.fireStationRepository.isExisting(fireStation);

        assertFalse(isExisting);
        assertThat(database.getFireStations().contains(fireStation)).isFalse();
    }

    @Test
    public void findAllByStation_shouldReturnAllStation_ForAnExistingStationNumber() {

        Long stationNumber = 1L;

        List<FireStation> fireStations = this.fireStationRepository.findAllByStation(stationNumber);

        long fireStationCount = this.database.getFireStations()
                                             .stream()
                                             .filter(item -> item.getStation().equals(stationNumber))
                                             .count();

        assertThat(fireStations.size()).isEqualTo(fireStationCount);
    }

    @Test
    public void findAllByStations_shouldReturnAllStation_ForAnExistingStationNumber() {

        List<Long> stationNumbers = Arrays.asList(1L, 2L, 3L, 4L);

        List<FireStation> fireStations = this.fireStationRepository.findAllByStations(stationNumbers);

        assertThat(fireStations.size()).isEqualTo(this.database.getFireStations().size());
    }
}

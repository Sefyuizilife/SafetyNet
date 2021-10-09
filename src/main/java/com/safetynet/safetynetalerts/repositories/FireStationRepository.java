package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FireStationRepository implements IRepository<FireStation> {

    private final Database database;

    public FireStationRepository(Database database) {

        this.database = database;
    }

    @Override
    public List<FireStation> findAll() {

        return database.getFireStations();
    }

    @Override
    public FireStation save(FireStation entity) {

        Optional<FireStation> fireStation = this.database.getFireStations().stream()
                                                         .filter(item -> item.getAddress().equals(entity.getAddress()))
                                                         .findFirst();

        if (fireStation.isPresent()) {

            Collections.replaceAll(database.getFireStations(), fireStation.get(), entity);
            return database.getFireStations().get(database.getFireStations().indexOf(entity));
        } else {

            database.getFireStations().add(entity);
            return database.getFireStations().get(database.getFireStations().size() - 1);
        }
    }

    @Override
    public void delete(FireStation entity) {

        this.database.getFireStations().remove(entity);
    }

    public boolean isExisting(FireStation fireStation) {

        Optional<FireStation> oFireStation = this.database.getFireStations()
                                                          .stream()
                                                          .filter(item -> item.equals(fireStation))
                                                          .findFirst();

        return oFireStation.isPresent();
    }

    public Optional<FireStation> findByAddress(String address) {

        return this.database.getFireStations()
                            .stream()
                            .filter(item -> item.getAddress().equals(address))
                            .findFirst();
    }

    public List<FireStation> findAllByStation(Long stationNumber) {

        return this.database.getFireStations()
                            .stream()
                            .filter(item -> item.getStation().equals(stationNumber))
                            .collect(Collectors.toList());
    }

    public List<FireStation> findAllByStations(List<Long> stationNumber) {

        return this.database.getFireStations()
                            .stream()
                            .filter(item -> stationNumber.contains(item.getStation()))
                            .collect(Collectors.toList());
    }
}

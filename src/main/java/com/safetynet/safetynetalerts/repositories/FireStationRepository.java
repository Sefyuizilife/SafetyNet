package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.FireStation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Optional<FireStation> findByAddress(String address) {

        return this.database.getFireStations().stream().filter(item -> item.getAddress().equals(address)).findFirst();
    }
}

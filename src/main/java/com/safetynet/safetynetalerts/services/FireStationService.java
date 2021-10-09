package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.repositories.FireStationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;

    FireStationService(FireStationRepository fireStationRepository) {

        this.fireStationRepository = fireStationRepository;
    }


    public FireStation update(FireStation fireStation) {

        this.fireStationRepository.findByAddress(fireStation.getAddress())
                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return this.fireStationRepository.save(fireStation);
    }

    public List<FireStation> findAll() {

        return fireStationRepository.findAll();
    }

    public FireStation save(FireStation fireStation) {

        if (!this.fireStationRepository.isExisting(fireStation)) {

            return this.fireStationRepository.save(fireStation);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    public void delete(String address) {

        Optional<FireStation> fireStation = fireStationRepository.findByAddress(address);

        if (fireStation.isPresent()) {

            fireStationRepository.delete(fireStation.get());
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<FireStation> findByAddress(String address) {

        return this.fireStationRepository.findByAddress(address);
    }

    public List<FireStation> findAllByStation(Long stationNumber) {

        return this.fireStationRepository.findAllByStation(stationNumber);
    }

    public List<FireStation> findAllByStations(List<Long> stationNumber) {

        return this.fireStationRepository.findAllByStations(stationNumber);
    }
}

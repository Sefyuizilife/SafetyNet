package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.services.FireStationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("firestation")
@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {

        this.fireStationService = fireStationService;
    }

    @GetMapping("")
    public List<FireStation> browse() {

        return this.fireStationService.findAll();
    }

    @GetMapping("/read")
    public FireStation read(@RequestParam(name = "address") String address) {

        return this.fireStationService.findByAddress(address)
                                      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("")
    public FireStation update(@RequestBody FireStation fireStation) {

        return this.fireStationService.update(fireStation);
    }

    @PostMapping("")
    public FireStation save(@RequestBody FireStation fireStation) {

        return this.fireStationService.save(fireStation);
    }

    @DeleteMapping("")
    public void delete(@RequestBody FireStation fireStation) {

        fireStationService.delete(fireStation.getAddress());

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

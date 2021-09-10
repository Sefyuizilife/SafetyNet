package com.safetynet.safetynetalerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.services.FireStationService;
import com.safetynet.safetynetalerts.services.PersonService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("firestation")
@RestController
public class FireStationController {

    private static final Logger       LOGGER = LoggerFactory.getLogger(FireStationController.class);
    private static final ObjectMapper OM     = new ObjectMapper();

    private final FireStationService fireStationService;
    private final PersonService      personService;
    private final HttpServletRequest request;

    public FireStationController(FireStationService fireStationService, PersonService personService, HttpServletRequest request) {

        this.personService      = personService;
        this.fireStationService = fireStationService;
        this.request            = request;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String browseOrGetPersonsByStation(@RequestParam(required = false) Long stationNumber) {

        if (stationNumber == null) {

            LOGGER.info("GET: {}", this.request.getRequestURI());

            JSONArray jsonArray = new JSONArray(this.fireStationService.findAll()
                                                                       .stream()
                                                                       .map(item -> new JSONObject() {{
                                                                           this.put("station", item.getStation());
                                                                           this.put("address", item.getAddress());
                                                                       }})
                                                                       .collect(Collectors.toList()));

            LOGGER.info(jsonArray.toString());
            return jsonArray.toString();
        } else {

            LOGGER.info("GET: {}?stationNumber={}", this.request.getRequestURI(), stationNumber);

            List<FireStation> fireStations = this.fireStationService.findAllByStation(stationNumber);

            if (fireStations.isEmpty()) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            List<Person> persons = this.personService.findAllByAddresses(
                    fireStations.stream()
                                .map(FireStation::getAddress)
                                .collect(Collectors
                                        .toList())
            );

            int childrenNumber = this.personService.getChildrenNumber(persons);

            JSONArray jsonArray = new JSONArray() {{
                persons.forEach(item -> {
                    this.put(new JSONObject() {{
                        this.put("firstName", item.getFirstName());
                        this.put("lastName", item.getLastName());
                        this.put("address", item.getAddress());
                        this.put("phone", item.getPhone());
                    }});
                });
            }};

            JSONObject jsonObject = new JSONObject() {{

                this.put("persons", jsonArray);
                this.put("adultsNumber", persons.size() - childrenNumber);
                this.put("kidsNumber", childrenNumber);
            }};

            LOGGER.info(jsonObject.toString());
            return jsonObject.toString();
        }
    }

    @GetMapping("/read")
    public FireStation read(@RequestParam(name = "address") String address) throws JsonProcessingException {

        LOGGER.info("GET: {}?address={}", this.request.getRequestURI(), address);

        FireStation fireStation = this.fireStationService.findByAddress(address)
                                                         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LOGGER.info(OM.writeValueAsString(fireStation));
        return fireStation;
    }

    @PutMapping("")
    public FireStation update(@RequestBody FireStation fireStation) throws JsonProcessingException {

        LOGGER.info("PUT: {}", this.request.getRequestURI());

        fireStation = this.fireStationService.update(fireStation);

        LOGGER.info(OM.writeValueAsString(fireStation));
        return fireStation;
    }

    @PostMapping("")
    public FireStation save(@RequestBody FireStation fireStation) throws JsonProcessingException {

        LOGGER.info("POST: {}", this.request.getRequestURI());

        fireStation = this.fireStationService.save(fireStation);

        LOGGER.info(OM.writeValueAsString(fireStation));
        return fireStation;
    }

    @DeleteMapping("")
    public void delete(@RequestBody FireStation fireStation) {

        LOGGER.info("DELETE: {}", this.request.getRequestURI());

        fireStationService.delete(fireStation.getAddress());

        LOGGER.info(new ResponseStatusException(HttpStatus.NO_CONTENT).toString());
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

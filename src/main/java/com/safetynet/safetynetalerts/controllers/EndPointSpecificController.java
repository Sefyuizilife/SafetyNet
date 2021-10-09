package com.safetynet.safetynetalerts.controllers;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class EndPointSpecificController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndPointSpecificController.class);

    private final PersonService      personService;
    private final FireStationService fireStationService;
    private final HttpServletRequest request;

    public EndPointSpecificController(PersonService personService, FireStationService fireStationService, HttpServletRequest request) {

        this.personService      = personService;
        this.fireStationService = fireStationService;
        this.request            = request;
    }

    @GetMapping(value = "childAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getChildrenByAddress(@RequestParam(name = "address") String address) {

        LOGGER.info("GET: {}?address={}", this.request.getRequestURI(), address);

        List<Person> persons = this.personService.findAllByAddress(address);

        List<Person> children = persons.stream()
                                       .filter(item -> Period.between(item.getBirthDate(),
                                               LocalDate.now()).getYears() <= 18)
                                       .collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray() {{
            children.forEach(
                    child -> {
                        this.put(new JSONObject() {{
                            this.put("lastname", child.getLastName());
                            this.put("firstname", child.getFirstName());
                            this.put("age", Period.between(child.getBirthDate(), LocalDate.now()).getYears());
                            this.put("otherMembersHouse", new JSONArray() {{
                                persons.forEach(other -> {
                                    if (!other.equals(child)) {
                                        this.put(new JSONObject() {{
                                            this.put("lastName", other.getLastName());
                                            this.put("firstName", other.getFirstName());
                                        }});
                                    }
                                });
                            }});

                        }});
                    }
            );
        }};

        LOGGER.debug(jsonArray.toString());
        return jsonArray.toString();
    }

    @GetMapping(value = "phoneAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPhoneByFireStation(@RequestParam("firestation") Long stationNumber) {

        LOGGER.info("GET: {}?firestation={}", this.request.getRequestURI(), stationNumber);

        List<FireStation> fireStations = this.fireStationService.findAllByStation(stationNumber);

        List<Person> persons = this.personService.findAllByAddresses(fireStations.stream()
                                                                                 .map(FireStation::getAddress)
                                                                                 .collect(Collectors.toList()));

        JSONArray jsonArray = new JSONArray(persons.stream().map(Person::getPhone).collect(Collectors.toList()));

        LOGGER.debug(jsonArray.toString());
        return jsonArray.toString();
    }

    @GetMapping(value = "communityEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEmailByCity(@RequestParam String city) {

        LOGGER.info("GET: {}?city={}", this.request.getRequestURI(), city);

        List<Person> persons = this.personService.findAllByCity(city);

        JSONArray jsonArray = new JSONArray(persons.stream().map(Person::getEmail).collect(Collectors.toList()));

        LOGGER.debug(jsonArray.toString());
        return jsonArray.toString();
    }

    @GetMapping(value = "fire", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFireStationByAddress(@RequestParam String address) {

        LOGGER.info("GET: {}?fire={}", this.request.getRequestURI(), address);

        Optional<FireStation> fireStation = this.fireStationService.findByAddress(address);

        List<Person> persons = this.personService.findAllByAddress(address);

        JSONObject jsonObject = new JSONObject();

        if (fireStation.isEmpty() || persons.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        JSONArray jsonPersons = new JSONArray();

        for (Person item : persons) {

            JSONObject jsonPerson = new JSONObject();
            jsonPerson.put("lastName", item.getLastName());
            jsonPerson.put("firstName", item.getFirstName());
            jsonPerson.put("phone", item.getPhone());
            jsonPerson.put("age", Period.between(item.getBirthDate(), LocalDate.now()).getYears());
            jsonPerson.put("medications", new JSONArray(item.getMedicalRecord().getMedications()));
            jsonPerson.put("allergies", new JSONArray(item.getMedicalRecord().getAllergies()));

            jsonPersons.put(jsonPerson);
        }

        jsonObject.put("station", fireStation.get().getStation());
        jsonObject.put("persons", jsonPersons);

        LOGGER.debug(jsonObject.toString());
        return jsonObject.toString();
    }

    @GetMapping(value = "flood/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHomesByStations(@RequestParam(value = "stations") List<Long> stationNumbers) {

        LOGGER.info("GET: {}?station={}", this.request.getRequestURI(), stationNumbers);

        List<FireStation> fireStations = this.fireStationService.findAllByStations(stationNumbers);

        List<Person> persons = this.personService.findAllByAddresses(fireStations.stream()
                                                                                 .map(FireStation::getAddress)
                                                                                 .collect(Collectors.toList()));

        JSONArray results = new JSONArray();

        for (FireStation fireStation : fireStations) {

            JSONObject result = new JSONObject();
            result.put("address", fireStation.getAddress());

            JSONArray jsonPersons = new JSONArray();

            for (Person person : persons) {

                if (fireStation.getAddress().equals(person.getAddress())) {

                    JSONObject jsonPerson = new JSONObject();

                    jsonPerson.put("lastName", person.getLastName());
                    jsonPerson.put("firstName", person.getFirstName());
                    jsonPerson.put("age", Period.between(person.getBirthDate(), LocalDate.now()).getYears());
                    jsonPerson.put("phone", person.getPhone());
                    jsonPerson.put("medications", new JSONArray(person.getMedicalRecord().getMedications()));
                    jsonPerson.put("allergies", new JSONArray(person.getMedicalRecord().getAllergies()));

                    jsonPersons.put(jsonPerson);
                }
            }

            result.put("persons", jsonPersons);

            results.put(result);
        }

        LOGGER.debug(results.toString());
        return results.toString();
    }

    @GetMapping(value = "personInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String personInfo(@RequestParam String firstName, @RequestParam String lastName) {

        LOGGER.info("GET: {}?firstName={}&lastName={}", this.request.getRequestURI(), firstName, lastName);

        Person person = this.personService.findByFullName(firstName, lastName)
                                          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        JSONObject jsonObject = new JSONObject() {{

            this.put("lastName", person.getLastName());
            this.put("firstName", person.getFirstName());
            this.put("age", Period.between(person.getBirthDate(), LocalDate.now()).getYears());
            this.put("address", person.getAddress());
            this.put("email", person.getEmail());
            this.put("medications", new JSONArray(person.getMedicalRecord().getMedications()));
            this.put("allergies", new JSONArray(person.getMedicalRecord().getAllergies()));
        }};

        LOGGER.debug(jsonObject.toString());
        return jsonObject.toString();
    }
}

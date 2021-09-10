package com.safetynet.safetynetalerts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/person")
@RestController
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService      personService;
    private final HttpServletRequest request;

    public PersonController(PersonService personService, HttpServletRequest request) {

        this.personService = personService;
        this.request       = request;
    }

    @GetMapping("")
    public List<Person> browse() {

        LOGGER.info("GET: {}", this.request.getRequestURI());

        List<Person> persons = this.personService.findAll();

        LOGGER.info(persons.toString());
        return persons;
    }

    @GetMapping("/read")
    public Person read(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) throws JsonProcessingException {

        LOGGER.info("GET: {}?firstName={}&lastName={}", this.request.getRequestURI(), firstName, lastName);

        Person person = this.personService.findByFullName(firstName, lastName)
                                          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LOGGER.info(person.toJson().toString());
        return person;
    }

    @PutMapping("")
    public Person update(@RequestBody Person person) throws JsonProcessingException {

        LOGGER.info("PUT: {}", this.request.getRequestURI());

        person = this.personService.update(person);

        LOGGER.info(person.toJson().toString());
        return person;
    }

    @PostMapping("")
    public Person save(@RequestBody Person person) throws JsonProcessingException {

        LOGGER.info("POST: {}", this.request.getRequestURI());

        person = this.personService.save(person);

        LOGGER.info(person.toJson().toString());
        return person;
    }

    @DeleteMapping("")
    public void delete(@RequestBody Person person) {

        LOGGER.info("DELETE: {}", this.request.getRequestURI());

        personService.delete(person.getFirstName(), person.getLastName());

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

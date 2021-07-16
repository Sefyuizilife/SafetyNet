package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/person")
@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {

        this.personService = personService;
    }

    @GetMapping("")
    public List<Person> browse() {

        return this.personService.findAll();
    }

    @GetMapping("/read")
    public Person read(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) {

        return this.personService.findByFullName(firstName, lastName)
                                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("")
    public Person update(@RequestBody Person person) {

        return this.personService.update(person);
    }

    @PostMapping("")
    public Person save(@RequestBody Person person) {

        return this.personService.save(person);
    }

    @DeleteMapping("")
    public void delete(@RequestBody Person person) {

        personService.delete(person.getFirstName(), person.getLastName());

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

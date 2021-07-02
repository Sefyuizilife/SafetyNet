package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.Person;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonRepository implements IRepository<Person> {

    private final List<Person> persons;

    PersonRepository(Database database) {

        this.persons = database.getPersons();
    }

    @Override
    public List<Person> findAll() {

        return persons;
    }

    @Override
    public Person save(Person entity) {

        Optional<Person> person = this.persons.stream()
                                              .filter(item -> item.getLastName().equals(entity.getLastName()))
                                              .filter(item -> item.getFirstName().equals(entity.getFirstName()))
                                              .findFirst();

        if (person.isPresent()) {

            Collections.replaceAll(persons, person.get(), entity);
            return persons.get(persons.indexOf(entity));
        } else {

            persons.add(entity);
            return persons.get(persons.size() - 1);
        }
    }

    @Override
    public void delete(Person entity) {

        this.persons.remove(entity);
    }

    public Optional<Person> findByFullName(String firstName, String lastName) {

        return this.persons.stream()
                           .filter(item ->
                                   item.getFirstName().equalsIgnoreCase(firstName))
                           .filter(item -> item.getLastName().equalsIgnoreCase(lastName))
                           .findFirst();
    }
}

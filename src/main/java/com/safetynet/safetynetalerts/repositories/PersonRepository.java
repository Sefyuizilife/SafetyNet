package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.Person;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonRepository implements IRepository<Person> {

    private final Database database;

    public PersonRepository(Database database) {

        this.database = database;
    }

    @Override
    public List<Person> findAll() {

        return database.getPersons();
    }

    @Override
    public Person save(Person entity) {

        Optional<Person> person = this.database.getPersons().stream()
                                               .filter(item -> item.getLastName().equals(entity.getLastName()))
                                               .filter(item -> item.getFirstName().equals(entity.getFirstName()))
                                               .findFirst();

        if (person.isPresent()) {

            Collections.replaceAll(database.getPersons(), person.get(), entity);
            return database.getPersons().get(database.getPersons().indexOf(entity));
        } else {

            database.getPersons().add(entity);
            return database.getPersons().get(database.getPersons().size() - 1);
        }
    }

    @Override
    public void delete(Person entity) {

        this.database.getPersons().remove(entity);
    }

    public Optional<Person> findByFullName(String firstName, String lastName) {

        return this.database.getPersons().stream()
                            .filter(item ->
                                    item.getFirstName().equalsIgnoreCase(firstName))
                            .filter(item -> item.getLastName().equalsIgnoreCase(lastName))
                            .findFirst();
    }
}

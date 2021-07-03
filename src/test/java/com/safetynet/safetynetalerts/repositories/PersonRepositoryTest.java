package com.safetynet.safetynetalerts.repositories;

import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PersonRepositoryTest {

    private PersonRepository personRepository;
    private List<Person>     personsDatabase;

    @BeforeEach
    public void setUpPerTest(@Autowired Database database, @Autowired PersonRepository personRepository) {

        this.personsDatabase  = database.getPersons();
        this.personRepository = personRepository;
    }

    @Test
    public void findAll_shouldReturnAllPersons() {

        List<Person> persons = personRepository.findAll();

        assertThat(persons.size()).isEqualTo(personsDatabase.size());
    }


    @Test
    public void save_shouldReturnCreatePerson_forNewPerson() {

        int beforeSize = personsDatabase.size();

        Person person = new Person();
        person.setFirstName("Youssef");
        person.setLastName("SafeNet");
        person.setBirthDate(LocalDate.now());
        person.setAddress("5 rue des coquelicot");
        person.setZip(51100);
        person.setEmail("toto@gmail.com");
        person.setPhone("0033-001");
        person.setMedicalRecord(new MedicalRecord() {{
            this.setAllergies(new ArrayList<>());
            this.setMedications(new ArrayList<>());
        }});

        Person personSaved = personRepository.save(person);

        assertThat(personsDatabase.contains(personSaved)).isTrue();
        assertThat(personsDatabase.size()).isGreaterThan(beforeSize);
    }

    @Test
    public void update_shouldReturnUpdatedPerson_forPersonAlreadyExists() {

        int beforeSize = personsDatabase.size();

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setBirthDate(LocalDate.now());
        person.setAddress("5 rue des coquelicot");
        person.setZip(51100);
        person.setEmail("toto@gmail.com");
        person.setPhone("0033-001");
        person.setMedicalRecord(new MedicalRecord() {{
            this.setAllergies(new ArrayList<>());
            this.setMedications(new ArrayList<>());
        }});

        Person personSaved = personRepository.save(person);

        assertThat(personsDatabase.contains(personSaved)).isTrue();
        assertThat(personsDatabase.size()).isEqualTo(beforeSize);
    }

    @Test
    public void delete_shouldReturnDatabaseSmallerThanBefore_ForDeletePerson() {

        int beforeSize = personsDatabase.size();

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip(97451);
        person.setPhone("841-874-6512");
        person.setEmail("jaboyd@email.com");
        person.setBirthDate(LocalDate.of(1984, 3, 6));
        person.setMedicalRecord(
                new MedicalRecord(
                        new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")),
                        new ArrayList<>(Collections.singletonList("nillacilan"))
                )
        );


        personRepository.delete(person);

        assertThat(personsDatabase.contains(person)).isFalse();
        assertThat(personsDatabase.size()).isLessThan(beforeSize);
    }
}

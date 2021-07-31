package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    public void setUpPerTest() {

        this.personService = new PersonService(personRepository);
    }

    @Test
    public void findAll_shouldReturnAllPersonOfDatabase() {

        when(personRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new Person("person", "1", "address1", 1, "city1", "phone1", "email1"),
                new Person("person", "2", "address2", 2, "city2", "phone2", "email2"),
                new Person("person", "3", "address3", 3, "city3", "phone3", "email3"),
                new Person("person", "4", "address4", 4, "city4", "phone4", "email4"),
                new Person("person", "5", "address5", 5, "city5", "phone5", "email5")
        )));

        assertThat(personService.findAll()).isEqualTo(personRepository.findAll());
    }

    @Test
    public void update_shouldReturnTheUpdatedPerson_forAnExistingPerson() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip(97451);
        person.setPhone("UPDATED");
        person.setEmail("jaboyd@email.com");
        person.setBirthDate(LocalDate.of(1984, 3, 6));
        person.setMedicalRecord(new MedicalRecord());

        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person personUpdated = personService.update(person);

        verify(personRepository, times(1)).save(person);

        assertThat(personUpdated).isEqualTo(person);
        assertThat(personUpdated.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(personUpdated.getLastName()).isEqualTo(person.getLastName());
        assertThat(personUpdated.getPhone()).isEqualTo(person.getPhone());
    }

    @Test
    public void update_shouldReturnThisUpdatedPerson_forAnNotExistingPerson() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip(97451);
        person.setPhone("UPDATED");
        person.setEmail("jaboyd@email.com");
        person.setBirthDate(LocalDate.of(1984, 3, 6));
        person.setMedicalRecord(new MedicalRecord());

        assertThrows(ResponseStatusException.class, () -> personService.update(person));
        verify(personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldRemoveThePersonFromTheDatabase_ForPersonExisting() {

        Person person = new Person();
        person.setFirstName("Youssef");
        person.setLastName("SafeNet");

        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));

        personService.delete(person.getFirstName(), person.getLastName());

        verify(personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldThrowNoSuchElementException_ForPersonNotExisting() {

        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> personService.delete("", ""));
    }
}

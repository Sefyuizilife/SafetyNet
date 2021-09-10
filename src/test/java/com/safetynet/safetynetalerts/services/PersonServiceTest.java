package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    private final List<Person> persons;

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    {
        persons = new ArrayList<>(Arrays.asList(
                new Person("person1", "adult", "address1", 1, "city1", "phone1", "email1") {{
                    this.setBirthDate(LocalDate.now().minusYears(30));
                }},
                new Person("person2", "adult", "address2", 2, "city2", "phone2", "email2") {{
                    this.setBirthDate(LocalDate.now().minusYears(30));
                }},
                new Person("person3", "adult", "address3", 3, "city3", "phone3", "email3") {{
                    this.setBirthDate(LocalDate.now().minusYears(30));
                }},
                new Person("person4", "child", "address1", 1, "city1", "phone1", "email4") {{
                    this.setBirthDate(LocalDate.now().minusYears(15));
                }},
                new Person("person5", "child", "address2", 2, "city2", "phone2", "email5") {{
                    this.setBirthDate(LocalDate.now().minusYears(15));
                }}
        ));
    }

    @BeforeEach
    public void setUpPerTest() {

        this.personService = new PersonService(personRepository);
    }

    @Test
    public void save_shouldReturnNewPersonCreated_forNewPerson() {

        Person person = new Person("personSave", "adult", "address50", 51, "city100", "phone50", "toto@gmail.com");

        when(this.personRepository.save(any())).thenReturn(person);

        Person personSave = personService.save(person);

        assertThat(person).isEqualTo(personSave);
    }

    @Test
    public void save_shouldThrowException_forAlreadyPersonCreated() {

        Person person = persons.get(0);

        when(this.personRepository.findByFullName(any(), any())).thenReturn(Optional.of(person));

        assertThrows(ResponseStatusException.class, () -> this.personService.save(person));
    }

    @Test
    public void findAll_shouldReturnAllPersonOfDatabase() {

        when(this.personRepository.findAll()).thenReturn(this.persons);

        assertThat(personService.findAll()).isEqualTo(personRepository.findAll());
    }

    @Test
    public void update_shouldReturnTheUpdatedPerson_forAnExistingPerson() {

        Person person = new Person();
        person.setFirstName("person1");
        person.setLastName("adult");
        person.setAddress("UPDATED");
        person.setCity("UPDATED");
        person.setZip(0);
        person.setPhone("UPDATED");
        person.setEmail("UPDATED");

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(persons.get(0)));
        when(this.personRepository.save(any(Person.class))).thenReturn(person);

        Person personUpdated = personService.update(person);

        //        verify(this.personRepository, times(1)).save(person);
        //        verify(this.personRepository, times(1)).findByFullName(anyString(), anyString());

        assertThat(personUpdated).isEqualTo(person);
        assertThat(personUpdated.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(personUpdated.getLastName()).isEqualTo(person.getLastName());
        assertThat(personUpdated.getPhone()).isEqualTo(person.getPhone());
    }

    @Test
    public void update_shouldReturnThisUpdatedPerson_forAnNotExistingPerson() {

        assertThrows(ResponseStatusException.class, () -> personService.update(this.persons.get(0)));
        verify(this.personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldRemoveThePersonFromTheDatabase_ForPersonExisting() {

        Person person = new Person();
        person.setFirstName("Youssef");
        person.setLastName("SafeNet");

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));

        personService.delete(person.getFirstName(), person.getLastName());

        verify(this.personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldThrowNoSuchElementException_forPersonNotExisting() {

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> personService.delete("", ""));
    }

    @Test
    public void getChildrenNumber_shouldReturnNumberChildren_forPersonsList() {

        int childrenNumber = this.personService.getChildrenNumber(this.persons);

        long count = this.persons.stream()
                                 .filter(item -> (Period.between(item.getBirthDate(), LocalDate.now())
                                                        .getYears()) <= 18)
                                 .count();

        assertThat(childrenNumber).isEqualTo(count);
    }

    @Test
    public void findAllByAddresses_returnPersonsListWithAnAddressIsContained_forAddressesList() {

        List<String> addresses = List.of("address1", "address2");

        when(personRepository.findAll()).thenReturn(this.persons);

        List<Person> persons = this.personService.findAllByAddresses(addresses);

        assertThat(persons.stream()
                          .map(Person::getAddress)
                          .distinct()
                          .collect(Collectors.toList())).isEqualTo(addresses);

        assertThat(persons.toString()).isEqualTo(this.persons.stream()
                                                             .filter(item -> addresses.contains(item.getAddress()))
                                                             .collect(Collectors.toList()).toString());
    }

    @Test
    public void findAllByAddress_returnPersonsListWithAnAddressIsContained_forOneAddress() {

        String address = "address1";

        when(personRepository.findAll()).thenReturn(this.persons);

        List<Person> persons = this.personService.findAllByAddress(address);

        assertThat(persons.stream()
                          .map(Person::getAddress)
                          .distinct().findAny().get()).isEqualTo(address);

        assertThat(persons.toString()).isEqualTo(this.persons.stream()
                                                             .filter(item -> address.equals(item.getAddress()))
                                                             .collect(Collectors.toList()).toString());
    }

    @Test
    public void findAllByCity_returnPersonsListWithACityEqual_forOneCity() {

        String city = "city3";

        when(personRepository.findAll()).thenReturn(this.persons);

        List<Person> persons = this.personService.findAllByCity(city);

        assertThat(persons.stream()
                          .map(Person::getCity)
                          .distinct().findAny().get()).isEqualTo(city);

        assertThat(persons.toString()).isEqualTo(this.persons.stream()
                                                             .filter(item -> city.equals(item.getCity()))
                                                             .collect(Collectors.toList()).toString());
    }
}

package com.safetynet.safetynetalerts.controllers;


import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import com.safetynet.safetynetalerts.services.PersonService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void browse_shouldAllPersons() throws Exception {

        Person person = new Person();
        person.setLastName("SafeNet");
        person.setFirstName("Youssef");
        person.setBirthDate(LocalDate.of(1989, 5, 27));
        person.setMedicalRecord(new MedicalRecord());
        person.setEmail("test@email.com");
        person.setAddress("5, rue des coquelicots");
        person.setZip(10250);
        person.setCity("Troyes");

        List<Person> persons = new ArrayList<>();
        persons.add(person);

        when(personService.findAll()).thenReturn(persons);

        mockMvc.perform(get("/person"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Youssef")))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(personService, times(1)).findAll();
    }

    @Test
    public void read_shouldReturnThePersonCorrespondingToTheFirstAndLastName() throws Exception {

        Person person = new Person();
        person.setLastName("SafeNet");
        person.setFirstName("Youssef");
        person.setBirthDate(LocalDate.of(1989, 5, 27));
        person.setMedicalRecord(new MedicalRecord());
        person.setEmail("test@email.com");
        person.setAddress("5, rue des coquelicots");
        person.setZip(10250);
        person.setCity("Troyes");

        when(this.personService.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));

        mockMvc.perform(get("/person/read?firstName=Youssef&lastName=SafeNet"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Youssef")))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void save_shouldReturnNewPersonCreated() throws Exception {

        Person person = new Person();
        person.setLastName("SafeNet");
        person.setFirstName("Youssef");
        person.setBirthDate(LocalDate.of(1989, 5, 27));
        person.setMedicalRecord(new MedicalRecord());
        person.setEmail("test@email.com");
        person.setAddress("5, rue des coquelicots");
        person.setZip(10250);
        person.setCity("Troyes");

        JSONObject personJson = new JSONObject() {{
            this.put("lastName", person.getLastName());
            this.put("firstName", person.getFirstName());
            this.put("birthDate", person.getBirthDate());
            this.put("email", person.getEmail());
            this.put("address", person.getAddress());
            this.put("zip", person.getZip());
            this.put("city", person.getCity());
        }};

        when(this.personService.save(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                .content(String.valueOf(personJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(String.valueOf(personJson)));
    }

    @Test
    public void update_shouldReturnTheUpdatedPerson_whenAPersonExists() throws Exception {

        Person person = new Person();
        person.setLastName("SafeNet");
        person.setFirstName("Youssef");
        person.setBirthDate(LocalDate.of(1989, 5, 27));
        person.setMedicalRecord(new MedicalRecord());
        person.setEmail("test@email.com");
        person.setAddress("5, rue des coquelicots");
        person.setZip(10250);
        person.setCity("Troyes");

        JSONObject personJson = new JSONObject() {{
            this.put("lastName", person.getLastName());
            this.put("firstName", person.getFirstName());
            this.put("birthDate", person.getBirthDate());
            this.put("email", person.getEmail());
            this.put("address", person.getAddress());
            this.put("zip", person.getZip());
            this.put("city", person.getCity());
        }};

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));
        when(this.personService.update(any())).thenReturn(person);

        mockMvc.perform(put("/person")
                .content(String.valueOf(personJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(String.valueOf(personJson)));
    }

    @Test
    public void delete_shouldReturnVoid_whenAPersonExist() throws Exception {

        Person person = new Person();
        person.setLastName("SafeNet");
        person.setFirstName("Youssef");

        JSONObject personJson = new JSONObject() {{
            this.put("firstName", person.getFirstName());
            this.put("lastName", person.getLastName());
        }};

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));

        mockMvc.perform(delete("/person")
                .content(String.valueOf(personJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());

    }
}
package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.services.FireStationService;
import com.safetynet.safetynetalerts.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EndPointSpecificControllerTest {

    @MockBean
    PersonService personService;

    @MockBean
    FireStationService fireStationService;

    List<Person>      persons      = new ArrayList<>();
    List<FireStation> fireStations = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    {
        this.persons.addAll(Arrays.asList(
                new Person() {{
                    this.setFirstName("1");
                    this.setLastName("Adult1");
                    this.setAddress("address1");
                    this.setPhone("11111-11111");
                    this.setCity("City1");
                    this.setZip(12345);
                    this.setEmail("email1@toto.com");
                    this.setBirthDate(LocalDate.of(1989, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }},
                new Person() {{
                    this.setFirstName("2");
                    this.setLastName("Adult2");
                    this.setAddress("address2");
                    this.setPhone("22222-22222");
                    this.setCity("City2");
                    this.setZip(12345);
                    this.setEmail("email2@toto.com");
                    this.setBirthDate(LocalDate.of(1989, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }},
                new Person() {{
                    this.setFirstName("3");
                    this.setLastName("Child1");
                    this.setAddress("address2");
                    this.setPhone("22222-22222");
                    this.setCity("City2");
                    this.setZip(12345);
                    this.setEmail("email2@toto.com");
                    this.setBirthDate(LocalDate.of(2021, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }},
                new Person() {{
                    this.setFirstName("3");
                    this.setLastName("Child2");
                    this.setAddress("address2");
                    this.setPhone("22222-22222");
                    this.setCity("City2");
                    this.setZip(12345);
                    this.setEmail("email2@toto.com");
                    this.setBirthDate(LocalDate.of(2021, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }},
                new Person() {{
                    this.setFirstName("3");
                    this.setLastName("Adult1");
                    this.setAddress("address3");
                    this.setPhone("22222-33333");
                    this.setCity("City2");
                    this.setZip(12345);
                    this.setEmail("email2@toto.com");
                    this.setBirthDate(LocalDate.of(1950, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }},
                new Person() {{
                    this.setFirstName("2");
                    this.setLastName("Child1");
                    this.setAddress("address3");
                    this.setPhone("22222-33333");
                    this.setCity("City2");
                    this.setZip(12345);
                    this.setEmail("email2@toto.com");
                    this.setBirthDate(LocalDate.of(2020, 5, 27));
                    this.setMedicalRecord(
                            new MedicalRecord(
                                    Arrays.asList("medication1", "medication2", "medication3"),
                                    Arrays.asList("allergie1", "allergie2", "allergie3"))
                    );
                }}));

        this.fireStations.addAll(Arrays.asList(
                new FireStation() {{
                    this.setStation(1L);
                    this.setAddress("address1");
                }},
                new FireStation() {{
                    this.setStation(2L);
                    this.setAddress("address2");
                }},
                new FireStation() {{
                    this.setStation(2L);
                    this.setAddress("address3");
                }}
        ));
    }

    @Test
    public void getChildrenByAddress_shouldReturnChildren() throws Exception {

        when(this.personService.findAllByAddress(anyString()))
                .thenReturn((this.persons.stream()
                                         .filter(item -> item.getAddress()
                                                             .equals("address2"))
                                         .collect(Collectors.toList())));

        mockMvc.perform(get("/childAlert?address={address}", "address2"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().string(containsString("Child2")));
    }

    @Test
    public void getPhoneByFireStation_shouldReturnPhoneList_ForFireStationExisting() throws Exception {

        Long station = 1L;

        when(this.fireStationService.findAllByStation(anyLong())).thenReturn(
                this.fireStations.stream()
                                 .filter(item -> item.getStation().equals(station))
                                 .collect(Collectors.toList()));

        when(this.personService.findAllByAddresses(anyList())).thenReturn(
                this.persons.stream()
                            .filter(item -> item.getAddress().equalsIgnoreCase("address1"))
                            .collect(Collectors.toList()));


        mockMvc.perform(get("/phoneAlert?firestation=1"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().string(containsString("11111-11111")));
    }

    @Test
    public void browseOrGetPersonsByStation_shouldReturnPersonListWithChildrenNumberAndAdultNumber() throws Exception {

        Long stationNumber = 2L;

        List<FireStation> fireStations = this.fireStations.stream()
                                                          .filter(item -> item.getStation().equals(stationNumber))
                                                          .collect(Collectors.toList());

        List<String> fireStationAddresses = fireStations.stream()
                                                        .map(FireStation::getAddress)
                                                        .collect(Collectors.toList());

        List<Person> personsByAddresses = this.persons.stream()
                                                      .filter(item -> fireStationAddresses.contains(item.getAddress()))
                                                      .collect(Collectors.toList());

        when(this.fireStationService.findAllByStation(anyLong())).thenReturn(fireStations);

        when(this.personService.findAllByAddresses(anyList())).thenReturn(personsByAddresses);

        when(this.personService.getChildrenNumber(anyList())).thenReturn(3);

        mockMvc.perform(get("/firestation?stationNumber={stationNumber}", "2"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.kidsNumber").value(3))
               .andExpect(jsonPath("$.adultsNumber").value(2))
               .andExpect(jsonPath("$.persons").value(personsByAddresses
                       .stream()
                       .map(item -> String.format("%s %s", item.getLastName(), item.getFirstName()))
                       .collect(Collectors.toList()
                       )));
    }

    @Test
    public void getFireStationByAddress_shouldReturnPersonsListWithMedicationAndAssociatedFireStation_forRegisteredAddress() throws Exception {

        String address = "address2";

        Optional<FireStation> oFireStation = this.fireStations.stream()
                                                              .filter(item -> item.getAddress().equals(address))
                                                              .findFirst();

        List<Person> persons = this.persons.stream()
                                           .filter(item -> item.getAddress().equals(address))
                                           .collect(Collectors.toList());

        when(this.fireStationService.findByAddress(anyString())).thenReturn(oFireStation);
        when(this.personService.findAllByAddress(anyString())).thenReturn(persons);

        mockMvc.perform(get("/fire?address={address}", address))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.persons").isArray())
               .andExpect(jsonPath("$.persons[0].lastName").isString())
               .andExpect(jsonPath("$.persons[0].firstName").isString())
               .andExpect(jsonPath("$.persons[0].phone").isString())
               .andExpect(jsonPath("$.persons[0].age").isNumber())
               .andExpect(jsonPath("$.persons[0].allergies").isArray())
               .andExpect(jsonPath("$.persons[0].medications").isArray())
               .andExpect(jsonPath("$.persons", hasSize(3)))
               .andExpect(jsonPath("$.station", is(2)))
        ;
    }

    @Test
    public void getHomesByStations_shouldReturnAllPersonsByAddress_forAllStations() throws Exception {

        String stations = "1, 2, 3";

        when(this.fireStationService.findAllByStations(anyList())).thenReturn(this.fireStations);
        when(this.personService.findAllByAddresses(anyList())).thenReturn(this.persons);

        mockMvc.perform(get("/flood/stations?stations={stations}", stations))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].persons").isArray())
               .andExpect(jsonPath("$[0].persons[0].firstName").isString())
               .andExpect(jsonPath("$[0].persons[0].lastName").isString())
               .andExpect(jsonPath("$[0].persons[0].age").isNumber())
               .andExpect(jsonPath("$[0].persons[0].phone").isString())
               .andExpect(jsonPath("$[0].persons[0].allergies").isArray())
               .andExpect(jsonPath("$[0].persons[0].medications").isArray())
               .andExpect(jsonPath("$[0].address", is("address1")))
               .andExpect(jsonPath("$[1].address", is("address2")))
               .andExpect(jsonPath("$[2].address", is("address3")))
               .andExpect(jsonPath("$[0].persons", hasSize(1)))
               .andExpect(jsonPath("$[1].persons", hasSize(3)))
               .andExpect(jsonPath("$[2].persons", hasSize(2)));
    }

    @Test
    public void personInfo_shouldPersonInfos_forPersonExist() throws Exception {

        String firstName = "1";
        String lastName  = "Adult1";

        when(this.personService.findByFullName(anyString(), anyString())).thenReturn(Optional.of(this.persons.get(0)));

        mockMvc.perform(get("/personInfo?firstName={firstName}&lastName={lastName}", firstName, lastName))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.allergies").isArray())
               .andExpect(jsonPath("$.medications").isArray())
               .andExpect(jsonPath("$.firstName").isString())
               .andExpect(jsonPath("$.lastName").isString())
               .andExpect(jsonPath("$.age").isNumber())
               .andExpect(jsonPath("$.email").isString())
               .andExpect(jsonPath("$.address", is("address1")))
               .andExpect(jsonPath("$.firstName", is("1")))
               .andExpect(jsonPath("$.lastName", is("Adult1")))
               .andExpect(jsonPath("$.age", is(32)))
               .andExpect(jsonPath("$.email", is("email1@toto.com")));
    }

    @Test
    public void getEmailByCity_shouldEmailList_forRegisteredCity() throws Exception {

        String city = "City2";

        when(this.personService.findAllByCity(anyString())).thenReturn(this.persons.stream()
                                                                                   .filter(item -> item.getCity()
                                                                                                       .equals(city))
                                                                                   .collect(Collectors.toList()));

        mockMvc.perform(get("/communityEmail?city={city}", city))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$", hasSize(5)));
    }
}

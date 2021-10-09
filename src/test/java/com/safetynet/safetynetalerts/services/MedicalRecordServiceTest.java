package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private PersonRepository personRepository;

    private MedicalRecordService medicalRecordService;

    @BeforeEach
    public void setUpPerTest() {

        this.medicalRecordService = new MedicalRecordService(personRepository);
    }

    @Test
    public void findAll_shouldReturnAllMedicalRecordOfDatabase() {

        when(personRepository.findAll()).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new Person() {{
                            this.setFirstName("person1");
                            this.setLastName("1");
                            this.setBirthDate(LocalDate.of(1990, 1, 1));
                            this.setMedicalRecord(new MedicalRecord(Arrays.asList("1", "2"), Arrays.asList("01", "02")));
                        }},
                        new Person() {{
                            this.setFirstName("person2");
                            this.setLastName("2");
                            this.setBirthDate(LocalDate.of(1990, 1, 1));
                            this.setMedicalRecord(new MedicalRecord(Arrays.asList("1", "2"), Arrays.asList("01", "02")));
                        }},
                        new Person() {{
                            this.setFirstName("person3");
                            this.setLastName("3");
                            this.setBirthDate(LocalDate.of(1990, 1, 1));
                            this.setMedicalRecord(new MedicalRecord(Arrays.asList("1", "2"), Arrays.asList("01", "02")));
                        }}
                )));

        assertThat(medicalRecordService.findAll()
                                       .stream()
                                       .map(MedicalRecordDTO::getFirstName)
                                       .collect(Collectors.toList())).isEqualTo(personRepository.findAll()
                                                                                                .stream()
                                                                                                .map(MedicalRecordDTO::new)
                                                                                                .map(MedicalRecordDTO::getFirstName)
                                                                                                .collect(Collectors.toList()));
    }

    @Test
    public void update_shouldReturnTheUpdatedMedicalRecord_forAnExistingMedicalRecord() {

        Person person = new Person() {{
            this.setFirstName("John");
            this.setLastName("Boyd");
            this.setAddress("1509 Culver St");
            this.setCity("Culver");
            this.setZip(97451);
            this.setPhone("UPDATED");
            this.setEmail("jaboyd@email.com");
            this.setBirthDate(LocalDate.of(1984, 3, 6));
            this.setMedicalRecord(new MedicalRecord());
        }};

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO(person);

        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        MedicalRecordDTO medicalRecordUpdated = medicalRecordService.update(medicalRecordDTO);

        verify(personRepository, times(1)).save(person);

        assertThat(medicalRecordUpdated.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(medicalRecordUpdated.getLastName()).isEqualTo(person.getLastName());
        assertThat(medicalRecordUpdated.getBirthdate()).isEqualTo(person.getBirthDate());
        assertThat(medicalRecordUpdated.getMedications()).isEqualTo(person.getMedicalRecord().getMedications());
        assertThat(medicalRecordUpdated.getAllergies()).isEqualTo(person.getMedicalRecord().getAllergies());
    }

    @Test
    public void update_shouldReturnThisUpdatedMedicalRecord_forAnNotExist() {

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

        assertThrows(ResponseStatusException.class, () -> medicalRecordService.update(new MedicalRecordDTO(person)));
        verify(personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldRemoveTheMedicalRecordFromTheDatabase_ForMedicalRecordExisting() {

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setFirstName("Youssef");
        medicalRecordDTO.setLastName("SafeNet");
        medicalRecordDTO.setBirthdate(LocalDate.now());
        medicalRecordDTO.setMedications(Arrays.asList("1", "2"));
        medicalRecordDTO.setAllergies(Arrays.asList("1", "2"));


        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(new Person(medicalRecordDTO)));

        medicalRecordService.delete(medicalRecordDTO.getFirstName(), medicalRecordDTO.getLastName());

        verify(personRepository, times(1)).findByFullName(anyString(), anyString());
    }

    @Test
    public void delete_shouldThrowNoSuchElementException_ForMedicalRecordNotExisting() {

        when(personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> medicalRecordService.delete("", ""));
    }

    @Test
    public void create_shouldReturnMedicalRecord_ForNewMedicalRecord() {

        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO() {{

            this.setFirstName("firstName");
            this.setLastName("lastName");
            this.setBirthdate(LocalDate.of(2000, 1, 1));
            this.setMedications(Arrays.asList("medications1, medication2", "medication3"));
            this.setAllergies(Arrays.asList("allergie1", "allergie2"));
        }};

        Person person = new Person("firstName", "lastName", "address", 12345, "city", "phone", "email");

        when(this.personRepository.findByFullName(anyString(), anyString())).thenReturn(Optional.of(person));
        when(this.personRepository.save(any(Person.class))).thenReturn(person);

        MedicalRecordDTO medicalRecordDTOCreated = this.medicalRecordService.create(medicalRecordDTO);

        assertThat(medicalRecordDTOCreated.getLastName()).isEqualTo(medicalRecordDTO.getLastName());
        assertThat(medicalRecordDTOCreated.getFirstName()).isEqualTo(medicalRecordDTO.getFirstName());
        assertThat(medicalRecordDTOCreated.getBirthdate()).isEqualTo(medicalRecordDTO.getBirthdate());
        assertThat(medicalRecordDTOCreated.getMedications()).isEqualTo(medicalRecordDTO.getMedications());
        assertThat(medicalRecordDTOCreated.getAllergies()).isEqualTo(medicalRecordDTO.getAllergies());
    }
}

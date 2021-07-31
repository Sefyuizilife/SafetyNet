package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.entities.MedicalRecord;
import com.safetynet.safetynetalerts.entities.Person;
import com.safetynet.safetynetalerts.repositories.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final PersonRepository personRepository;

    MedicalRecordService(PersonRepository personRepository) {

        this.personRepository = personRepository;
    }

    public List<MedicalRecordDTO> findAll() {

        return this.personRepository.findAll()
                                    .stream()
                                    .map(MedicalRecordDTO::new)
                                    .collect(Collectors.toList());
    }

    public Optional<MedicalRecordDTO> findByFullName(String firstName, String lastName) {

        return Optional.of(
                new MedicalRecordDTO(this.personRepository.findByFullName(firstName, lastName)
                                                          .filter(item -> item.getBirthDate() != null)
                                                          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
        );
    }

    public MedicalRecordDTO create(MedicalRecordDTO medicalRecordDTO) {

        if (medicalRecordDTO.getBirthdate() == null || medicalRecordDTO.getMedications() == null || medicalRecordDTO
                                                                                                            .getAllergies() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthdate, medications or allergies parameters is missing");
        }

        Optional<Person> oPerson = this.personRepository.findByFullName(medicalRecordDTO.getFirstName(), medicalRecordDTO
                .getLastName());

        if (oPerson.isEmpty()) {

            return new MedicalRecordDTO(this.personRepository.save(new Person(medicalRecordDTO)));
        }

        Person person = oPerson.get();

        if (person.getBirthDate() == null && person.getMedicalRecord() == null) {

            person.setBirthDate(medicalRecordDTO.getBirthdate());
            person.setMedicalRecord(new MedicalRecord(medicalRecordDTO.getAllergies(), medicalRecordDTO.getMedications()));

            return new MedicalRecordDTO(this.personRepository.save(person));
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    public MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO) {

        if (medicalRecordDTO.getBirthdate() == null || medicalRecordDTO.getMedications() == null || medicalRecordDTO
                                                                                                            .getAllergies() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthdate, medications or allergies parameters is missing");
        }

        Person personToUpdate = personRepository.findByFullName(medicalRecordDTO.getFirstName(), medicalRecordDTO.getLastName())
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (personToUpdate.getMedicalRecord() == null & personToUpdate.getBirthDate() == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        personToUpdate.setBirthDate(medicalRecordDTO.getBirthdate());

        personToUpdate.setMedicalRecord(new MedicalRecord(medicalRecordDTO.getMedications(), medicalRecordDTO.getAllergies()));

        return new MedicalRecordDTO(this.personRepository.save(personToUpdate));
    }

    public void delete(String firstName, String lastName) {

        Optional<Person> oPerson = personRepository.findByFullName(firstName, lastName);

        if (oPerson.isPresent()) {

            Person person = oPerson.get();

            if (person.getBirthDate() != null) {

                person.setBirthDate(null);
                person.setMedicalRecord(null);
            } else {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

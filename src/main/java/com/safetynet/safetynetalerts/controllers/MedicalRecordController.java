package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("medicalrecord")
public class MedicalRecordController {

    private final MedicalRecordService
            medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {

        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("")
    public List<MedicalRecordDTO> browse() {

        return this.medicalRecordService.findAll();
    }

    @GetMapping("/read")
    public MedicalRecordDTO read(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) {

        return this.medicalRecordService.findByFullName(firstName, lastName)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public MedicalRecordDTO create(@RequestBody MedicalRecordDTO medicalRecordDTO) {

        return this.medicalRecordService.create(medicalRecordDTO);
    }

    @PutMapping("")
    public MedicalRecordDTO update(@RequestBody MedicalRecordDTO medicalRecordDTO) {

        return this.medicalRecordService.update(medicalRecordDTO);
    }

    @DeleteMapping("")
    public void delete(@RequestBody MedicalRecordDTO medicalRecordDTO) {

        this.medicalRecordService.delete(medicalRecordDTO.getFirstName(), medicalRecordDTO.getLastName());
    }


}

package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("medicalrecord")
public class MedicalRecordController {

    private static final Logger               LOGGER = LoggerFactory.getLogger(MedicalRecordController.class);
    private final        MedicalRecordService medicalRecordService;
    private final        HttpServletRequest   request;


    public MedicalRecordController(MedicalRecordService medicalRecordService, HttpServletRequest request) {

        this.medicalRecordService = medicalRecordService;
        this.request              = request;
    }

    @GetMapping("")
    public List<MedicalRecordDTO> browse() {

        LOGGER.info("GET: {}", this.request.getRequestURI());

        List<MedicalRecordDTO> medicalRecords = this.medicalRecordService.findAll();

        LOGGER.debug(Arrays.toString(medicalRecords.stream().map(MedicalRecordDTO::toJson).toArray()));
        return medicalRecords;
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
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}

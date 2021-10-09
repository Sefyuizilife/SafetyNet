package com.safetynet.safetynetalerts.controllers;

import com.safetynet.safetynetalerts.DTO.MedicalRecordDTO;
import com.safetynet.safetynetalerts.services.MedicalRecordService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    List<MedicalRecordDTO> medicalRecordDTOs = new ArrayList<>();

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MockMvc mockMvc;

    {
        medicalRecordDTOs.add(new MedicalRecordDTO() {{
            this.setFirstName("1");
            this.setLastName("Adult");
            this.setBirthdate(LocalDate.now());
            this.setMedications(Arrays.asList("1-medication1", "1-medication2", "1-medication3", "1-medication4"));
            this.setAllergies(Arrays.asList("1-allergie1", "1-allergie2", "1-allergie3", "1-allergie4"));
        }});
        medicalRecordDTOs.add(new MedicalRecordDTO() {{
            this.setFirstName("2");
            this.setLastName("Adult");
            this.setBirthdate(LocalDate.now());
            this.setMedications(Arrays.asList("2-medication1", "2-medication2", "2-medication3", "2-medication4"));
            this.setAllergies(Arrays.asList("2-allergie1", "2-allergie2", "2-allergie3", "2-allergie4"));
        }});
        medicalRecordDTOs.add(new MedicalRecordDTO() {{
            this.setFirstName("3");
            this.setLastName("Adult");
            this.setBirthdate(LocalDate.now());
            this.setMedications(Arrays.asList("3-medication1", "3-medication2", "3-medication3", "3-medication4"));
            this.setAllergies(Arrays.asList("3-allergie1", "3-allergie2", "3-allergie3", "3-allergie4"));
        }});
    }


    @Test
    public void browse_shouldAllMedicalRecords() throws Exception {

        when(this.medicalRecordService.findAll()).thenReturn(this.medicalRecordDTOs);

        mockMvc.perform(get("/medicalrecord"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void read_shouldReturnTheMedicalRecordCorrespondingToTheFirstAndLastName() throws Exception {

        MedicalRecordDTO medicalRecordDTO = this.medicalRecordDTOs.get(0);

        when(this.medicalRecordService.findByFullName(anyString(), anyString())).thenReturn(Optional.of(medicalRecordDTO));

        mockMvc.perform(get("/medicalrecord/read?firstName={firstName}&lastName={lastName}", 1, "Adult"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("1-medication1")))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void create_shouldReturnNewMedicalRecordCreated() throws Exception {

        MedicalRecordDTO medicalRecordDTO = this.medicalRecordDTOs.get(0);

        JSONObject body = new JSONObject() {{
            this.put("firstName", medicalRecordDTO.getFirstName());
            this.put("lastName", medicalRecordDTO.getLastName());
            this.put("birthdate", medicalRecordDTO.getBirthdate()
                                                  .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            this.put("medications", new JSONArray(medicalRecordDTO.getMedications()));
            this.put("allergies", new JSONArray(medicalRecordDTO.getAllergies()));
        }};

        when(this.medicalRecordService.create(any(MedicalRecordDTO.class))).thenReturn(medicalRecordDTO);

        mockMvc.perform(post("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(body.toString()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void update_shouldReturnTheUpdatedMedicalRecord_whenAMedicalRecordExists() throws Exception {

        MedicalRecordDTO medicalRecordDTO = this.medicalRecordDTOs.get(0);

        JSONObject body = new JSONObject() {{
            this.put("firstName", medicalRecordDTO.getFirstName());
            this.put("lastName", medicalRecordDTO.getLastName());
            this.put("birthdate", medicalRecordDTO.getBirthdate()
                                                  .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            this.put("medications", new JSONArray(medicalRecordDTO.getMedications()));
            this.put("allergies", new JSONArray(medicalRecordDTO.getAllergies()));
        }};

        when(this.medicalRecordService.findByFullName(anyString(), anyString())).thenReturn(Optional.of(medicalRecordDTO));
        when(this.medicalRecordService.update(any())).thenReturn(medicalRecordDTO);

        mockMvc.perform(put("/medicalrecord")
                       .contentType(MediaType.APPLICATION_JSON).content(body.toString()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void delete_shouldReturnVoid_whenAMedicalRecordExist() throws Exception {

        MedicalRecordDTO medicalRecordDTO = this.medicalRecordDTOs.get(0);

        JSONObject body = new JSONObject() {{
            this.put("firstName", medicalRecordDTO.getFirstName());
            this.put("lastName", medicalRecordDTO.getLastName());
            this.put("birthdate", medicalRecordDTO.getBirthdate()
                                                  .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
            this.put("medications", new JSONArray(medicalRecordDTO.getMedications()));
            this.put("allergies", new JSONArray(medicalRecordDTO.getAllergies()));
        }};

        when(this.medicalRecordService.findByFullName(anyString(), anyString())).thenReturn(Optional.of(medicalRecordDTO));

        mockMvc.perform(delete("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(body.toString()))
               .andExpect(status().isNoContent());

    }
}

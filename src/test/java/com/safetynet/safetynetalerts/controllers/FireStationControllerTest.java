package com.safetynet.safetynetalerts.controllers;


import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.repositories.FireStationRepository;
import com.safetynet.safetynetalerts.services.FireStationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
public class FireStationControllerTest {

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private FireStationRepository fireStationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void browse_shouldAllFireStations() throws Exception {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1L);
        fireStation.setAddress("5, rue des coquelicots");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(fireStationService.findAll()).thenReturn(fireStations);

        mockMvc.perform(get("/firestation"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("coquelicots")))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(fireStationService, times(1)).findAll();
    }

    @Test
    public void read_shouldReturnTheFireStationCorrespondingToTheFirstAndLastName() throws Exception {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1L);
        fireStation.setAddress("5, rue des coquelicots");

        when(this.fireStationService.findByAddress(anyString())).thenReturn(Optional.of(fireStation));

        mockMvc.perform(get("/firestation/read?address=5%2C%20rue%20des%20coquelicots"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("coquelicots")))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void save_shouldReturnNewFireStationCreated() throws Exception {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1L);
        fireStation.setAddress("5, rue des coquelicots");

        JSONObject fireStationJson = new JSONObject() {{
            this.put("station", fireStation.getStation());
            this.put("address", fireStation.getAddress());
        }};

        when(this.fireStationService.save(any(FireStation.class))).thenReturn(fireStation);

        mockMvc.perform(post("/firestation")
                .content(String.valueOf(fireStationJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(String.valueOf(fireStationJson)));
    }

    @Test
    public void update_shouldReturnTheUpdatedFireStation_whenAFireStationExists() throws Exception {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1L);
        fireStation.setAddress("5, rue des coquelicots");

        JSONObject fireStationJson = new JSONObject() {{
            this.put("station", fireStation.getStation());
            this.put("address", fireStation.getAddress());
        }};

        when(this.fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));
        when(this.fireStationService.update(any())).thenReturn(fireStation);

        mockMvc.perform(put("/firestation")
                .content(String.valueOf(fireStationJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(String.valueOf(fireStationJson)));
    }

    @Test
    public void delete_shouldReturnVoid_whenAFireStationExist() throws Exception {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1L);
        fireStation.setAddress("5, rue des coquelicots");

        JSONObject fireStationJson = new JSONObject() {{
            this.put("station", fireStation.getStation());
            this.put("address", fireStation.getAddress());
        }};

        when(this.fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));

        mockMvc.perform(delete("/firestation")
                .content(String.valueOf(fireStationJson))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());

    }
}
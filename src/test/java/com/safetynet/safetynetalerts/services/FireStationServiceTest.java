package com.safetynet.safetynetalerts.services;

import com.safetynet.safetynetalerts.entities.FireStation;
import com.safetynet.safetynetalerts.repositories.FireStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    List<FireStation> fireStations = new ArrayList<>();

    @Mock
    private FireStationRepository fireStationRepository;

    private FireStationService fireStationService;

    {
        fireStations.addAll(
                Arrays.asList(
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }},
                        new FireStation() {{
                            this.setStation(1L);
                            this.setAddress("address1");
                        }}
                )
        );
    }

    @BeforeEach
    public void setUpPerTest() {

        this.fireStationService = new FireStationService(fireStationRepository);
    }

    @Test
    public void findAll_shouldReturnAllFireStationOfDatabase() {

        when(fireStationRepository.findAll()).thenReturn(new ArrayList<>());

        assertThat(fireStationService.findAll()).isEqualTo(fireStationRepository.findAll());
    }

    @Test
    public void update_shouldReturnTheUpdatedFireStation_forAnExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        when(fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));
        when(fireStationRepository.save(any(FireStation.class))).thenReturn(fireStation);

        FireStation fireStationUpdated = fireStationService.update(fireStation);

        verify(this.fireStationRepository, times(1)).save(fireStation);

        assertThat(fireStationUpdated).isEqualTo(fireStation);
    }

    @Test
    public void update_shouldReturnThisUpdatedFireStation_forAnNotExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        assertThrows(ResponseStatusException.class, () -> this.fireStationService.update(fireStation));
        verify(this.fireStationRepository, times(1)).findByAddress(anyString());
    }

    @Test
    public void delete_shouldRemoveTheFireStationFromTheDatabase_forFireStationExisting() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        when(this.fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));

        fireStationService.delete(fireStation.getAddress());

        verify(this.fireStationRepository, times(1)).findByAddress(anyString());
    }

    @Test
    public void delete_shouldThrowNoSuchElementException_forFireStationNotExisting() {

        when(this.fireStationRepository.findByAddress(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> fireStationService.delete(""));
    }

    @Test
    public void save_shouldReturnFireStationSaved_forNewFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setStation(999L);
        fireStation.setAddress("rue des coquelicots");

        when(this.fireStationRepository.isExisting(any(FireStation.class))).thenReturn(false);
        when(this.fireStationRepository.save(any(FireStation.class))).thenReturn(fireStation);

        FireStation fireStationSaved = this.fireStationService.save(fireStation);

        assertThat(fireStationSaved).isEqualTo(fireStation);
    }
}

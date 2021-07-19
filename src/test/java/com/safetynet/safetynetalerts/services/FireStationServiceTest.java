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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;

    private FireStationService fireStationService;

    @BeforeEach
    public void setUpPerTest() {

        this.fireStationService = new FireStationService(fireStationRepository);
    }

    @Test
    public void findAll_shouldReturnAllFireStationOfDatabase() {

        when(fireStationRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new FireStation(),
                new FireStation(),
                new FireStation(),
                new FireStation(),
                new FireStation(),
                new FireStation(),
                new FireStation(),
                new FireStation()
        )));

        assertThat(fireStationService.findAll()).isEqualTo(fireStationRepository.findAll());
    }

    @Test
    public void update_shouldReturnTheUpdatedFireStation_forAnExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        when(fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));
        when(fireStationRepository.save(any(FireStation.class))).thenReturn(fireStation);

        FireStation fireStationUpdated = fireStationService.update(fireStation);

        verify(fireStationRepository, times(1)).save(fireStation);

        assertThat(fireStationUpdated).isEqualTo(fireStation);
    }

    @Test
    public void update_shouldReturnThisUpdatedFireStation_forAnNotExistingFireStation() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        assertThrows(ResponseStatusException.class, () -> fireStationService.update(fireStation));
        verify(fireStationRepository, times(1)).findByAddress(anyString());
    }

    @Test
    public void delete_shouldRemoveTheFireStationFromTheDatabase_ForFireStationExisting() {

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");

        when(fireStationRepository.findByAddress(anyString())).thenReturn(Optional.of(fireStation));

        fireStationService.delete(fireStation.getAddress());

        verify(fireStationRepository, times(1)).findByAddress(anyString());
    }

    @Test
    public void delete_shouldThrowNoSuchElementException_ForFireStationNotExisting() {

        when(fireStationRepository.findByAddress(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> fireStationService.delete(""));
    }
}

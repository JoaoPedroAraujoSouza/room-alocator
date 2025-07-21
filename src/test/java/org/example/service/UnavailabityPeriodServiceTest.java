package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.UnavailabityPeriod;
import org.example.repository.UnavailabityPeriodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnavailabityPeriodServiceTest {

    @Mock
    private UnavailabityPeriodRepository periodRepository;

    @InjectMocks
    private UnavailabityPeriodService periodService;

    private UnavailabityPeriod period;

    @BeforeEach
    void setUp() {
        period = new UnavailabityPeriod(
                1L,
                UUID.randomUUID(),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "Manutenção Preventiva"
        );
    }

    @Test
    void testGetAllPeriods() {
        when(periodRepository.findAll()).thenReturn(List.of(period));

        List<UnavailabityPeriod> periods = periodService.getAll();

        assertNotNull(periods);
        assertEquals(1, periods.size());
    }

    @Test
    void testGetPeriodById_Success() {
        when(periodRepository.findById(1L)).thenReturn(Optional.of(period));

        Optional<UnavailabityPeriod> foundPeriod = periodService.getById(1L);

        assertTrue(foundPeriod.isPresent());
        assertEquals("Manutenção Preventiva", foundPeriod.get().getReason());
    }

    @Test
    void testAddPeriod_Success() throws IOException {
        periodService.add(period);
        verify(periodRepository, times(1)).create(period);
    }

    @Test
    void testUpdatePeriod_Success() throws IOException {
        periodService.update(period);
        verify(periodRepository, times(1)).update(period);
    }

    @Test
    void testDeletePeriodById_Success() throws IOException {
        periodService.deleteById(1L);
        verify(periodRepository, times(1)).deleteById(1L);
    }
}
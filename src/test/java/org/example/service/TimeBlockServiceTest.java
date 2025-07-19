package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.example.models.TimeBlock;
import org.example.repository.TimeBlockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeBlockServiceTest {

    @Mock
    private TimeBlockRepository timeBlockRepository;

    @InjectMocks
    private TimeBlockService timeBlockService;

    private TimeBlock timeBlock;

    @BeforeEach
    void setUp() {
        timeBlock = new TimeBlock(
                1L,
                DayOfWeek.FRIDAY,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );
    }

    @Test
    void testGetAllTimeBlocks() {
        when(timeBlockRepository.findAll()).thenReturn(List.of(timeBlock));

        List<TimeBlock> timeBlocks = timeBlockService.getAll();

        assertNotNull(timeBlocks);
        assertEquals(1, timeBlocks.size());
    }

    @Test
    void testGetTimeBlockById_Success() {
        when(timeBlockRepository.findById(1L)).thenReturn(Optional.of(timeBlock));

        Optional<TimeBlock> foundTimeBlock = timeBlockService.getById(1L);

        assertTrue(foundTimeBlock.isPresent());
        assertEquals(DayOfWeek.FRIDAY, foundTimeBlock.get().getDayOfWeek());
    }

    @Test
    void testAddTimeBlock_Success() throws IOException {
        timeBlockService.add(timeBlock);
        verify(timeBlockRepository, times(1)).create(timeBlock);
    }

    @Test
    void testUpdateTimeBlock_Success() throws IOException {
        timeBlockService.update(timeBlock);
        verify(timeBlockRepository, times(1)).update(timeBlock);
    }

    @Test
    void testDeleteTimeBlockById_Success() throws IOException {
        timeBlockService.deleteById(1L);
        verify(timeBlockRepository, times(1)).deleteById(1L);
    }
}
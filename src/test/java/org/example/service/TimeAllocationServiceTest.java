package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.example.exceptions.ConflictException;
import org.example.models.*;
import org.example.repository.TimeAllocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeAllocationServiceTest {

    @Mock
    private TimeAllocationRepository timeAllocationRepository;

    @InjectMocks
    private TimeAllocationService timeAllocationService;

    private Teacher teacher1;
    private Room room1;
    private Classroom classroom1;
    private TimeBlock timeBlock1;
    private TimeAllocation allocation1;

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher(1L, UUID.randomUUID(), "Prof. A", "111.111.111-11", "a@test.com");
        room1 = new Room(1L, UUID.randomUUID(), "Sala 101", "Bloco A", 30, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        classroom1 = new Classroom(1L, UUID.randomUUID(), "2025.1", Shift.MORNING, 25, teacher1, new ArrayList<>());
        timeBlock1 = new TimeBlock(1L, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        allocation1 = new TimeAllocation(1L, UUID.randomUUID(), classroom1, room1, timeBlock1);
    }

    @Test
    void testAddAllocation_Success() throws ConflictException, IOException {
        when(timeAllocationRepository.findAll()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> timeAllocationService.add(allocation1));

        verify(timeAllocationRepository, times(1)).create(allocation1);
    }

    @Test
    void testAddAllocation_Failure_RoomCapacityTooSmall() {
        room1.setCapacity(20); // Capacidade da sala (20) < alunos da turma (25)

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            timeAllocationService.add(allocation1);
        });

        assertEquals("Room capacity must be greater than or equal to Classroom max students capacity.", exception.getMessage());
    }

    @Test
    void testAddAllocation_Failure_RoomIsUnavailable() {
        UnavailabityPeriod period = new UnavailabityPeriod(1L, UUID.randomUUID(), LocalTime.of(9, 0), LocalTime.of(11, 0), "Manutenção");
        room1.setUnavailabilityPeriods(List.of(period));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            timeAllocationService.add(allocation1);
        });

        assertEquals("The room is unavailable during the specified time block.", exception.getMessage());
    }

    @Test
    void testAddAllocation_Failure_TeacherConflict() {
        Teacher teacher2 = new Teacher(2L, UUID.randomUUID(), "Prof. B", "222.222.222-22", "b@test.com");
        Room room2 = new Room(2L, UUID.randomUUID(), "Sala 102", "Bloco B", 30, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Classroom classroom2 = new Classroom(2L, UUID.randomUUID(), "2025.1", Shift.MORNING, 25, teacher1, new ArrayList<>()); // Mesmo professor
        TimeBlock timeBlock2 = new TimeBlock(2L, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)); // Horário conflitante
        TimeAllocation existingAllocation = new TimeAllocation(2L, UUID.randomUUID(), classroom2, room2, timeBlock2);

        when(timeAllocationRepository.findAll()).thenReturn(List.of(existingAllocation));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            timeAllocationService.add(allocation1);
        });

        assertEquals("The teacher is already allocated during this time block.", exception.getMessage());
    }

    @Test
    void testAddAllocation_Failure_RoomConflict() {
        Teacher teacher2 = new Teacher(2L, UUID.randomUUID(), "Prof. B", "222.222.222-22", "b@test.com");
        Classroom classroom2 = new Classroom(2L, UUID.randomUUID(), "2025.1", Shift.MORNING, 25, teacher2, new ArrayList<>());
        TimeBlock timeBlock2 = new TimeBlock(2L, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)); // Horário conflitante
        TimeAllocation existingAllocation = new TimeAllocation(2L, UUID.randomUUID(), classroom2, room1, timeBlock2); // Mesma sala

        when(timeAllocationRepository.findAll()).thenReturn(List.of(existingAllocation));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            timeAllocationService.add(allocation1);
        });

        assertEquals("The room is already allocated during this time block.", exception.getMessage());
    }
}
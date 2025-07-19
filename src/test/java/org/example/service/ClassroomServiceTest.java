package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.Classroom;
import org.example.models.Shift;
import org.example.models.Teacher;
import org.example.repository.ClassroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomService classroomService;

    private Classroom classroom;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(1L, UUID.randomUUID(), "Prof. Ada", "333.333.333-33", "ada@test.com");
        classroom = new Classroom(
                1L,
                UUID.randomUUID(),
                "2025.1",
                Shift.EVENING,
                40,
                teacher,
                new ArrayList<>()
        );
    }

    @Test
    void testGetAllClassrooms() {
        when(classroomRepository.findAll()).thenReturn(List.of(classroom));

        List<Classroom> classrooms = classroomService.getAll();

        assertNotNull(classrooms);
        assertEquals(1, classrooms.size());
        assertEquals("2025.1", classrooms.get(0).getSemester());
    }

    @Test
    void testGetClassroomById_Success() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        Optional<Classroom> foundClassroom = classroomService.getById(1L);

        assertTrue(foundClassroom.isPresent());
        assertEquals(classroom.getId(), foundClassroom.get().getId());
    }

    @Test
    void testAddClassroom_Success() throws IOException {
        classroomService.add(classroom);
        verify(classroomRepository, times(1)).create(classroom);
    }

    @Test
    void testUpdateClassroom_Success() throws IOException {
        classroomService.update(classroom);
        verify(classroomRepository, times(1)).update(classroom);
    }

    @Test
    void testDeleteClassroomById_Success() throws IOException {
        classroomService.deleteById(1L);
        verify(classroomRepository, times(1)).deleteById(1L);
    }
}
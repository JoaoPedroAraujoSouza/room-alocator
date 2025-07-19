package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.models.Teacher;
import org.example.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(
                1L,
                UUID.randomUUID(),
                "John Doe",
                "123.456.789-00",
                "john.doe@example.com"
        );
    }

    @Test
    void testAddTeacher_Success() throws ValidationException, IOException {
        teacherService.add(teacher);
        verify(teacherRepository, times(1)).create(teacher);
    }

    @Test
    void testAddTeacher_Failure_InvalidName() {
        teacher.setName("");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            teacherService.add(teacher);
        });
        assertEquals("Teacher name cannot be empty", exception.getMessage());
    }

    @Test
    void testAddTeacher_Failure_InvalidCPF() {
        teacher.setCpf("123456");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            teacherService.add(teacher);
        });
        assertEquals("Teacher CPF invalid", exception.getMessage());
    }

    @Test
    void testUpdateTeacher_Success() throws ValidationException, NotFoundException, IOException {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        teacher.setName("John Doe Updated");
        teacherService.update(teacher);
        verify(teacherRepository, times(1)).update(teacher);
    }

    @Test
    void testUpdateTeacher_Failure_NotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            teacherService.update(teacher);
        });
    }
}
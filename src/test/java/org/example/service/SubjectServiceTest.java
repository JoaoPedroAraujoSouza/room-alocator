package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.Subject;
import org.example.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject(
                1L,
                UUID.randomUUID(),
                "Cálculo I",
                "Estudo de limites, derivadas e integrais.",
                60
        );
    }

    @Test
    void testGetAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(List.of(subject));

        List<Subject> subjects = subjectService.getAll();

        assertNotNull(subjects);
        assertEquals(1, subjects.size());
    }

    @Test
    void testGetSubjectById_Success() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Optional<Subject> foundSubject = subjectService.getById(1L);

        assertTrue(foundSubject.isPresent());
        assertEquals(subject.getName(), foundSubject.get().getName());
    }

    @Test
    void testAddSubject_Success() throws IOException {
        subjectService.add(subject);
        verify(subjectRepository, times(1)).create(subject);
    }

    @Test
    void testUpdateSubject_Success() throws IOException {
        subjectService.update(subject);
        verify(subjectRepository, times(1)).update(subject);
    }

    @Test
    void testDeleteSubjectById_Success() throws IOException {
        subjectService.deleteById(1L);
        verify(subjectRepository, times(1)).deleteById(1L);
    }
}
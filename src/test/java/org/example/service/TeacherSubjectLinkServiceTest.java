package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.*;
import org.example.repository.TeacherSubjectLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeacherSubjectLinkServiceTest {

    @Mock
    private TeacherSubjectLinkRepository linkRepository;

    @InjectMocks
    private TeacherSubjectLinkService linkService;

    private TeacherSubjectLink link;
    private Teacher teacher;
    private Subject subject;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
        teacher = new Teacher(1L, UUID.randomUUID(), "Prof. X", "444.444.444-44", "x@test.com");
        subject = new Subject(1L, UUID.randomUUID(), "Algoritmos", "Estruturas de dados.", 60);
        classroom = new Classroom(1L, UUID.randomUUID(), "2025.1", Shift.MORNING, 30, teacher, new ArrayList<>());

        link = new TeacherSubjectLink(
                1L,
                UUID.randomUUID(),
                teacher,
                subject,
                "2025.1",
                true,
                classroom
        );
    }

    @Test
    void testGetAllLinks() {
        when(linkRepository.findAll()).thenReturn(List.of(link));

        List<TeacherSubjectLink> links = linkService.getAll();

        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals("Prof. X", links.get(0).getTeacher().getName());
    }

    @Test
    void testGetLinkById_Success() {
        when(linkRepository.findById(1L)).thenReturn(Optional.of(link));

        Optional<TeacherSubjectLink> foundLink = linkService.getById(1L);

        assertTrue(foundLink.isPresent());
        assertEquals(link.getId(), foundLink.get().getId());
    }

    @Test
    void testAddLink_Success() throws IOException {
        linkService.add(link);
        verify(linkRepository, times(1)).create(link);
    }

    @Test
    void testUpdateLink_Success() throws IOException {
        linkService.update(link);
        verify(linkRepository, times(1)).update(link);
    }

    @Test
    void testDeleteLinkById_Success() throws IOException {
        linkService.deleteById(1L);
        verify(linkRepository, times(1)).deleteById(1L);
    }
}
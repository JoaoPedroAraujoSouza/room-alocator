package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.models.Room;
import org.example.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(
                1L,
                UUID.randomUUID(),
                "Sala de Conferências",
                "Bloco C",
                50,
                new ArrayList<>(List.of("Projetor", "Quadro Branco")),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Test
    void testGetAllRooms_Success() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> rooms = roomService.getAll();

        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("Sala de Conferências", rooms.get(0).getName());
    }

    @Test
    void testGetRoomById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Optional<Room> foundRoom = roomService.getById(1L);

        assertTrue(foundRoom.isPresent());
        assertEquals(room.getId(), foundRoom.get().getId());
    }

    @Test
    void testGetRoomById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> foundRoom = roomService.getById(1L);

        assertFalse(foundRoom.isPresent());
    }

    @Test
    void testAddRoom_Success() throws IOException {
        roomService.add(room);
        verify(roomRepository, times(1)).create(room);
    }

    @Test
    void testUpdateRoom_Success() throws IOException {
        roomService.update(room);
        verify(roomRepository, times(1)).update(room);
    }

    @Test
    void testDeleteRoomById_Success() throws IOException {
        roomService.deleteById(1L);
        verify(roomRepository, times(1)).deleteById(1L);
    }
}
package org.example.service.interfaces;

import org.example.models.Room;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RoomServiceInterface {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(long id);
    void addRoom(Room room) throws IOException;
    void updateRoom(Room room) throws IOException;
    void deleteRoomById(long id) throws IOException;
}
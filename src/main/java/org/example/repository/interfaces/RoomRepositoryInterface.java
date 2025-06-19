package org.example.repository.interfaces;

import org.example.models.Room;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RoomRepositoryInterface {
    List<Room> findAll();
    Optional<Room> findById(long id);
    void create(Room room) throws IOException;
    void update(Room room) throws IOException;
    void deleteById(long id) throws IOException;
}
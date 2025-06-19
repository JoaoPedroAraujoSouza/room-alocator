package org.example.service;

import org.example.models.Room;
import org.example.repository.RoomRepository;
import org.example.service.interfaces.RoomServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RoomService extends AbstractService<Room> implements RoomServiceInterface {
    private final RoomRepository repository = new RoomRepository();

    @Override
    public List<Room> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Room> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Room room) throws IOException {
        repository.create(room);
    }

    @Override
    public void update(Room room) throws IOException {
        repository.update(room);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return getAll();
    }

    @Override
    public Optional<Room> getRoomById(long id) {
        return getById(id);
    }

    @Override
    public void addRoom(Room room) throws IOException {
        add(room);
    }

    @Override
    public void updateRoom(Room room) throws IOException {
        update(room);
    }

    @Override
    public void deleteRoomById(long id) throws IOException {
        deleteById(id);
    }
}
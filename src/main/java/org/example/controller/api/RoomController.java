package org.example.controller.api;

import java.util.List;
import java.util.Optional;

import org.example.models.Room;
import org.example.service.RoomService;

public class RoomController extends AbstractController<Room>{
    private final RoomService service = new RoomService();

    @Override
    public List<Room> getAll() {
        return service.getAllRooms();
    }

    @Override
    public Optional<Room> getById(long id) {
        return service.getRoomById(id);
    }

    @Override
    public boolean create(Room room) {
        try {
            service.addRoom(room);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Room room) {
        try {
            service.updateRoom(room);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteRoomById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }
}

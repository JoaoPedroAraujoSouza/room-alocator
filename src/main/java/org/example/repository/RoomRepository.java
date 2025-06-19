package org.example.repository;

import org.example.models.Room;
import org.example.repository.interfaces.RoomRepositoryInterface;

public class RoomRepository extends AbstractRepository<Room> implements RoomRepositoryInterface {
    public RoomRepository() {
        super("rooms.dat");
    }

    @Override
    protected long getId(Room room) {
        return room.getId();
    }
}
package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.example.models.Room;
import org.example.service.RoomService;

public class Main {
    public static void main(String[] args) {

        RoomService roomService = new RoomService();

        try {
            Room room = new Room(
                    1L,
                    UUID.randomUUID(),
                    "Room 101",
                    "Building A",
                    30,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            roomService.add(room);
            System.out.println("Created: " + room);

            Optional<Room> fetched = roomService.getById(1L);
            fetched.ifPresent(r -> System.out.println("Fetched: " + r));

            if (fetched.isPresent()) {
                Room toUpdate = fetched.get();
                toUpdate.setCapacity(40);
                roomService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }

            roomService.deleteById(1L);
            System.out.println("Deleted room with id 1");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
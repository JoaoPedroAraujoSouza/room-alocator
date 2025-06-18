package org.example.models;

import java.util.UUID;

public class TimeAllocation {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private Classroom classroom;
    private Room room;
    private TimeBlock timeBlock;

    public TimeAllocation(UUID uuid, Classroom classroom, Room room, TimeBlock timeBlock) {
        this.id = nextId++;
        this.uuid = uuid;
        this.classroom = classroom;
        this.room = room;
        this.timeBlock = timeBlock;
    }
}
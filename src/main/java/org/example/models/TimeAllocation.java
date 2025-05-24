package org.example.models;

import java.util.UUID;

public class TimeAllocation {
    private UUID id;
    private Classroom classroom;
    private Room room;
    private TimeBlock timeblock;

    public TimeAllocation(UUID id, Classroom classroom, Room room, TimeBlock timeblock) {
        this.id = id;
        this.classroom = classroom;
        this.room = room;
        this.timeblock = timeblock;
    }
}

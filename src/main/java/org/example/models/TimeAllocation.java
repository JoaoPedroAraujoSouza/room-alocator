package org.example.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class TimeAllocation {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private Classroom classroom;
    private Room room;
    private TimeBlock timeBlock;
}
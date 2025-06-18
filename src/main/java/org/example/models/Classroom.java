package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@Getter
@Setter

public class Classroom {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String semester;
    private Shift shift;
    private int studentsQuantity;
    private Teacher teacher;
    private List<TimeAllocation> timeAllocations;
}
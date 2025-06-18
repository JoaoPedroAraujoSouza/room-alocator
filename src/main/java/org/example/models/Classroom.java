package org.example.models;

import java.util.List;
import java.util.UUID;

public class Classroom {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String semester;
    private Shift shift;
    private int studentsQuantity;
    private Teacher teacher;
    private List<TimeAllocation> timeAllocations;

    public Classroom(UUID uuid, String semester, int studentsQuantity, Teacher teacher, List<TimeAllocation> timeAllocations) {
        this.id = nextId++;
        this.uuid = uuid;
        this.semester = semester;
        this.studentsQuantity = studentsQuantity;
        this.teacher = teacher;
        this.timeAllocations = timeAllocations;
    }
}
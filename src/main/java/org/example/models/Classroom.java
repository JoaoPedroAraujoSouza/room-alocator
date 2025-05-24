package org.example.models;

import java.util.List;
import java.util.UUID;

public class Classroom {
    private UUID id;
    private String semester;
    private Shift shift;
    private int studentsQuantity;
    private Teacher teacher;
    private List<TimeAllocation> timeAllocations;

    public Classroom(UUID id, String semester, int studentsQuantity, Teacher teacher, List<TimeAllocation> timeAllocations) {
        this.id = id;
        this.semester = semester;
        this.studentsQuantity = studentsQuantity;
        this.teacher = teacher;
        this.timeAllocations = timeAllocations;
    }
}

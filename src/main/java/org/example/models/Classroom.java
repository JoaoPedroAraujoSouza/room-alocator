package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
@Getter
@Setter

public class Classroom implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String semester;
    private Shift shift;
    private int maxStudentsCapacity;
    private Teacher responsibleTeacher;
    private List<TimeAllocation> timeAllocations;
}
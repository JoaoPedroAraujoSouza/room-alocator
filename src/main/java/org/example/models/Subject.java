package org.example.models;

import java.util.List;
import java.util.UUID;

public class Subject {
    private UUID id;
    private String name;
    private String description;
    private String hourlyLoad;
    private List<Teacher> teachers;

    public Subject(UUID id, String name, String hourlyLoad, List<Teacher> teachers, String description) {
        this.id = id;
        this.name = name;
        this.hourlyLoad = hourlyLoad;
        this.teachers = teachers;
        this.description = description;
    }
}

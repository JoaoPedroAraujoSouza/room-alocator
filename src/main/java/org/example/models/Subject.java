package org.example.models;

import java.util.List;
import java.util.UUID;

public class Subject {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String name;
    private String description;
    private String hourlyLoad;
    private List<Teacher> teachers;

    public Subject(UUID uuid, String name, String hourlyLoad, List<Teacher> teachers, String description) {
        this.id = nextId++;
        this.uuid = uuid;
        this.name = name;
        this.hourlyLoad = hourlyLoad;
        this.teachers = teachers;
        this.description = description;
    }
}
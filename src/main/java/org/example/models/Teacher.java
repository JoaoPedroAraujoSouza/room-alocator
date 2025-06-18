package org.example.models;

import java.util.List;
import java.util.UUID;

public class Teacher {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String name;
    private String cpf;
    private String email;
    private List<Subject> subjects;
    private List<Classroom> classrooms;

    public Teacher(UUID uuid, String name, String cpf, List<Subject> subjects, String email) {
        this.id = nextId++;
        this.uuid = uuid;
        this.name = name;
        this.cpf = cpf;
        this.subjects = subjects;
        this.email = email;
    }
}
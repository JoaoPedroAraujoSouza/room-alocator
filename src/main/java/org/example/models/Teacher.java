package org.example.models;

import java.util.List;
import java.util.UUID;

public class Teacher {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private List<Subject> subjects;
    private List<Classroom> classrooms;

    public Teacher(UUID id, String name, String cpf, List<Subject> subjects, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.subjects = subjects;
        this.email = email;
    }
}
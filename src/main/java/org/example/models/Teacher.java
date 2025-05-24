package org.example.models;

import java.util.List;
import java.util.UUID;

public class Teacher {
    private UUID id;
    private String name;
    private String cpf;
    private List<Subject> subjects;
    private List<Classroom> classrooms;
}

package org.example.models;

import java.util.UUID;

public class TeacherSubjectLink {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private Teacher teacher;
    private Subject subject;
    private String semester;
    private boolean active;

    public TeacherSubjectLink(UUID uuid, Teacher teacher, Subject subject, String semester, boolean active) {
        this.id = nextId++;
        this.uuid = uuid;
        this.teacher = teacher;
        this.subject = subject;
        this.semester = semester;
        this.active = active;
    }
}
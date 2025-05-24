package org.example.models;

import java.util.UUID;

public class TeacherSubjectLink {
    private UUID id;
    private Teacher teacher;
    private Subject subject;
    private String semester;
    private boolean active;

    public TeacherSubjectLink(UUID id, Teacher teacher, Subject subject, String semester, boolean active) {
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
        this.semester = semester;
        this.active = active;
    }
}

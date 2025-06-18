package org.example.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class TeacherSubjectLink {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private Teacher teacher;
    private Subject subject;
    private String semester;
    private boolean active;
}
package org.example.models;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class TeacherSubjectLink implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Teacher teacher;
    private Subject subject;
    private String semester;
    private boolean active;
    private Classroom classroom;
}
package org.example.models;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class Subject {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String name;
    private String description;
    private String hourlyLoad;
    private List<Teacher> teachers;
}
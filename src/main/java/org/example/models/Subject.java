package org.example.models;

import java.util.List;
import java.util.UUID;

public class Subject {
    private UUID id;
    private String name;
    private String description;
    private String hourlyLoad;
    private List<Teacher> teachers;
}

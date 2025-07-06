package org.example.models;

import java.io.Serializable;
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

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String name;
    private String description;
    private int hourlyLoad;

    public static synchronized long getNextId() {
        return nextId++;
    }
}
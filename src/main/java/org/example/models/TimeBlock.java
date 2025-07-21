package org.example.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class TimeBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    private static long nextId = 1;
    private final long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    
    public static long getNextId() {
        return nextId++;
    }
}
package org.example.models;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeBlock {
    private static long nextId = 1;
    private final long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeBlock(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = nextId++;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

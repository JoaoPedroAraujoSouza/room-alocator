package org.example.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public class TimeBlock {
    private UUID id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}

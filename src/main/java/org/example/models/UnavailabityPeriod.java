package org.example.models;

import java.time.LocalTime;
import java.util.UUID;

public class UnavailabityPeriod {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private LocalTime startDate;
    private LocalTime endDate;
    private String reason;

    public UnavailabityPeriod(UUID uuid, LocalTime startDate, LocalTime endDate) {
        this.id = nextId++;
        this.uuid = uuid;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
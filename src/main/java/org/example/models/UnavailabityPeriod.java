package org.example.models;

import java.time.LocalTime;
import java.util.UUID;

public class UnavailabityPeriod {
    private UUID id;
    private LocalTime startDate;
    private LocalTime endDate;
    private String reason;

    public UnavailabityPeriod(UUID id, LocalTime startDate, LocalTime endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

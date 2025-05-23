package org.example.models;

import java.util.UUID;
import java.time.LocalTime;

public class UnavailabityPeriod {
    private UUID id;
    private LocalTime startDate;
    private LocalTime endDate;
    private String reason;
}

package org.example.models;

import java.time.LocalTime;
import java.util.UUID;

public class UnavailabityPeriod {
    private UUID id;
    private LocalTime startDate;
    private LocalTime endDate;
    private String reason;
}

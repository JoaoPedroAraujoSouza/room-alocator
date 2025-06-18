package org.example.models;

import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Setter

public class UnavailabityPeriod {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private LocalTime startDate;
    private LocalTime endDate;
    private String reason;
}
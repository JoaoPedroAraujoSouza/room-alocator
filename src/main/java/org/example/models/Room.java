package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter

public class Room {
    private static long nextId = 1;
    private final long id;
    private UUID uuid;
    private String name;
    private String localization;
    private int capacity;
    private List<String> resources;
    private List<UnavailabityPeriod> unavailabilityPeriods;
    private List<TimeAllocation> timeAllocations;
}
package org.example.models;

import java.util.List;
import java.util.UUID;

public class Room {
    private UUID id;
    private String name;
    private String localization;
    private int capacity;
    private List<String> resources;
    private List<UnavailabityPeriod> unavailabilityPeriods;
    private List<TimeAllocation> timeAllocations;

    public Room(UUID id, String name, String localization, int capacity, List<String> resources, List<UnavailabityPeriod> unavailabilityPeriods, List<TimeAllocation> timeAllocations ) {
        this.id = id;
        this.name = name;
        this.localization = localization;
        this.capacity = capacity;
        this.resources = resources;
        this.unavailabilityPeriods = unavailabilityPeriods;
        this.timeAllocations = timeAllocations;
    }
}

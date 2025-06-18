package org.example.models;

import java.util.List;
import java.util.UUID;

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

    public Room(UUID uuid, String name, String localization, int capacity, List<String> resources, List<UnavailabityPeriod> unavailabilityPeriods, List<TimeAllocation> timeAllocations) {
        this.id = nextId++;
        this.uuid = uuid;
        this.name = name;
        this.localization = localization;
        this.capacity = capacity;
        this.resources = resources;
        this.unavailabilityPeriods = unavailabilityPeriods;
        this.timeAllocations = timeAllocations;
    }
}
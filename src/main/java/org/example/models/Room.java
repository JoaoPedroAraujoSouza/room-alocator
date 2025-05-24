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
}

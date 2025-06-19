package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String localization;
    private int capacity;
    private List<String> resources;
    private List<UnavailabityPeriod> unavailabilityPeriods;
    private List<TimeAllocation> timeAllocations;
}
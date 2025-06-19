package org.example.repository;

import org.example.models.TimeAllocation;
import org.example.repository.interfaces.TimeAllocationRepositoryInterface;

public class TimeAllocationRepository extends AbstractRepository<TimeAllocation> implements TimeAllocationRepositoryInterface {
    public TimeAllocationRepository() {
        super("time_allocations.dat");
    }

    @Override
    protected long getId(TimeAllocation allocation) {
        return allocation.getId();
    }
}
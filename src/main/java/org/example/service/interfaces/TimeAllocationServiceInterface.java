package org.example.service.interfaces;

import org.example.models.TimeAllocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TimeAllocationServiceInterface {
    List<TimeAllocation> getAllAllocations();
    Optional<TimeAllocation> getAllocationById(long id);
    void addAllocation(TimeAllocation allocation) throws IOException;
    void updateAllocation(TimeAllocation allocation) throws IOException;
    void deleteAllocationById(long id) throws IOException;
}
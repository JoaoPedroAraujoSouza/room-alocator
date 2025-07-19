package org.example.service.interfaces;

import org.example.exceptions.ConflictException;
import org.example.exceptions.NotFoundException;
import org.example.models.TimeAllocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TimeAllocationServiceInterface {
    List<TimeAllocation> getAllAllocations();
    Optional<TimeAllocation> getAllocationById(long id);
    void addAllocation(TimeAllocation allocation) throws ConflictException, IOException;
    void updateAllocation(TimeAllocation allocation) throws ConflictException, NotFoundException, IOException;
    void deleteAllocationById(long id) throws NotFoundException, IOException;
}

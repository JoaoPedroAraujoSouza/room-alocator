// TimeAllocationRepositoryInterface.java
package org.example.repository.interfaces;

import org.example.models.TimeAllocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TimeAllocationRepositoryInterface {
    List<TimeAllocation> findAll();
    Optional<TimeAllocation> findById(long id);
    void create(TimeAllocation allocation) throws IOException;
    void update(TimeAllocation allocation) throws IOException;
    void deleteById(long id) throws IOException;
}
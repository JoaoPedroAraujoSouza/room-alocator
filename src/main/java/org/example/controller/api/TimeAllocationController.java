package org.example.controller.api;

import java.util.List;
import java.util.Optional;

import org.example.models.TimeAllocation;
import org.example.service.TimeAllocationService;

public class TimeAllocationController extends AbstractController<TimeAllocation> {
    private final TimeAllocationService service = new TimeAllocationService();

    @Override
    public List<TimeAllocation> getAll() {
        return service.getAllAllocations();
    }

    @Override
    public Optional<TimeAllocation> getById(long id) {
        return service.getAllocationById(id);
    }

    @Override
    public boolean create(TimeAllocation timeAllocation) {
        try {
            service.addAllocation(timeAllocation);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating time allocation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TimeAllocation timeAllocation) {
        try {
            service.updateAllocation(timeAllocation);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating time allocation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteAllocationById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting time allocation: " + e.getMessage());
            return false;
        }
    }
}

package org.example.controller;

import org.example.models.TimeBlock;
import org.example.service.TimeBlockService;

import java.util.List;
import java.util.Optional;

public class TimeBlockController extends AbstractController<TimeBlock> {
    private final TimeBlockService service = new TimeBlockService();

    @Override
    public List<TimeBlock> getAll() {
        return service.getAllTimeBlocks();
    }

    @Override
    public Optional<TimeBlock> getById(long id) {
        return service.getTimeBlockById(id);
    }

    @Override
    public boolean create(TimeBlock timeBlock) {
        try {
            service.addTimeBlock(timeBlock);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating time block: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TimeBlock timeBlock) {
        try {
            service.updateTimeBlock(timeBlock);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating time block: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteTimeBlockById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting time block: " + e.getMessage());
            return false;
        }
    }
}

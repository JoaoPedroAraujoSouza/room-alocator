package org.example.service;

import org.example.exceptions.ConflictException;
import org.example.exceptions.NotFoundException;
import org.example.models.*;
import org.example.repository.TimeAllocationRepository;
import org.example.service.interfaces.TimeAllocationServiceInterface;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class TimeAllocationService extends AbstractService<TimeAllocation> implements TimeAllocationServiceInterface {
    private final TimeAllocationRepository repository;

    public TimeAllocationService(TimeAllocationRepository repository) {
        this.repository = repository;
    }

    public TimeAllocationService() {
        this.repository = new TimeAllocationRepository();
    }

    private void validateAllocationBusinessRules(TimeAllocation newAllocation) throws ConflictException {
        if (newAllocation.getRoom() == null || newAllocation.getClassroom() == null || newAllocation.getTimeBlock() == null) {
            throw new ConflictException("Room, Classroom, and TimeBlock must be specified for the allocation.");
        }

        if (newAllocation.getRoom().getCapacity() < newAllocation.getClassroom().getMaxStudentsCapacity()) {
            throw new ConflictException("Room capacity must be greater than or equal to Classroom max students capacity.");
        }

        List<TimeAllocation> allAllocations = repository.findAll();
        TimeBlock newTimeBlock = newAllocation.getTimeBlock();
        Room room = newAllocation.getRoom();

        if (room.getUnavailabilityPeriods() != null) {
            for (UnavailabityPeriod period : room.getUnavailabilityPeriods()) {
                LocalTime periodStart = period.getStartDate();
                LocalTime periodEnd = period.getEndDate();
                boolean timeOverlaps = newTimeBlock.getStartTime().isBefore(periodEnd) && newTimeBlock.getEndTime().isAfter(periodStart);
                if (timeOverlaps) {
                    throw new ConflictException("The room is unavailable during the specified time block.");
                }
            }
        }

        for (TimeAllocation existingAllocation : allAllocations) {
            if (existingAllocation.getId() == newAllocation.getId()) {
                continue;
            }

            TimeBlock existingTimeBlock = existingAllocation.getTimeBlock();
            if (existingTimeBlock == null) continue;

            boolean sameDay = existingTimeBlock.getDayOfWeek() == newTimeBlock.getDayOfWeek();
            if (!sameDay) continue;

            boolean timeOverlaps = newTimeBlock.getStartTime().isBefore(existingTimeBlock.getEndTime()) && newTimeBlock.getEndTime().isAfter(existingTimeBlock.getStartTime());

            if (timeOverlaps) {
                if (existingAllocation.getClassroom().getResponsibleTeacher().getId() == newAllocation.getClassroom().getResponsibleTeacher().getId()) {
                    throw new ConflictException("The teacher is already allocated during this time block.");
                }
                if (existingAllocation.getRoom().getId() == newAllocation.getRoom().getId()) {
                    throw new ConflictException("The room is already allocated during this time block.");
                }
            }
        }
    }

    @Override
    public List<TimeAllocation> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TimeAllocation> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(TimeAllocation allocation) throws ConflictException, IOException {
        validateAllocationBusinessRules(allocation);
        repository.create(allocation);
    }

    @Override
    public void update(TimeAllocation allocation) throws ConflictException, NotFoundException, IOException {
        repository.findById(allocation.getId())
                .orElseThrow(() -> new NotFoundException("Allocation with ID " + allocation.getId() + " not found."));
        validateAllocationBusinessRules(allocation);
        repository.update(allocation);
    }

    @Override
    public void deleteById(long id) throws NotFoundException, IOException {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Allocation with ID " + id + " not found."));
        repository.deleteById(id);
    }

    @Override
    public List<TimeAllocation> getAllAllocations() {
        return getAll();
    }

    @Override
    public Optional<TimeAllocation> getAllocationById(long id) {
        return getById(id);
    }

    @Override
    public void addAllocation(TimeAllocation allocation) throws ConflictException, IOException {
        add(allocation);
    }

    @Override
    public void updateAllocation(TimeAllocation allocation) throws ConflictException, NotFoundException, IOException {
        update(allocation);
    }

    @Override
    public void deleteAllocationById(long id) throws NotFoundException, IOException {
        deleteById(id);
    }
}
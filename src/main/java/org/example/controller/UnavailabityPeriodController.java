package org.example.controller.api;

import java.util.List;
import java.util.Optional;

import org.example.models.UnavailabityPeriod;
import org.example.service.UnavailabityPeriodService;

public class UnavailabityPeriodController extends AbstractController<UnavailabityPeriod> {
    private final UnavailabityPeriodService service = new UnavailabityPeriodService();

    @Override
    public List<UnavailabityPeriod> getAll() {
        return service.getAllPeriods();
    }

    @Override
    public Optional<UnavailabityPeriod> getById(long id) {
        return service.getPeriodById(id);
    }

    @Override
    public boolean create(UnavailabityPeriod unavailabilityPeriod) {
        try {
            service.addPeriod(unavailabilityPeriod);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating unavailability period: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(UnavailabityPeriod unavailabilityPeriod) {
        try {
            service.updatePeriod(unavailabilityPeriod);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating unavailability period: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deletePeriodById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting unavailability period: " + e.getMessage());
            return false;
        }
    }
}
package org.example.repository;

import org.example.models.UnavailabityPeriod;
import org.example.repository.interfaces.UnavailabityPeriodRepositoryInterface;

public class UnavailabityPeriodRepository extends AbstractRepository<UnavailabityPeriod> implements UnavailabityPeriodRepositoryInterface {
    public UnavailabityPeriodRepository() {
        super("unavailabity_periods.dat");
    }

    @Override
    protected long getId(UnavailabityPeriod period) {
        return period.getId();
    }
}
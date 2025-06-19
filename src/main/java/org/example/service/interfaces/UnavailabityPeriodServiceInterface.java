package org.example.service.interfaces;

import org.example.models.UnavailabityPeriod;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UnavailabityPeriodServiceInterface {
    List<UnavailabityPeriod> getAllPeriods();
    Optional<UnavailabityPeriod> getPeriodById(long id);
    void addPeriod(UnavailabityPeriod period) throws IOException;
    void updatePeriod(UnavailabityPeriod period) throws IOException;
    void deletePeriodById(long id) throws IOException;
}
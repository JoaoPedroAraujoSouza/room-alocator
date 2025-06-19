// UnavailabityPeriodRepositoryInterface.java
package org.example.repository.interfaces;

import org.example.models.UnavailabityPeriod;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UnavailabityPeriodRepositoryInterface {
    List<UnavailabityPeriod> findAll();
    Optional<UnavailabityPeriod> findById(long id);
    void create(UnavailabityPeriod period) throws IOException;
    void update(UnavailabityPeriod period) throws IOException;
    void deleteById(long id) throws IOException;
}
// TimeBlockRepositoryInterface.java
package org.example.repository.interfaces;

import org.example.models.TimeBlock;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TimeBlockRepositoryInterface {
    List<TimeBlock> findAll();
    Optional<TimeBlock> findById(long id);
    void create(TimeBlock timeBlock) throws IOException;
    void update(TimeBlock timeBlock) throws IOException;
    void deleteById(long id) throws IOException;
}
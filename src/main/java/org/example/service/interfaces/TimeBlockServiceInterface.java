package org.example.service.interfaces;

import org.example.models.TimeBlock;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TimeBlockServiceInterface {
    List<TimeBlock> getAllTimeBlocks();
    Optional<TimeBlock> getTimeBlockById(long id);
    void addTimeBlock(TimeBlock timeBlock) throws IOException;
    void updateTimeBlock(TimeBlock timeBlock) throws IOException;
    void deleteTimeBlockById(long id) throws IOException;
}
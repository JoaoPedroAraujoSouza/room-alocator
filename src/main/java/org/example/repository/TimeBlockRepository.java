package org.example.repository;

import org.example.models.TimeBlock;
import org.example.repository.interfaces.TimeBlockRepositoryInterface;

public class TimeBlockRepository extends AbstractRepository<TimeBlock> implements TimeBlockRepositoryInterface {
    public TimeBlockRepository() {
        super("time_blocks.dat");
    }

    @Override
    protected long getId(TimeBlock timeBlock) {
        return timeBlock.getId();
    }
}
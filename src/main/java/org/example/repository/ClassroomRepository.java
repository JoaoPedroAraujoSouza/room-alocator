package org.example.repository;

import org.example.models.Classroom;
import org.example.repository.interfaces.ClassroomRepositoryInterface;

public class ClassroomRepository extends AbstractRepository<Classroom> implements ClassroomRepositoryInterface {
    public ClassroomRepository() {
        super("classrooms.dat");
    }

    @Override
    protected long getId(Classroom classroom) {
        return classroom.getId();
    }
}
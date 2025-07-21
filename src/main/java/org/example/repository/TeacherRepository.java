package org.example.repository;

import org.example.models.Teacher;
import org.example.repository.interfaces.TeacherRepositoryInterface;

public class TeacherRepository extends AbstractRepository<Teacher> implements TeacherRepositoryInterface {
    public TeacherRepository() {
        super("teachers.dat");
    }

    @Override
    protected long getId(Teacher teacher) {
        return teacher.getId();
    }
}
package org.example.repository;

import org.example.models.Subject;
import org.example.repository.interfaces.SubjectRepositoryInterface;

public class SubjectRepository extends AbstractRepository<Subject> implements SubjectRepositoryInterface {
    public SubjectRepository() {
        super("subjects.dat");
    }

    @Override
    protected long getId(Subject subject) {
        return subject.getId();
    }
}
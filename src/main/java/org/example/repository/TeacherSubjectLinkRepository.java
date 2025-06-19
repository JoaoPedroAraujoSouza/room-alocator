package org.example.repository;

import org.example.models.TeacherSubjectLink;
import org.example.repository.interfaces.TeacherSubjectLinkRepositoryInterface;

public class TeacherSubjectLinkRepository extends AbstractRepository<TeacherSubjectLink> implements TeacherSubjectLinkRepositoryInterface {
    public TeacherSubjectLinkRepository() {
        super("teacher_subject_links.dat");
    }

    @Override
    protected long getId(TeacherSubjectLink link) {
        return link.getId();
    }
}
package org.example.service;

import org.example.models.TeacherSubjectLink;
import org.example.repository.TeacherSubjectLinkRepository;
import org.example.service.interfaces.TeacherSubjectLinkServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TeacherSubjectLinkService extends AbstractService<TeacherSubjectLink> implements TeacherSubjectLinkServiceInterface {
    private final TeacherSubjectLinkRepository repository = new TeacherSubjectLinkRepository();

    @Override
    public List<TeacherSubjectLink> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TeacherSubjectLink> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(TeacherSubjectLink link) throws IOException {
        repository.create(link);
    }

    @Override
    public void update(TeacherSubjectLink link) throws IOException {
        repository.update(link);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<TeacherSubjectLink> getAllLinks() {
        return getAll();
    }

    @Override
    public Optional<TeacherSubjectLink> getLinkById(long id) {
        return getById(id);
    }

    @Override
    public void addLink(TeacherSubjectLink link) throws IOException {
        add(link);
    }

    @Override
    public void updateLink(TeacherSubjectLink link) throws IOException {
        update(link);
    }

    @Override
    public void deleteLinkById(long id) throws IOException {
        deleteById(id);
    }
}
package org.example.controller.api;

import java.util.List;
import java.util.Optional;

import org.example.models.TeacherSubjectLink;
import org.example.service.TeacherSubjectLinkService;

public class TeacherSubjectLinkController extends AbstractController<TeacherSubjectLink> {
    private final TeacherSubjectLinkService service = new TeacherSubjectLinkService();

    @Override
    public List<TeacherSubjectLink> getAll() {
        return service.getAllLinks();
    }

    @Override
    public Optional<TeacherSubjectLink> getById(long id) {
        return service.getLinkById(id);
    }

    @Override
    public boolean create(TeacherSubjectLink link) {
        try {
            service.addLink(link);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating teacher-subject link: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TeacherSubjectLink link) {
        try {
            service.updateLink(link);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating teacher-subject link: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteLinkById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting teacher-subject link: " + e.getMessage());
            return false;
        }
    }
}

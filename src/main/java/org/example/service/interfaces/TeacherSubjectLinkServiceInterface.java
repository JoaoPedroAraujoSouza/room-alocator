package org.example.service.interfaces;

import org.example.models.TeacherSubjectLink;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TeacherSubjectLinkServiceInterface {
    List<TeacherSubjectLink> getAllLinks();
    Optional<TeacherSubjectLink> getLinkById(long id);
    void addLink(TeacherSubjectLink link) throws IOException;
    void updateLink(TeacherSubjectLink link) throws IOException;
    void deleteLinkById(long id) throws IOException;
}
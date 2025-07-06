package org.example.controller.ui.EditPopUp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.example.models.Teacher;
import org.example.models.Classroom;
import org.example.models.TeacherSubjectLink;
import org.example.service.TeacherService;
import org.example.service.ClassroomService;
import java.net.URL;
import java.util.ResourceBundle;

public class EditTeacherSubjectLinkDialogController implements Initializable {
    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private TextField semesterField;
    @FXML private CheckBox activeCheckBox;
    @FXML private ComboBox<Classroom> classroomComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private TeacherSubjectLink linkToEdit;
    private boolean updated = false;
    private final TeacherService teacherService = new TeacherService();
    private final ClassroomService classroomService = new ClassroomService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Preencher ComboBoxes com dados reais
        teacherComboBox.getItems().setAll(teacherService.getAllTeachers());
        classroomComboBox.getItems().setAll(classroomService.getAllClassrooms());
    }

    public void setLink(TeacherSubjectLink link) {
        this.linkToEdit = link;
        if (link != null) {
            teacherComboBox.setValue(link.getTeacher());
            semesterField.setText(link.getSemester());
            activeCheckBox.setSelected(link.isActive());
            classroomComboBox.setValue(link.getClassroom());
        }
    }

    @FXML
    private void handleSave() {
        if (linkToEdit != null) {
            Teacher teacher = teacherComboBox.getValue();
            String semester = semesterField.getText().trim();
            boolean active = activeCheckBox.isSelected();
            Classroom classroom = classroomComboBox.getValue();
            if (teacher == null || semester.isEmpty() || classroom == null) {
                return;
            }
            linkToEdit.setTeacher(teacher);
            linkToEdit.setSemester(semester);
            linkToEdit.setActive(active);
            linkToEdit.setClassroom(classroom);
            updated = true;
        }
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        updated = false;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public boolean isUpdated() {
        return updated;
    }
} 
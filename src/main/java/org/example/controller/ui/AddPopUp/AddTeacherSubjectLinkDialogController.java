package org.example.controller.ui.AddPopUp;

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

public class AddTeacherSubjectLinkDialogController implements Initializable {
    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private TextField semesterField;
    @FXML private CheckBox activeCheckBox;
    @FXML private ComboBox<Classroom> classroomComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private TeacherSubjectLink createdLink;
    private final TeacherService teacherService = new TeacherService();
    private final ClassroomService classroomService = new ClassroomService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Preencher ComboBoxes com dados reais
        teacherComboBox.getItems().setAll(teacherService.getAllTeachers());
        classroomComboBox.getItems().setAll(classroomService.getAllClassrooms());
        System.out.println("[LOG] Teacher and Classroom ComboBoxes populated with real data.");
    }

    @FXML
    private void handleSave() {
        Teacher teacher = teacherComboBox.getValue();
        String semester = semesterField.getText().trim();
        boolean active = activeCheckBox.isSelected();
        Classroom classroom = classroomComboBox.getValue();
        if (teacher == null || semester.isEmpty() || classroom == null) {
            System.out.println("[LOG] Validation failed: All fields must be filled.");
            return;
        }
        createdLink = new TeacherSubjectLink(
            org.example.models.TeacherSubjectLink.getNextId(),
            java.util.UUID.randomUUID(),
            teacher,
            null, // subject será setado pelo controller pai
            semester,
            active,
            classroom
        );
        System.out.println("[LOG] Created TeacherSubjectLink: " + createdLink);
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        System.out.println("[LOG] AddTeacherSubjectLinkDialog canceled by user.");
        createdLink = null;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public TeacherSubjectLink getCreatedLink() {
        return createdLink;
    }
} 
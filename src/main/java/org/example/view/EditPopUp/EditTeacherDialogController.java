package org.example.view.EditPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.example.exceptions.NotFoundException; // Import necessário
import org.example.exceptions.ValidationException; // Import necessário
import org.example.models.Teacher;
import org.example.models.TeacherSubjectLink;
import org.example.service.TeacherService;
import org.example.service.TeacherSubjectLinkService;
import org.example.view.DashBoardTeacherController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTeacherDialogController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField emailField;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<TeacherSubjectLink> tableSubjectLinks;

    @FXML
    private TableColumn<TeacherSubjectLink, String> columnSubjectName;

    @FXML
    private TableColumn<TeacherSubjectLink, String> columnSubjectCode;

    @FXML
    private TableColumn<TeacherSubjectLink, String> columnSubjectCredits;

    @FXML
    private TableColumn<TeacherSubjectLink, String> columnLinkDate;

    private TeacherService teacherService = new TeacherService();
    private TeacherSubjectLinkService teacherSubjectLinkService = new TeacherSubjectLinkService();
    private DashBoardTeacherController parentController;
    private Teacher currentTeacher;
    private ObservableList<TeacherSubjectLink> subjectLinkList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSubjectLinksTable();
    }

    private void setupSubjectLinksTable() {
        columnSubjectName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject() != null ? cellData.getValue().getSubject().getName() : "Not assigned"));
        columnSubjectCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject() != null ? String.valueOf(cellData.getValue().getSubject().getId()) : "Not assigned"));
        columnSubjectCredits.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject() != null ? String.valueOf(cellData.getValue().getSubject().getHourlyLoad()) : "Not assigned"));
        columnLinkDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester() != null ? cellData.getValue().getSemester() : "Not assigned"));
        tableSubjectLinks.setItems(subjectLinkList);
    }

    public void setParentController(DashBoardTeacherController parentController) {
        this.parentController = parentController;
    }

    public void setTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        populateFields();
        loadSubjectLinksForTeacher();
    }

    private void populateFields() {
        if (currentTeacher != null) {
            nameField.setText(currentTeacher.getName());
            cpfField.setText(currentTeacher.getCpf());
            emailField.setText(currentTeacher.getEmail());
        }
    }

    private void loadSubjectLinksForTeacher() {
        if (currentTeacher == null) {
            subjectLinkList.clear();
            return;
        }

        List<TeacherSubjectLink> allLinks = teacherSubjectLinkService.getAll();
        subjectLinkList.setAll(
                allLinks.stream()
                        .filter(link -> link.getTeacher() != null && link.getTeacher().getId() == currentTeacher.getId())
                        .toList()
        );
    }

    @FXML
    private void handleUpdate() {
        if (validateInputs()) {
            try {
                currentTeacher.setName(nameField.getText().trim());
                currentTeacher.setCpf(cpfField.getText().trim());
                currentTeacher.setEmail(emailField.getText().trim());

                teacherService.update(currentTeacher);

                if (parentController != null) {
                    parentController.updateTeacherInTable(currentTeacher);
                }

                showAlert("Success", "Teacher updated successfully!", Alert.AlertType.INFORMATION);
                closeDialog();

            } catch (ValidationException e) {
                showAlert("Validation Error", "Could not update teacher: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (NotFoundException e) {
                showAlert("Not Found Error", "Could not update teacher: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (IOException e) {
                showAlert("File Error", "Failed to update teacher: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.", Alert.AlertType.ERROR);
            return false;
        }
        if (cpfField.getText().trim().isEmpty() || !isValidCPF(cpfField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid CPF format (e.g., 123.456.789-00).", Alert.AlertType.ERROR);
            return false;
        }
        if (emailField.getText().trim().isEmpty() || !isValidEmail(emailField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid email address.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean isValidCPF(String cpf) {
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}
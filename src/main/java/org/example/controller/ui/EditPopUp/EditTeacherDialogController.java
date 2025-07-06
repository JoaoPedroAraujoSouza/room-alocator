package org.example.controller.ui.EditPopUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import org.example.models.Teacher;
import org.example.models.TeacherSubjectLink;
import org.example.service.TeacherService;
import org.example.service.TeacherSubjectLinkService;
import org.example.controller.ui.DashBoardTeacherController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    
    // TeacherSubjectLink Table
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
        // Configure subject links table columns
        columnSubjectName.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                cellData.getValue().getSubject() != null ? cellData.getValue().getSubject().getName() : "Not assigned"
            );
        });
        columnSubjectCode.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                cellData.getValue().getSubject() != null ? 
                String.valueOf(cellData.getValue().getSubject().getId()) : "Not assigned"
            );
        });
        columnSubjectCredits.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                cellData.getValue().getSubject() != null ? 
                String.valueOf(cellData.getValue().getSubject().getHourlyLoad()) : "Not assigned"
            );
        });
        columnLinkDate.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                cellData.getValue().getSemester() != null ? 
                cellData.getValue().getSemester() : "Not assigned"
            );
        });
        
        // Set table data
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
        
        // Filter subject links by teacher
        List<TeacherSubjectLink> allLinks = teacherSubjectLinkService.getAll();
        List<TeacherSubjectLink> teacherLinks = allLinks.stream()
            .filter(link -> link.getTeacher() != null && 
                    link.getTeacher().getId() == currentTeacher.getId())
            .toList();
        
        subjectLinkList.clear();
        subjectLinkList.addAll(teacherLinks);
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
                
            } catch (IOException e) {
                showAlert("Error", "Failed to update teacher: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleCancel() {
        closeDialog();
    }
    
    private boolean validateInputs() {
        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.", Alert.AlertType.ERROR);
            nameField.requestFocus();
            return false;
        }
        
        // Validate CPF
        String cpf = cpfField.getText().trim();
        if (cpf.isEmpty()) {
            showAlert("Validation Error", "CPF is required.", Alert.AlertType.ERROR);
            cpfField.requestFocus();
            return false;
        }
        
        // CPF format validation (Brazilian CPF format)
        if (!isValidCPF(cpf)) {
            showAlert("Validation Error", "Please enter a valid CPF format (e.g., 123.456.789-00).", Alert.AlertType.ERROR);
            cpfField.requestFocus();
            return false;
        }
        
        // Validate email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showAlert("Validation Error", "Email is required.", Alert.AlertType.ERROR);
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(email)) {
            showAlert("Validation Error", "Please enter a valid email address.", Alert.AlertType.ERROR);
            emailField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidCPF(String cpf) {
        // Remove non-numeric characters
        String cleanCPF = cpf.replaceAll("[^0-9]", "");
        
        // Check if it has 11 digits
        if (cleanCPF.length() != 11) {
            return false;
        }
        
        // Check if all digits are the same (invalid CPF)
        if (cleanCPF.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Basic format validation for display
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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
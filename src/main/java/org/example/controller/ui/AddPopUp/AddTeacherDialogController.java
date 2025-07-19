package org.example.controller.ui.AddPopUp;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import org.example.controller.ui.DashBoardTeacherController;
import org.example.exceptions.ValidationException;
import org.example.models.Teacher;
import org.example.service.TeacherService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTeacherDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField emailField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private TeacherService teacherService = new TeacherService();
    private DashBoardTeacherController parentController;

    public void setParentController(DashBoardTeacherController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleSave() {
        if (validateInputs()) {
            try {
                Teacher teacher = new Teacher(
                        Teacher.getNextId(),
                        UUID.randomUUID(),
                        nameField.getText().trim(),
                        cpfField.getText().trim(),
                        emailField.getText().trim()
                );

                // Esta chamada pode lançar ValidationException ou IOException
                teacherService.add(teacher);

                if (parentController != null) {
                    parentController.addTeacherToTable(teacher);
                }

                Alert alert = new Alert(AlertType.INFORMATION, "Teacher added successfully!", ButtonType.OK);
                alert.setHeaderText(null);
                alert.setTitle("Success");
                alert.showAndWait();
                closeDialog();

            } catch (ValidationException e) {
                showAlert("Validation Error", "A validation error occurred in the service: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (IOException e) {
                showAlert("Error", "Failed to save teacher: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInputs() {
        // Validações da UI...
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.", Alert.AlertType.ERROR);
            nameField.requestFocus();
            return false;
        }

        String cpf = cpfField.getText().trim();
        if (cpf.isEmpty()) {
            showAlert("Validation Error", "CPF is required.", Alert.AlertType.ERROR);
            cpfField.requestFocus();
            return false;
        }

        if (!isValidCPF(cpf)) {
            showAlert("Validation Error", "Please enter a valid CPF format (e.g., 123.456.789-00).", Alert.AlertType.ERROR);
            cpfField.requestFocus();
            return false;
        }

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
        String cleanCPF = cpf.replaceAll("[^0-9]", "");
        if (cleanCPF.length() != 11) {
            return false;
        }
        if (cleanCPF.matches("(\\d)\\1{10}")) {
            return false;
        }
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
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
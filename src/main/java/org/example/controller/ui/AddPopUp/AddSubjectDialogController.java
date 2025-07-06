package org.example.controller.ui.AddPopUp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.Subject;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class AddSubjectDialogController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    private Spinner<Integer> creditsSpinner;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Subject createdSubject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura spinner de créditos
        creditsSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 4));
        // Validação inicial
        validateForm();
        // Listeners para validação
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        codeField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        creditsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        descriptionField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    private void validateForm() {
        boolean valid = !nameField.getText().trim().isEmpty()
                && !codeField.getText().trim().isEmpty()
                && creditsSpinner.getValue() != null
                && creditsSpinner.getValue() > 0;
        saveButton.setDisable(!valid);
    }

    @FXML
    private void handleSave() {
        createdSubject = new Subject(
                Subject.getNextId(),
                UUID.randomUUID(),
                nameField.getText().trim(),
                descriptionField.getText().trim(),
                creditsSpinner.getValue()
        );
        Alert alert = new Alert(AlertType.INFORMATION, "Subject added successfully!", ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Success");
        alert.showAndWait();
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        createdSubject = null;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public Subject getCreatedSubject() {
        return createdSubject;
    }
} 
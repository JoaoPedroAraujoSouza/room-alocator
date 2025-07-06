package org.example.controller.ui.EditPopUp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.Subject;

import java.net.URL;
import java.util.ResourceBundle;

public class EditSubjectDialogController implements Initializable {
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

    private Subject subjectToEdit;
    private boolean updated = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditsSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 4));
        validateForm();
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        codeField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        creditsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());
        descriptionField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }

    public void setSubject(Subject subject) {
        this.subjectToEdit = subject;
        if (subject != null) {
            nameField.setText(subject.getName());
            codeField.setText(String.valueOf(subject.getId()));
            creditsSpinner.getValueFactory().setValue(subject.getHourlyLoad());
            descriptionField.setText(subject.getDescription());
        }
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
        if (subjectToEdit != null) {
            subjectToEdit.setName(nameField.getText().trim());
            // O campo codeField é apenas visual, não altera o id real
            subjectToEdit.setHourlyLoad(creditsSpinner.getValue());
            subjectToEdit.setDescription(descriptionField.getText().trim());
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
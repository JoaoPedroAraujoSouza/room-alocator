package org.example.controller.ui.AddPopUp;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.example.controller.ui.DashBoardAllocationsController;
import org.example.exceptions.ConflictException;
import org.example.models.Classroom;
import org.example.models.Room;
import org.example.models.TimeAllocation;
import org.example.models.TimeBlock;
import org.example.service.ClassroomService;
import org.example.service.RoomService;
import org.example.service.TimeAllocationService;
import org.example.service.TimeBlockService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class AddAllocationDialogController implements Initializable {

    @FXML private ComboBox<Classroom> classroomComboBox;
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private ComboBox<DayOfWeek> dayComboBox;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final ClassroomService classroomService = new ClassroomService();
    private final RoomService roomService = new RoomService();
    private final TimeBlockService timeBlockService = new TimeBlockService();
    private final TimeAllocationService timeAllocationService = new TimeAllocationService();

    private DashBoardAllocationsController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSpinners();
        setupComboBoxes();
        setupValidation();
    }

    private void setupSpinners() {
        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        startMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));
        startHourSpinner.setEditable(true);
        startMinuteSpinner.setEditable(true);
        endHourSpinner.setEditable(true);
        endMinuteSpinner.setEditable(true);
    }

    private void setupComboBoxes() {
        try {
            classroomComboBox.setItems(FXCollections.observableArrayList(classroomService.getAll()));
            classroomComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Classroom item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : "Semester: " + item.getSemester() + " (Shift: " + item.getShift() + ")");
                }
            });
            classroomComboBox.setButtonCell(classroomComboBox.getCellFactory().call(null));

            roomComboBox.setItems(FXCollections.observableArrayList(roomService.getAll()));
            roomComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Room item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName() + " (Capacity: " + item.getCapacity() + ")");
                }
            });
            roomComboBox.setButtonCell(roomComboBox.getCellFactory().call(null));

            dayComboBox.setItems(FXCollections.observableArrayList(DayOfWeek.values()));
        } catch (Exception e) {
            showAlert("Error", "Failed to load data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupValidation() {
        startHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateTime());
        startMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateTime());
        endHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateTime());
        endMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> validateTime());
    }

    private void validateTime() {
        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
        saveButton.setDisable(endTime.isBefore(startTime) || endTime.equals(startTime));
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            TimeBlock timeBlock = new TimeBlock(
                    TimeBlock.getNextId(),
                    dayComboBox.getValue(),
                    LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()),
                    LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue())
            );

            TimeAllocation allocation = new TimeAllocation(
                    TimeAllocation.getNextId(),
                    UUID.randomUUID(),
                    classroomComboBox.getValue(),
                    roomComboBox.getValue(),
                    timeBlock
            );

            timeAllocationService.add(allocation);

            if (parentController != null) {
                parentController.addAllocation(allocation);
            }

            showAlert("Success", "Time allocation created successfully!", Alert.AlertType.INFORMATION);
            closeDialog();

        } catch (ConflictException e) {
            showAlert("Conflict Error", "Could not create allocation: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlert("File Error", "Failed to create allocation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInput() {
        if (classroomComboBox.getValue() == null || roomComboBox.getValue() == null || dayComboBox.getValue() == null) {
            showAlert("Validation Error", "Please fill all fields.", Alert.AlertType.WARNING);
            return false;
        }
        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            showAlert("Validation Error", "End time must be after start time.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(DashBoardAllocationsController parentController) {
        this.parentController = parentController;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
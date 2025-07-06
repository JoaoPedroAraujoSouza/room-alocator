package org.example.controller.ui.AddPopUp;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.example.controller.ui.DashBoardAllocationsController;
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
        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8);
        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        startHourSpinner.setValueFactory(startHourFactory);
        endHourSpinner.setValueFactory(endHourFactory);

        SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        startMinuteSpinner.setValueFactory(startMinuteFactory);
        endMinuteSpinner.setValueFactory(endMinuteFactory);

        startHourSpinner.setEditable(true);
        startMinuteSpinner.setEditable(true);
        endHourSpinner.setEditable(true);
        endMinuteSpinner.setEditable(true);
    }

    private void setupComboBoxes() {
        try {
            List<Classroom> classrooms = classroomService.getAll();
            ObservableList<Classroom> classroomList = FXCollections.observableArrayList(classrooms);
            classroomComboBox.setItems(classroomList);
            
            classroomComboBox.setCellFactory(param -> new ListCell<Classroom>() {
                @Override
                protected void updateItem(Classroom item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText("Semester: " + item.getSemester() + " (Shift: " + item.getShift() + ")");
                    }
                }
            });
            
            classroomComboBox.setButtonCell(classroomComboBox.getCellFactory().call(null));
        } catch (Exception e) {
            showAlert("Error", "Failed to load classrooms: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        try {
            List<Room> rooms = roomService.getAll();
            ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);
            roomComboBox.setItems(roomList);
            
            roomComboBox.setCellFactory(param -> new ListCell<Room>() {
                @Override
                protected void updateItem(Room item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (Capacity: " + item.getCapacity() + ")");
                    }
                }
            });
            
            roomComboBox.setButtonCell(roomComboBox.getCellFactory().call(null));
        } catch (Exception e) {
            showAlert("Error", "Failed to load rooms: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        ObservableList<DayOfWeek> dayList = FXCollections.observableArrayList(DayOfWeek.values());
        dayComboBox.setItems(dayList);

        dayComboBox.setCellFactory(param -> new ListCell<DayOfWeek>() {
            @Override
            protected void updateItem(DayOfWeek item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        dayComboBox.setButtonCell(dayComboBox.getCellFactory().call(null));
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
        
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            saveButton.setDisable(true);
        } else {
            saveButton.setDisable(false);
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
            
            TimeBlock timeBlock = new TimeBlock(
                TimeBlock.getNextId(),
                dayComboBox.getValue(),
                startTime,
                endTime
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

        } catch (IOException e) {
            showAlert("Error", "Failed to create allocation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInput() {
        if (classroomComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a classroom.", Alert.AlertType.WARNING);
            return false;
        }

        if (roomComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a room.", Alert.AlertType.WARNING);
            return false;
        }

        if (dayComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a day of week.", Alert.AlertType.WARNING);
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
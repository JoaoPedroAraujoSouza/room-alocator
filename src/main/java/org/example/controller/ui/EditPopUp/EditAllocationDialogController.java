package org.example.controller.ui.EditPopUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.controller.ui.DashBoardAllocationsController;
import org.example.models.TimeAllocation;
import org.example.models.Classroom;
import org.example.models.Room;
import org.example.models.TimeBlock;
import org.example.service.ClassroomService;
import org.example.service.RoomService;
import org.example.service.TimeBlockService;
import org.example.service.TimeAllocationService;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class EditAllocationDialogController implements Initializable {

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
    private TimeAllocation currentAllocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSpinners();
        setupComboBoxes();
        setupValidation();
    }

    private void setupSpinners() {
        // Setup hour spinners (0-23)
        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8);
        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        startHourSpinner.setValueFactory(startHourFactory);
        endHourSpinner.setValueFactory(endHourFactory);

        // Setup minute spinners (0-59, step by 15)
        SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        startMinuteSpinner.setValueFactory(startMinuteFactory);
        endMinuteSpinner.setValueFactory(endMinuteFactory);

        // Make spinners editable
        startHourSpinner.setEditable(true);
        startMinuteSpinner.setEditable(true);
        endHourSpinner.setEditable(true);
        endMinuteSpinner.setEditable(true);
    }

    private void setupComboBoxes() {
        // Setup classroom combo box
        try {
            List<Classroom> classrooms = classroomService.getAll();
            ObservableList<Classroom> classroomList = FXCollections.observableArrayList(classrooms);
            classroomComboBox.setItems(classroomList);
            
            // Set cell factory to display classroom semester
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
            
            // Set button cell factory
            classroomComboBox.setButtonCell(classroomComboBox.getCellFactory().call(null));
        } catch (Exception e) {
            showAlert("Error", "Failed to load classrooms: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        // Setup room combo box
        try {
            List<Room> rooms = roomService.getAll();
            ObservableList<Room> roomList = FXCollections.observableArrayList(rooms);
            roomComboBox.setItems(roomList);
            
            // Set cell factory to display room name
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
            
            // Set button cell factory
            roomComboBox.setButtonCell(roomComboBox.getCellFactory().call(null));
        } catch (Exception e) {
            showAlert("Error", "Failed to load rooms: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        // Setup day combo box
        ObservableList<DayOfWeek> dayList = FXCollections.observableArrayList(DayOfWeek.values());
        dayComboBox.setItems(dayList);
        
        // Set cell factory to display day name
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
        
        // Set button cell factory
        dayComboBox.setButtonCell(dayComboBox.getCellFactory().call(null));
    }

    private void setupValidation() {
        // Add validation listeners
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

    public void setAllocation(TimeAllocation allocation) {
        this.currentAllocation = allocation;
        populateFields();
    }

    private void populateFields() {
        if (currentAllocation == null) return;

        // Set classroom
        if (currentAllocation.getClassroom() != null) {
            classroomComboBox.setValue(currentAllocation.getClassroom());
        }

        // Set room
        if (currentAllocation.getRoom() != null) {
            roomComboBox.setValue(currentAllocation.getRoom());
        }

        // Set time block
        if (currentAllocation.getTimeBlock() != null) {
            TimeBlock timeBlock = currentAllocation.getTimeBlock();
            
            // Set day
            if (timeBlock.getDayOfWeek() != null) {
                dayComboBox.setValue(timeBlock.getDayOfWeek());
            }

            // Set start time
            if (timeBlock.getStartTime() != null) {
                startHourSpinner.getValueFactory().setValue(timeBlock.getStartTime().getHour());
                startMinuteSpinner.getValueFactory().setValue(timeBlock.getStartTime().getMinute());
            }

            // Set end time
            if (timeBlock.getEndTime() != null) {
                endHourSpinner.getValueFactory().setValue(timeBlock.getEndTime().getHour());
                endMinuteSpinner.getValueFactory().setValue(timeBlock.getEndTime().getMinute());
            }
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            // Create updated TimeBlock
            LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
            
            TimeBlock updatedTimeBlock = new TimeBlock(
                currentAllocation.getTimeBlock().getId(),
                dayComboBox.getValue(),
                startTime,
                endTime
            );

            // Create updated TimeAllocation
            TimeAllocation updatedAllocation = new TimeAllocation(
                currentAllocation.getId(),
                currentAllocation.getUuid(),
                classroomComboBox.getValue(),
                roomComboBox.getValue(),
                updatedTimeBlock
            );

            // Update in service
            timeAllocationService.update(updatedAllocation);
            
            // Update in parent controller
            if (parentController != null) {
                parentController.updateAllocation(currentAllocation, updatedAllocation);
            }

            showAlert("Success", "Time allocation updated successfully!", Alert.AlertType.INFORMATION);
            closeDialog();

        } catch (IOException e) {
            showAlert("Error", "Failed to update allocation: " + e.getMessage(), Alert.AlertType.ERROR);
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
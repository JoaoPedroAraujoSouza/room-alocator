package org.example.controller.ui.AddPopUp;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.controller.ui.DashBoardRoomsController;
import org.example.models.Room;
import org.example.models.UnavailabityPeriod;
import org.example.service.RoomService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddRoomDialogController {

    @FXML
    private TextField txtRoomName;

    @FXML
    private TextField txtLocalization;

    @FXML
    private TextField txtCapacity;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtResource;

    @FXML
    private Button btnAddResource;

    @FXML
    private TextArea txtResourcesDisplay;

    @FXML
    private Button btnClearResources;

    @FXML
    private TextField txtStartDay;

    @FXML
    private TextField txtEndDay;

    @FXML
    private TextField txtReason;

    @FXML
    private Button btnAddPeriod;

    @FXML
    private TextArea txtPeriodsDisplay;

    @FXML
    private Button btnClearPeriods;

    private RoomService roomService;
    private DashBoardRoomsController parentController;
    private ObservableList<String> resourcesList;
    private ObservableList<String> periodsList;
    private List<UnavailabityPeriod> unavailabilityPeriods;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        roomService = new RoomService();

        resourcesList = FXCollections.observableArrayList();
        periodsList = FXCollections.observableArrayList();
        unavailabilityPeriods = new ArrayList<>();
        
        updateResourcesDisplay();
        updatePeriodsDisplay();
    }

    public void setParentController(DashBoardRoomsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleSave() {
        if (!validateFields()) {
            return;
        }

        try {
            Room newRoom = new Room(
                Room.getNextId(), 
                UUID.randomUUID(), 
                txtRoomName.getText().trim(), 
                txtLocalization.getText().trim(), 
                Integer.parseInt(txtCapacity.getText().trim()), 
                new ArrayList<>(resourcesList),
                new ArrayList<>(unavailabilityPeriods), 
                new ArrayList<>()  
            );

          
            roomService.add(newRoom);

  
            if (parentController != null) {
                parentController.addRoomToTable(newRoom);
            }


            Alert alert = new Alert(AlertType.INFORMATION, "Room added successfully!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Success");
            alert.showAndWait();


            closeDialog();

        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to save room: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Capacity must be a valid number.");
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }


    @FXML
    private void handleAddResource() {
        String resource = txtResource.getText().trim();
        if (resource.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Please enter a resource name.");
            txtResource.requestFocus();
            return;
        }
        
        if (resourcesList.contains(resource)) {
            showAlert(AlertType.WARNING, "Validation Error", "This resource is already added.");
            return;
        }
        
        resourcesList.add(resource);
        txtResource.clear();
        txtResource.requestFocus();
        updateResourcesDisplay();
    }

    @FXML
    private void handleClearResources() {
        resourcesList.clear();
        updateResourcesDisplay();
    }

    @FXML
    private void handleAddPeriod() {
        String startDayStr = txtStartDay.getText().trim();
        String endDayStr = txtEndDay.getText().trim();
        String reason = txtReason.getText().trim();

        if (startDayStr.isEmpty() || endDayStr.isEmpty() || reason.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Please fill all fields for the period.");
            return;
        }

        try {
            String[] startParts = startDayStr.split("/");
            String[] endParts = endDayStr.split("/");
            
            if (startParts.length != 2 || endParts.length != 2) {
                throw new IllegalArgumentException("Invalid format");
            }
            
            int startDay = Integer.parseInt(startParts[0]);
            int startMonth = Integer.parseInt(startParts[1]);
            int endDay = Integer.parseInt(endParts[0]);
            int endMonth = Integer.parseInt(endParts[1]);
            
            if (startDay < 1 || startDay > 31 || endDay < 1 || endDay > 31 ||
                startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
                showAlert(AlertType.WARNING, "Validation Error", "Invalid day or month values.");
                return;
            }

            if (startMonth > endMonth || (startMonth == endMonth && startDay > endDay)) {
                showAlert(AlertType.WARNING, "Validation Error", "Start date must be before end date.");
                return;
            }

            int startHour = (startDay - 1) % 24;
            int startMinute = (startMonth - 1) % 60;
            int endHour = (endDay - 1) % 24;
            int endMinute = (endMonth - 1) % 60;
            
            LocalTime startTime = LocalTime.of(startHour, startMinute);
            LocalTime endTime = LocalTime.of(endHour, endMinute);
            
            UnavailabityPeriod period = new UnavailabityPeriod(
                UnavailabityPeriod.getNextId(),
                UUID.randomUUID(),
                startTime,
                endTime,
                reason
            );

            unavailabilityPeriods.add(period);
            
            String periodDisplay = String.format("%s - %s: %s", startDayStr, endDayStr, reason);
            periodsList.add(periodDisplay);

            txtStartDay.clear();
            txtEndDay.clear();
            txtReason.clear();
            txtStartDay.requestFocus();
            
            updatePeriodsDisplay();

        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Validation Error", "Please enter date in dd/mm format (e.g., 15/03 for March 15th).");
        }
    }

    @FXML
    private void handleClearPeriods() {
        periodsList.clear();
        unavailabilityPeriods.clear();
        updatePeriodsDisplay();
    }
    
    private void updateResourcesDisplay() {
        if (resourcesList.isEmpty()) {
            txtResourcesDisplay.setText("");
        } else {
            txtResourcesDisplay.setText(String.join("\n", resourcesList));
        }
    }
    
    private void updatePeriodsDisplay() {
        if (periodsList.isEmpty()) {
            txtPeriodsDisplay.setText("");
        } else {
            txtPeriodsDisplay.setText(String.join("\n", periodsList));
        }
    }

    private boolean validateFields() {
        String roomName = txtRoomName.getText().trim();
        String localization = txtLocalization.getText().trim();
        String capacity = txtCapacity.getText().trim();

        if (roomName.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Room name is required.");
            txtRoomName.requestFocus();
            return false;
        }

        if (localization.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Localization is required.");
            txtLocalization.requestFocus();
            return false;
        }

        if (capacity.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Capacity is required.");
            txtCapacity.requestFocus();
            return false;
        }

        try {
            int cap = Integer.parseInt(capacity);
            if (cap <= 0) {
                showAlert(AlertType.WARNING, "Validation Error", "Capacity must be greater than 0.");
                txtCapacity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Validation Error", "Capacity must be a valid number.");
            txtCapacity.requestFocus();
            return false;
        }

        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
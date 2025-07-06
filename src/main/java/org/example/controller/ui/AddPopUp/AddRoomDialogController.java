package org.example.controller.ui.AddPopUp;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.models.Room;
import org.example.models.UnavailabityPeriod;
import org.example.service.RoomService;
import org.example.controller.ui.DashBoardRoomsController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
        
        // Initialize lists
        resourcesList = FXCollections.observableArrayList();
        periodsList = FXCollections.observableArrayList();
        unavailabilityPeriods = new ArrayList<>();
        
        // Set up display areas
        updateResourcesDisplay();
        updatePeriodsDisplay();
    }

    public void setParentController(DashBoardRoomsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleSave() {
        // Validar campos
        if (!validateFields()) {
            return;
        }

        try {
            // Criar nova sala
            Room newRoom = new Room(
                Room.getNextId(), // id
                UUID.randomUUID(), // uuid
                txtRoomName.getText().trim(), // name
                txtLocalization.getText().trim(), // localization
                Integer.parseInt(txtCapacity.getText().trim()), // capacity
                new ArrayList<>(resourcesList), // resources
                new ArrayList<>(unavailabilityPeriods), // unavailabilityPeriods
                new ArrayList<>()  // timeAllocations
            );

            // Salvar no service/repository
            roomService.add(newRoom);

            // Adicionar à tabela do controller pai
            if (parentController != null) {
                parentController.addRoomToTable(newRoom);
            }

            // Mostrar mensagem de sucesso
            Alert alert = new Alert(AlertType.INFORMATION, "Room added successfully!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Success");
            alert.showAndWait();

            // Fechar o pop-up
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

    // Resource management methods
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

    // Unavailability period management methods
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
            // Parse day format (dd/mm)
            String[] startParts = startDayStr.split("/");
            String[] endParts = endDayStr.split("/");
            
            if (startParts.length != 2 || endParts.length != 2) {
                throw new IllegalArgumentException("Invalid format");
            }
            
            int startDay = Integer.parseInt(startParts[0]);
            int startMonth = Integer.parseInt(startParts[1]);
            int endDay = Integer.parseInt(endParts[0]);
            int endMonth = Integer.parseInt(endParts[1]);
            
            // Basic validation for days and months
            if (startDay < 1 || startDay > 31 || endDay < 1 || endDay > 31 ||
                startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
                showAlert(AlertType.WARNING, "Validation Error", "Invalid day or month values.");
                return;
            }
            
            // Check if start date is before end date
            if (startMonth > endMonth || (startMonth == endMonth && startDay > endDay)) {
                showAlert(AlertType.WARNING, "Validation Error", "Start date must be before end date.");
                return;
            }

            // Create unavailability period (using LocalTime for compatibility with existing model)
            // We'll encode day:month as hour:minute
            // Day 1-31 -> Hour 0-23 (using modulo 24)
            // Month 1-12 -> Minute 0-59 (using modulo 60)
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
            
            // Add to display list
            String periodDisplay = String.format("%s - %s: %s", startDayStr, endDayStr, reason);
            periodsList.add(periodDisplay);

            // Clear fields
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
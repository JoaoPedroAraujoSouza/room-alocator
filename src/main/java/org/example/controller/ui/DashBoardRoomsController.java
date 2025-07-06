package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddRoomDialogController;
import org.example.controller.ui.EditPopUp.EditRoomDialogController;
import org.example.models.Room;
import org.example.models.TimeAllocation;
import org.example.models.UnavailabityPeriod;
import org.example.service.RoomService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashBoardRoomsController extends BaseDashboardController implements Initializable {

    @FXML
    private TableView<Room> tableRoom;

    @FXML
    private TableColumn <Room, String> columnName;

    @FXML
    private TableColumn<Room, String> columnLocalization;

    @FXML TableColumn<Room, Integer> columnCapacity;

    @FXML
    private TableColumn<Room, String> columnResources;

    @FXML
    private TableColumn<Room, String> columnUnavailability;

    @FXML
    private TableColumn<Room, String> columnAllocations;

    @FXML
    private TableColumn<Room, Void> columnActions;

    @FXML
    private TextField txtSearch;



    private ObservableList<Room> listRooms;
    private ObservableList<Room> filteredRooms;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnLocalization.setCellValueFactory(new PropertyValueFactory<>("localization"));
        columnCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        columnResources.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            String resourcesText = formatResources(room.getResources());
            return new SimpleStringProperty(resourcesText);
        });
        
        columnUnavailability.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            String unavailabilityText = formatUnavailabilityPeriods(room.getUnavailabilityPeriods());
            return new SimpleStringProperty(unavailabilityText);
        });
        
        columnAllocations.setCellValueFactory(cellData -> {
            Room room = cellData.getValue();
            String allocationsText = formatAllocations(room.getTimeAllocations());
            return new SimpleStringProperty(allocationsText);
        });

        columnActions.setCellFactory(col -> new TableCell<Room, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(e -> {
                    Room room = getTableView().getItems().get(getIndex());
                    editRoom(room);
                });

                deleteButton.setOnAction(e -> {
                    Room room = getTableView().getItems().get(getIndex());
                    deleteRoom(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        listRooms = FXCollections.observableArrayList();
        filteredRooms = FXCollections.observableArrayList();

        tableRoom.setItems(filteredRooms);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterRooms();
        });
        
        loadExistingRooms();
    }
    
    private void loadExistingRooms() {
        RoomService roomService = new RoomService();
        try {
            List<Room> existingRooms = roomService.getAll();
            listRooms.addAll(existingRooms);
            filterRooms(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateRoom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddRoomDialog.fxml"));
            Scene scene = new Scene(loader.load());
            
            AddRoomDialogController dialogController = loader.getController();
            dialogController.setParentController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Room");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setMinWidth(520);
            dialogStage.setMinHeight(650);
    
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addRoomToTable(Room room) {
        listRooms.add(room);
        filterRooms(); 
    }
    
    public void updateRoomInTable(Room room) {
        for (int i = 0; i < listRooms.size(); i++) {
            if (listRooms.get(i).getId() == room.getId()) {
                listRooms.set(i, room);
                break;
            }
        }
        filterRooms();
    }
    
    private void editRoom(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditRoomDialog.fxml"));
            Scene scene = new Scene(loader.load());

            EditRoomDialogController dialogController = loader.getController();
            dialogController.setParentController(this);
            dialogController.setRoomForEditing(room);
            

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Room");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setMinWidth(520);
            dialogStage.setMinHeight(650);

            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteRoom(Room room) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Room");
        alert.setContentText("Are you sure you want to delete the room '" + room.getName() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    RoomService roomService = new RoomService();
                    roomService.deleteById(room.getId());

                    listRooms.remove(room);
                    filterRooms(); 
                    showAlert(AlertType.INFORMATION, "Success", "Room deleted successfully!");
                    
                } catch (IOException e) {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete room: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String formatResources(List<String> resources) {
        if (resources == null || resources.isEmpty()) {
            return "None";
        }
        return String.join(", ", resources);
    }
    
    private String formatUnavailabilityPeriods(List<UnavailabityPeriod> periods) {
        if (periods == null || periods.isEmpty()) {
            return "None";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < periods.size(); i++) {
            UnavailabityPeriod period = periods.get(i);

                int startDay = period.getStartDate().getHour() + 1;
                int startMonth = period.getStartDate().getMinute() + 1;
                int endDay = period.getEndDate().getHour() + 1;
                int endMonth = period.getEndDate().getMinute() + 1;
                
                String startDate = String.format("%02d/%02d", startDay, startMonth);
                String endDate = String.format("%02d/%02d", endDay, endMonth);
            
            sb.append(startDate).append("-").append(endDate);
            if (i < periods.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    private String formatAllocations(List<TimeAllocation> allocations) {
        if (allocations == null || allocations.isEmpty()) {
            return "None";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allocations.size(); i++) {
            TimeAllocation allocation = allocations.get(i);
            
            String classroomInfo = allocation.getClassroom() != null ? 
                "Semester: " + allocation.getClassroom().getSemester() : "Unknown";
            String timeBlockInfo = allocation.getTimeBlock() != null ? 
                allocation.getTimeBlock().getDayOfWeek() + " " + 
                allocation.getTimeBlock().getStartTime() + "-" + 
                allocation.getTimeBlock().getEndTime() : "Unknown";
            
            sb.append(classroomInfo).append(" (").append(timeBlockInfo).append(")");
            if (i < allocations.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @FXML
    private void handleSearch() {
        filterRooms();
    }

    private void filterRooms() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            filteredRooms.clear();
            filteredRooms.addAll(listRooms);
        } else {
            filteredRooms.clear();
            for (Room room : listRooms) {
                if (matchesSearch(room, searchText)) {
                    filteredRooms.add(room);
                }
            }
        }
        
        tableRoom.refresh();
    }

    private boolean matchesSearch(Room room, String searchText) {

        if (room.getName() != null && room.getName().toLowerCase().contains(searchText)) {
            return true;
        }
        
        if (room.getLocalization() != null && room.getLocalization().toLowerCase().contains(searchText)) {
            return true;
        }

        if (room.getResources() != null) {
            for (String resource : room.getResources()) {
                if (resource.toLowerCase().contains(searchText)) {
                    return true;
                }
            }
        }

        if (searchText.matches("\\d+")) {
            int capacity = Integer.parseInt(searchText);
            if (room.getCapacity() == capacity) {
                return true;
            }
        }
        
        return false;
    }
}
package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddAllocationDialogController;
import org.example.controller.ui.EditPopUp.EditAllocationDialogController;
import org.example.exceptions.NotFoundException; // Import necessário
import org.example.models.Classroom;
import org.example.models.Room;
import org.example.models.TimeAllocation;
import org.example.models.TimeBlock;
import org.example.service.ClassroomService;
import org.example.service.RoomService;
import org.example.service.TimeAllocationService;
import org.example.service.TimeBlockService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashBoardAllocationsController extends BaseDashboardController implements Initializable {

    @FXML private TableView<TimeAllocation> allocationTable;
    @FXML private TableColumn<TimeAllocation, String> columnClassroom;
    @FXML private TableColumn<TimeAllocation, String> columnRoom;
    @FXML private TableColumn<TimeAllocation, String> columnDay;
    @FXML private TableColumn<TimeAllocation, String> columnStartTime;
    @FXML private TableColumn<TimeAllocation, String> columnEndTime;
    @FXML private TableColumn<TimeAllocation, Void> columnActions;
    @FXML private TextField searchField;

    private final TimeAllocationService timeAllocationService = new TimeAllocationService();

    private ObservableList<TimeAllocation> allocationList;
    private FilteredList<TimeAllocation> filteredAllocationList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadAllocations();
        setupSearch();
    }

    private void setupTable() {
        columnClassroom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassroom() != null ? "Semester: " + cellData.getValue().getClassroom().getSemester() : "N/A"));
        columnRoom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoom() != null ? cellData.getValue().getRoom().getName() : "N/A"));
        columnDay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeBlock() != null ? cellData.getValue().getTimeBlock().getDayOfWeek().toString() : "N/A"));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        columnStartTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeBlock() != null && cellData.getValue().getTimeBlock().getStartTime() != null ? timeFormatter.format(cellData.getValue().getTimeBlock().getStartTime()) : "N/A"));
        columnEndTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeBlock() != null && cellData.getValue().getTimeBlock().getEndTime() != null ? timeFormatter.format(cellData.getValue().getTimeBlock().getEndTime()) : "N/A"));

        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> handleEditAllocation(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteAllocation(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });

        allocationTable.setPlaceholder(new Label("No allocations found"));
    }

    private void setupSearch() {
        allocationList = FXCollections.observableArrayList();
        filteredAllocationList = new FilteredList<>(allocationList, p -> true);
        allocationTable.setItems(filteredAllocationList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredAllocationList.setPredicate(alloc -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                if (alloc.getClassroom() != null && alloc.getClassroom().getSemester().toLowerCase().contains(filter)) return true;
                if (alloc.getRoom() != null && alloc.getRoom().getName().toLowerCase().contains(filter)) return true;
                if (alloc.getTimeBlock() != null && alloc.getTimeBlock().getDayOfWeek().toString().toLowerCase().contains(filter)) return true;
                return false;
            });
        });
    }

    private void loadAllocations() {
        try {
            allocationList.setAll(timeAllocationService.getAll());
        } catch (Exception e) {
            showAlert("Error", "Failed to load allocations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddAllocation() {
        showAllocationDialog(null);
    }

    private void handleEditAllocation(TimeAllocation allocation) {
        showAllocationDialog(allocation);
    }

    private void showAllocationDialog(TimeAllocation allocation) {
        try {
            String fxmlFile = allocation == null ? "/org/example/view/AddPopUp/AddAllocationDialog.fxml" : "/org/example/view/EditPopUp/EditAllocationDialog.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (allocation == null) { // Add mode
                AddAllocationDialogController controller = loader.getController();
                controller.setParentController(this);
            } else { // Edit mode
                EditAllocationDialogController controller = loader.getController();
                controller.setAllocation(allocation);
                controller.setParentController(this);
            }

            Stage stage = new Stage();
            stage.setTitle(allocation == null ? "Add New Time Allocation" : "Edit Time Allocation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Failed to open dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteAllocation(TimeAllocation allocation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this allocation?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    timeAllocationService.deleteById(allocation.getId());
                    allocationList.remove(allocation);
                    showAlert("Success", "Allocation deleted successfully!", Alert.AlertType.INFORMATION);

                } catch (NotFoundException e) {
                    showAlert("Error", "Could not delete allocation: " + e.getMessage(), Alert.AlertType.ERROR);
                } catch (IOException e) {
                    showAlert("Error", "Failed to access data file: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void addAllocation(TimeAllocation allocation) {
        allocationList.add(allocation);
    }

    public void updateAllocation(TimeAllocation oldAllocation, TimeAllocation newAllocation) {
        int index = allocationList.indexOf(oldAllocation);
        if (index != -1) {
            allocationList.set(index, newAllocation);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
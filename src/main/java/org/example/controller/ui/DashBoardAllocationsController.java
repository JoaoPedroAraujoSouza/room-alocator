package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddAllocationDialogController;
import org.example.controller.ui.EditPopUp.EditAllocationDialogController;
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

    @FXML private HBox homeBox;
    @FXML private HBox roomBox;
    @FXML private HBox allocationBox;
    @FXML private HBox teacherBox;
    @FXML private HBox subjectBox;
    @FXML private HBox classroomBox;
    @FXML private HBox reportsBox;

    private final TimeAllocationService timeAllocationService = new TimeAllocationService();
    private final ClassroomService classroomService = new ClassroomService();
    private final RoomService roomService = new RoomService();
    private final TimeBlockService timeBlockService = new TimeBlockService();
    
    private ObservableList<TimeAllocation> allocationList;
    private FilteredList<TimeAllocation> filteredAllocationList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupSearch();
        loadAllocations();
        highlightCurrentSection();
    }

    private void setupTable() {
        columnClassroom.setCellValueFactory(cellData -> {
            Classroom classroom = cellData.getValue().getClassroom();
            return new SimpleStringProperty(classroom != null ? "Semester: " + classroom.getSemester() : "N/A");
        });
        columnRoom.setCellValueFactory(cellData -> {
            Room room = cellData.getValue().getRoom();
            return new SimpleStringProperty(room != null ? room.getName() : "N/A");
        });
        columnDay.setCellValueFactory(cellData -> {
            TimeBlock timeBlock = cellData.getValue().getTimeBlock();
            return new SimpleStringProperty(timeBlock != null ? timeBlock.getDayOfWeek().toString() : "N/A");
        });
        columnStartTime.setCellValueFactory(cellData -> {
            TimeBlock timeBlock = cellData.getValue().getTimeBlock();
            if (timeBlock != null && timeBlock.getStartTime() != null) {
                return new SimpleStringProperty(timeBlock.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            return new SimpleStringProperty("N/A");
        });
        columnEndTime.setCellValueFactory(cellData -> {
            TimeBlock timeBlock = cellData.getValue().getTimeBlock();
            if (timeBlock != null && timeBlock.getEndTime() != null) {
                return new SimpleStringProperty(timeBlock.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            return new SimpleStringProperty("N/A");
        });

        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                
                editButton.setOnAction(event -> {
                    TimeAllocation allocation = getTableView().getItems().get(getIndex());
                    handleEditAllocation(allocation);
                });
                
                deleteButton.setOnAction(event -> {
                    TimeAllocation allocation = getTableView().getItems().get(getIndex());
                    handleDeleteAllocation(allocation);
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

        allocationTable.setPlaceholder(new Label("No allocations found"));
        allocationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setupSearch() {
        allocationList = FXCollections.observableArrayList();
        filteredAllocationList = new FilteredList<>(allocationList, p -> true);
        allocationTable.setItems(filteredAllocationList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAllocationList.setPredicate(allocation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (allocation.getClassroom() != null && 
                    allocation.getClassroom().getSemester().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (allocation.getRoom() != null && 
                    allocation.getRoom().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (allocation.getTimeBlock() != null && 
                    allocation.getTimeBlock().getDayOfWeek().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (allocation.getTimeBlock() != null) {
                    String startTime = allocation.getTimeBlock().getStartTime() != null ? 
                        allocation.getTimeBlock().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "";
                    String endTime = allocation.getTimeBlock().getEndTime() != null ? 
                        allocation.getTimeBlock().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "";
                    
                    if (startTime.contains(lowerCaseFilter) || endTime.contains(lowerCaseFilter)) {
                        return true;
                    }
                }

                return false;
            });
        });
    }

    private void loadAllocations() {
        try {
            List<TimeAllocation> allocations = timeAllocationService.getAll();
            allocationList.clear();
            allocationList.addAll(allocations);
        } catch (Exception e) {
            showAlert("Error", "Failed to load allocations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddAllocation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddAllocationDialog.fxml"));
            Parent root = loader.load();
            
            AddAllocationDialogController controller = loader.getController();
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Add New Time Allocation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            showAlert("Error", "Failed to open add allocation dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditAllocation(TimeAllocation allocation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditAllocationDialog.fxml"));
            Parent root = loader.load();
            
            EditAllocationDialogController controller = loader.getController();
            controller.setAllocation(allocation);
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Time Allocation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            showAlert("Error", "Failed to open edit allocation dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteAllocation(TimeAllocation allocation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Time Allocation");
        alert.setContentText("Are you sure you want to delete this allocation?\n\n" +
                           "Classroom: " + (allocation.getClassroom() != null ? "Semester: " + allocation.getClassroom().getSemester() : "N/A") + "\n" +
                           "Room: " + (allocation.getRoom() != null ? allocation.getRoom().getName() : "N/A") + "\n" +
                           "Time: " + (allocation.getTimeBlock() != null ? 
                               allocation.getTimeBlock().getDayOfWeek() + " " +
                               allocation.getTimeBlock().getStartTime() + "-" + allocation.getTimeBlock().getEndTime() : "N/A"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                timeAllocationService.deleteById(allocation.getId());
                allocationList.remove(allocation);
                showAlert("Success", "Allocation deleted successfully!", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Error", "Failed to delete allocation: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadAllocations();
        searchField.clear();
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

    private void highlightCurrentSection() {
        homeBox.getStyleClass().remove("active-section");
        roomBox.getStyleClass().remove("active-section");
        allocationBox.getStyleClass().add("active-section");
        teacherBox.getStyleClass().remove("active-section");
        subjectBox.getStyleClass().remove("active-section");
        classroomBox.getStyleClass().remove("active-section");
        reportsBox.getStyleClass().remove("active-section");
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
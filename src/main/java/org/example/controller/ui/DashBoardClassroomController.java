package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddClassroomDialogController;
import org.example.controller.ui.EditPopUp.EditClassroomDialogController;
import org.example.models.Classroom;
import org.example.models.Teacher;
import org.example.models.TimeAllocation;
import org.example.service.ClassroomService;
import org.example.service.TeacherService;
import org.example.service.TimeAllocationService;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashBoardClassroomController extends BaseDashboardController implements Initializable {

    @FXML
    private TableView<Classroom> tableClassroom;
    
    @FXML
    private TableColumn<Classroom, String> columnSemester;
    
    @FXML
    private TableColumn<Classroom, String> columnShift;
    
    @FXML
    private TableColumn<Classroom, Integer> columnMaxCapacity;
    
    @FXML
    private TableColumn<Classroom, String> columnTeacher;
    
    @FXML
    private TableColumn<Classroom, String> columnTimeAllocations;
    
    @FXML
    private TableColumn<Classroom, Void> columnActions;
    
    @FXML
    private TextField txtSearch;
    

    
    private ClassroomService classroomService = new ClassroomService();
    private TeacherService teacherService = new TeacherService();
    private TimeAllocationService timeAllocationService = new TimeAllocationService();
    private ObservableList<Classroom> classroomList = FXCollections.observableArrayList();
    private FilteredList<Classroom> filteredClassroomList;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadClassrooms();
        setupSearch();
    }
    
    private void setupTable() {
        // Configure table columns
        columnSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        columnShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        columnMaxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxStudentsCapacity"));
        
        // Setup teacher column to show teacher name
        columnTeacher.setCellValueFactory(cellData -> {
            Teacher teacher = cellData.getValue().getResponsibleTeacher();
            return new SimpleStringProperty(
                teacher != null ? teacher.getName() : "Not assigned"
            );
        });
        
        // Setup time allocations column
        columnTimeAllocations.setCellValueFactory(cellData -> {
            Classroom classroom = cellData.getValue();
            List<TimeAllocation> allocations = timeAllocationService.getAll().stream()
                .filter(allocation -> allocation.getClassroom() != null && 
                        allocation.getClassroom().getId() == classroom.getId())
                .toList();
            
            if (allocations.isEmpty()) {
                return new SimpleStringProperty("No allocations");
            } else {
                String allocationText = allocations.size() + " allocation(s)";
                return new SimpleStringProperty(allocationText);
            }
        });
      
        setupActionsColumn();
        
        filteredClassroomList = new FilteredList<>(classroomList, p -> true);
        tableClassroom.setItems(filteredClassroomList);
    }
    
    private void setupActionsColumn() {
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);
            
            {
                editButton.setOnAction(event -> {
                    Classroom classroom = getTableView().getItems().get(getIndex());
                    handleEditClassroom(classroom);
                });
                
                deleteButton.setOnAction(event -> {
                    Classroom classroom = getTableView().getItems().get(getIndex());
                    handleDeleteClassroom(classroom);
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
    }
    
    private void loadClassrooms() {
        List<Classroom> classrooms = classroomService.getAll();
        classroomList.clear();
        classroomList.addAll(classrooms);
    }
    
    private void setupSearch() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredClassroomList.setPredicate(classroom -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return classroom.getSemester().toLowerCase().contains(lowerCaseFilter) ||
                       classroom.getShift().getDisplayName().toLowerCase().contains(lowerCaseFilter) ||
                       (classroom.getResponsibleTeacher() != null && 
                        classroom.getResponsibleTeacher().getName().toLowerCase().contains(lowerCaseFilter));
            });
        });
    }
    
    @FXML
    public void CreateClassroom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddClassroomDialog.fxml"));
            Parent root = loader.load();
            
            AddClassroomDialogController controller = loader.getController();
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Add New Classroom");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Add Classroom dialog", Alert.AlertType.ERROR);
        }
    }
    
    private void handleEditClassroom(Classroom classroom) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditClassroomDialog.fxml"));
            Parent root = loader.load();
            
            EditClassroomDialogController controller = loader.getController();
            controller.setClassroom(classroom);
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Classroom");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Edit Classroom dialog", Alert.AlertType.ERROR);
        }
    }
    
    private void handleDeleteClassroom(Classroom classroom) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Classroom");
        alert.setContentText("Are you sure you want to delete this classroom?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    classroomService.deleteById(classroom.getId());
                    classroomList.remove(classroom);
                    showAlert("Success", "Classroom deleted successfully", Alert.AlertType.INFORMATION);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Could not delete classroom", Alert.AlertType.ERROR);
                }
            }
        });
    }
    

    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void addClassroomToTable(Classroom classroom) {
        classroomList.add(classroom);
    }
    
    public void updateClassroomInTable(Classroom classroom) {
        int index = classroomList.indexOf(classroom);
        if (index != -1) {
            classroomList.set(index, classroom);
        }
    }
    
    public List<Teacher> getAllTeachers() {
        return teacherService.getAll();
    }
} 
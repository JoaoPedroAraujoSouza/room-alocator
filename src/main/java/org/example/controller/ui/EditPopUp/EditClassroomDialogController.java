package org.example.controller.ui.EditPopUp;

import java.io.IOException;
import java.util.List;

import org.example.controller.ui.DashBoardClassroomController;
import org.example.models.Classroom;
import org.example.models.Shift;
import org.example.models.Teacher;
import org.example.models.TimeAllocation;
import org.example.service.ClassroomService;
import org.example.service.TeacherService;
import org.example.service.TimeAllocationService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditClassroomDialogController {

    @FXML
    private TextField semesterField;
    
    @FXML
    private ComboBox<Shift> shiftComboBox;
    
    @FXML
    private Spinner<Integer> capacitySpinner;
    
    @FXML
    private ComboBox<Teacher> teacherComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    private TableView<TimeAllocation> timeAllocationsTable;
    
    @FXML
    private TableColumn<TimeAllocation, String> columnDay;
    
    @FXML
    private TableColumn<TimeAllocation, String> columnStartTime;
    
    @FXML
    private TableColumn<TimeAllocation, String> columnEndTime;
    
    @FXML
    private TableColumn<TimeAllocation, String> columnRoom;
    
    private DashBoardClassroomController parentController;
    private ClassroomService classroomService = new ClassroomService();
    private TeacherService teacherService = new TeacherService();
    private TimeAllocationService timeAllocationService = new TimeAllocationService();
    private Classroom classroomToEdit;
    private ObservableList<TimeAllocation> timeAllocationList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        setupShiftComboBox();
        setupCapacitySpinner();
        loadTeachers();
        setupTimeAllocationsTable();
        setupValidation();
    }
    
    private void setupShiftComboBox() {
        ObservableList<Shift> shifts = FXCollections.observableArrayList(Shift.values());
        shiftComboBox.setItems(shifts);
        shiftComboBox.setCellFactory(param -> new ListCell<Shift>() {
            @Override
            protected void updateItem(Shift item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
        shiftComboBox.setButtonCell(shiftComboBox.getCellFactory().call(null));
    }
    
    private void setupCapacitySpinner() {
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 30);
        capacitySpinner.setValueFactory(valueFactory);
    }
    
    private void setupTimeAllocationsTable() {
        // Configure time allocations table columns
        columnDay.setCellValueFactory(cellData -> {
            TimeAllocation allocation = cellData.getValue();
            return new SimpleStringProperty(
                allocation.getTimeBlock() != null ? allocation.getTimeBlock().getDayOfWeek().toString() : "Not assigned"
            );
        });
        columnStartTime.setCellValueFactory(cellData -> {
            TimeAllocation allocation = cellData.getValue();
            if (allocation.getTimeBlock() != null && allocation.getTimeBlock().getStartTime() != null) {
                return new SimpleStringProperty(allocation.getTimeBlock().getStartTime().toString());
            }
            return new SimpleStringProperty("Not assigned");
        });
        columnEndTime.setCellValueFactory(cellData -> {
            TimeAllocation allocation = cellData.getValue();
            if (allocation.getTimeBlock() != null && allocation.getTimeBlock().getEndTime() != null) {
                return new SimpleStringProperty(allocation.getTimeBlock().getEndTime().toString());
            }
            return new SimpleStringProperty("Not assigned");
        });
        columnRoom.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(
                cellData.getValue().getRoom() != null ? cellData.getValue().getRoom().getName() : "Not assigned"
            );
        });
        
        // Set table data
        timeAllocationsTable.setItems(timeAllocationList);
    }
    
    private void loadTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAll();
            ObservableList<Teacher> teacherList = FXCollections.observableArrayList(teachers);
            teacherComboBox.setItems(teacherList);
            
            teacherComboBox.setCellFactory(param -> new ListCell<Teacher>() {
                @Override
                protected void updateItem(Teacher item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " (" + item.getEmail() + ")");
                    }
                }
            });
            teacherComboBox.setButtonCell(teacherComboBox.getCellFactory().call(null));
        } catch (Exception e) {
            showAlert("Error", "Failed to load teachers: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadTimeAllocations() {
        if (classroomToEdit == null) {
            timeAllocationList.clear();
            return;
        }
        
        List<TimeAllocation> allAllocations = timeAllocationService.getAll();
        List<TimeAllocation> classroomAllocations = allAllocations.stream()
            .filter(allocation -> allocation.getClassroom() != null && 
                    allocation.getClassroom().getId() == classroomToEdit.getId())
            .toList();
        
        timeAllocationList.clear();
        timeAllocationList.addAll(classroomAllocations);
    }
    
    private void setupValidation() {
        saveButton.setDisable(true);
        
        semesterField.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        shiftComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateForm());
        teacherComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateForm());
    }
    
    private void validateForm() {
        boolean isValid = semesterField.getText() != null && !semesterField.getText().trim().isEmpty() &&
                         shiftComboBox.getValue() != null &&
                         teacherComboBox.getValue() != null;
        saveButton.setDisable(!isValid);
    }
    
    public void setClassroom(Classroom classroom) {
        this.classroomToEdit = classroom;
        populateFields();
        loadTimeAllocations();
    }
    
    private void populateFields() {
        if (classroomToEdit != null) {
            semesterField.setText(classroomToEdit.getSemester());
            shiftComboBox.setValue(classroomToEdit.getShift());
            capacitySpinner.getValueFactory().setValue(classroomToEdit.getMaxStudentsCapacity());
            teacherComboBox.setValue(classroomToEdit.getResponsibleTeacher());
        }
    }
    
    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        try {
            updateClassroom();
            classroomService.update(classroomToEdit);
            
            if (parentController != null) {
                parentController.updateClassroomInTable(classroomToEdit);
            }
            
            showAlert("Success", "Classroom updated successfully!", Alert.AlertType.INFORMATION);
            closeDialog();
        } catch (IOException e) {
            showAlert("Error", "Failed to update classroom: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean validateInput() {
        if (semesterField.getText() == null || semesterField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a semester.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (shiftComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a shift.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (teacherComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a responsible teacher.", Alert.AlertType.WARNING);
            return false;
        }
        
        int capacity = capacitySpinner.getValue();
        if (capacity <= 0) {
            showAlert("Validation Error", "Capacity must be greater than 0.", Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    private void updateClassroom() {
        classroomToEdit.setSemester(semesterField.getText().trim());
        classroomToEdit.setShift(shiftComboBox.getValue());
        classroomToEdit.setMaxStudentsCapacity(capacitySpinner.getValue());
        classroomToEdit.setResponsibleTeacher(teacherComboBox.getValue());
    }
    
    @FXML
    private void handleCancel() {
        closeDialog();
    }
    
    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void setParentController(DashBoardClassroomController parentController) {
        this.parentController = parentController;
    }
} 
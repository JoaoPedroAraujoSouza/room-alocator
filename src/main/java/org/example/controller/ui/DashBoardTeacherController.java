package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddTeacherDialogController;
import org.example.controller.ui.EditPopUp.EditTeacherDialogController;
import org.example.models.Teacher;
import org.example.models.TeacherSubjectLink;
import org.example.service.SubjectService;
import org.example.service.TeacherService;
import org.example.service.TeacherSubjectLinkService;

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


public class DashBoardTeacherController extends BaseDashboardController implements Initializable {

    @FXML
    private TableView<Teacher> tableTeacher;
    
    @FXML
    private TableColumn<Teacher, String> columnName;
    
    @FXML
    private TableColumn<Teacher, String> columnCpf;
    
    @FXML
    private TableColumn<Teacher, String> columnEmail;
    
    @FXML
    private TableColumn<Teacher, String> columnSubjectLinks;
    
    @FXML
    private TableColumn<Teacher, Void> columnActions;
    
    @FXML
    private TextField txtSearch;
    

    
    private TeacherService teacherService = new TeacherService();
    private SubjectService subjectService = new SubjectService();
    private TeacherSubjectLinkService teacherSubjectLinkService = new TeacherSubjectLinkService();
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    private FilteredList<Teacher> filteredTeacherList;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadTeachers();
        setupSearch();
    }
    
    private void setupTable() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        columnSubjectLinks.setCellValueFactory(cellData -> {
            Teacher teacher = cellData.getValue();
            List<TeacherSubjectLink> links = teacherSubjectLinkService.getAll().stream()
                .filter(link -> link.getTeacher() != null && 
                        link.getTeacher().getId() == teacher.getId())
                .toList();
            
            if (links.isEmpty()) {
                return new SimpleStringProperty("No links");
            } else {
                String linkText = links.size() + " subject(s)";
                return new SimpleStringProperty(linkText);
            }
        });
        
        setupActionsColumn();

        filteredTeacherList = new FilteredList<>(teacherList, p -> true);
        tableTeacher.setItems(filteredTeacherList);
    }
    
    private void setupActionsColumn() {
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);
            
            {
                editButton.setOnAction(event -> {
                    Teacher teacher = getTableView().getItems().get(getIndex());
                    handleEditTeacher(teacher);
                });
                
                deleteButton.setOnAction(event -> {
                    Teacher teacher = getTableView().getItems().get(getIndex());
                    handleDeleteTeacher(teacher);
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
    
    private void loadTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAll();
            teacherList.clear();
            teacherList.addAll(teachers);
        } catch (Exception e) {
            showAlert("Error", "Failed to load teachers: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void setupSearch() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTeacherList.setPredicate(teacher -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return teacher.getName().toLowerCase().contains(lowerCaseFilter) ||
                       teacher.getCpf().toLowerCase().contains(lowerCaseFilter) ||
                       teacher.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
    
    @FXML
    public void CreateTeacher() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddTeacherDialog.fxml"));
            Parent root = loader.load();
            
            AddTeacherDialogController controller = loader.getController();
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Add New Teacher");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Add Teacher dialog", Alert.AlertType.ERROR);
        }
    }
    
    private void handleEditTeacher(Teacher teacher) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditTeacherDialog.fxml"));
            Parent root = loader.load();
            
            EditTeacherDialogController controller = loader.getController();
            controller.setTeacher(teacher);
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Teacher");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Edit Teacher dialog", Alert.AlertType.ERROR);
        }
    }
    
    private void handleDeleteTeacher(Teacher teacher) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete teacher '" + teacher.getName() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    teacherService.deleteById(teacher.getId());
                    teacherList.remove(teacher);
                    showAlert("Success", "Teacher deleted successfully!", Alert.AlertType.INFORMATION);
                } catch (IOException e) {
                    showAlert("Error", "Failed to delete teacher: " + e.getMessage(), Alert.AlertType.ERROR);
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
    
    public void addTeacherToTable(Teacher teacher) {
        teacherList.add(teacher);
    }
    
    public void updateTeacherInTable(Teacher teacher) {

    }
} 
package org.example.controller.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.example.controller.ui.AddPopUp.AddSubjectDialogController;
import org.example.controller.ui.AddPopUp.AddTeacherSubjectLinkDialogController;
import org.example.controller.ui.EditPopUp.EditSubjectDialogController;
import org.example.controller.ui.EditPopUp.EditTeacherSubjectLinkDialogController;
import org.example.models.Subject;
import org.example.models.TeacherSubjectLink;
import org.example.service.SubjectService;
import org.example.service.TeacherSubjectLinkService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DashBoardSubjectController extends BaseDashboardController implements Initializable {

    private ObservableList<Subject> subjectList = FXCollections.observableArrayList();
    private ObservableList<Subject> filteredSubjects = FXCollections.observableArrayList();
    private Subject selectedSubject;
    private SubjectService subjectService = new SubjectService();
    private final TeacherSubjectLinkService teacherSubjectLinkService = new TeacherSubjectLinkService();

    @FXML
    private TableView<Subject> subjectTable;
    @FXML private TableColumn<Subject, String> columnName;
    @FXML private TableColumn<Subject, String> columnDescription;
    @FXML private TableColumn<Subject, Integer> columnHourlyLoad;
    @FXML private TableColumn<Subject, Void> columnActions;

    @FXML private TextField txtSearch;


    @FXML private VBox linkSection;
    @FXML private TableView<TeacherSubjectLink> linkTable;
    @FXML private TableColumn<TeacherSubjectLink, String> columnTeacherName;
    @FXML private TableColumn<TeacherSubjectLink, String> columnSemester;
    @FXML private TableColumn<TeacherSubjectLink, Boolean> columnActive;
    @FXML private TableColumn<TeacherSubjectLink, String> columnClassroom;
    @FXML private TableColumn<TeacherSubjectLink, Void> columnLinkActions;
    @FXML private Button addLinkButton;
    @FXML private Button editLinkButton;
    @FXML private Button removeLinkButton;

    private ObservableList<TeacherSubjectLink> linkList = FXCollections.observableArrayList();
    private TeacherSubjectLink selectedLink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnHourlyLoad.setCellValueFactory(new PropertyValueFactory<>("hourlyLoad"));
        
        columnActions.setCellFactory(col -> new TableCell<Subject, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(e -> {
                    Subject subject = getTableView().getItems().get(getIndex());
                    handleEditSubject(subject);
                });

                deleteButton.setOnAction(e -> {
                    Subject subject = getTableView().getItems().get(getIndex());
                    handleDeleteSubject(subject);
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

        subjectTable.setItems(filteredSubjects);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSubjects();
        });
        
        loadExistingSubjects();

        columnTeacherName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
        cellData.getValue().getTeacher() != null ? cellData.getValue().getTeacher().getName() : ""));
        columnSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        columnActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        columnClassroom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getClassroom() != null ? cellData.getValue().getClassroom().getSemester() : ""));
        columnLinkActions.setCellFactory(col -> new TableCell<TeacherSubjectLink, Void>() {
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Remover");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);
            {
                editButton.setOnAction(e -> {
                    TeacherSubjectLink link = getTableView().getItems().get(getIndex());
                    handleEditLink(link);
                });
                deleteButton.setOnAction(e -> {
                    TeacherSubjectLink link = getTableView().getItems().get(getIndex());
                    handleRemoveLink(link);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        linkTable.setItems(linkList);
        linkSection.setVisible(false);
        linkSection.setManaged(false);
        editLinkButton.setDisable(true);
        removeLinkButton.setDisable(true);
    }

    private void loadExistingSubjects() {
        try {
            List<Subject> existingSubjects = subjectService.getAllSubjects();
            subjectList.addAll(existingSubjects);
            filterSubjects(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        filterSubjects();
    }

    private void filterSubjects() {
        String searchText = txtSearch.getText().toLowerCase();
        filteredSubjects.clear();
        
        for (Subject subject : subjectList) {
            if (matchesSearch(subject, searchText)) {
                filteredSubjects.add(subject);
            }
        }
    }

    private boolean matchesSearch(Subject subject, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        
        return (subject.getName() != null && subject.getName().toLowerCase().contains(searchText)) ||
               (subject.getDescription() != null && subject.getDescription().toLowerCase().contains(searchText));
    }

    @FXML
    private void handleSubjectSelection(MouseEvent event) {
        selectedSubject = subjectTable.getSelectionModel().getSelectedItem();
        if (selectedSubject != null) {
            linkList.setAll(loadLinksForSubject(selectedSubject));
            linkSection.setVisible(true);
            linkSection.setManaged(true);
        } else {
            linkSection.setVisible(false);
            linkSection.setManaged(false);
            linkList.clear();
        }
        editLinkButton.setDisable(true);
        removeLinkButton.setDisable(true);
    }

    @FXML
    private void handleLinkSelection(MouseEvent event) {
        selectedLink = linkTable.getSelectionModel().getSelectedItem();
        boolean hasSelection = selectedLink != null;
        editLinkButton.setDisable(!hasSelection);
        removeLinkButton.setDisable(!hasSelection);
    }

    @FXML
    private void handleAddSubject() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddSubjectDialog.fxml"));
            Scene scene = new Scene(loader.load());
            AddSubjectDialogController dialogController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Subject");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            Subject created = dialogController.getCreatedSubject();
            if (created != null) {
                subjectService.addSubject(created);
                subjectList.add(created);
                filterSubjects();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditSubject(Subject subject) {
        if (subject == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditSubjectDialog.fxml"));
            Scene scene = new Scene(loader.load());
            EditSubjectDialogController dialogController = loader.getController();
            dialogController.setSubject(subject);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Subject");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            if (dialogController.isUpdated()) {
                subjectService.updateSubject(subject);
                subjectTable.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteSubject(Subject subject) {
        if (subject == null) return;
        subjectList.remove(subject);
        filterSubjects(); 
        selectedSubject = null;
        subjectTable.refresh();
    }

    @FXML
    private void handleAddLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/AddPopUp/AddTeacherSubjectLinkDialog.fxml"));
            Scene scene = new Scene(loader.load());
            AddTeacherSubjectLinkDialogController dialogController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Teacher-Subject Link");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            TeacherSubjectLink created = dialogController.getCreatedLink();
            if (created != null && selectedSubject != null) {
                created.setSubject(selectedSubject);
                try {
                    teacherSubjectLinkService.addLink(created);
                    linkList.add(created);
                    linkTable.refresh();
                    System.out.println("[LOG] Persisted TeacherSubjectLink: " + created);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("[LOG] Error persisting TeacherSubjectLink: " + e.getMessage());
                }
            } else {
                System.out.println("[LOG] No link created or no subject selected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[LOG] Error opening AddTeacherSubjectLinkDialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditLink() {
        if (selectedLink != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/EditPopUp/EditTeacherSubjectLinkDialog.fxml"));
                Scene scene = new Scene(loader.load());
                EditTeacherSubjectLinkDialogController dialogController = loader.getController();
                dialogController.setLink(selectedLink);
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Teacher-Subject Link");
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
                if (dialogController.isUpdated()) {
                    try {
                        teacherSubjectLinkService.updateLink(selectedLink);
                        linkTable.refresh();
                        System.out.println("[LOG] Persisted update for TeacherSubjectLink: " + selectedLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("[LOG] Error updating TeacherSubjectLink: " + e.getMessage());
                    }
                } else {
                    System.out.println("[LOG] EditTeacherSubjectLinkDialog canceled or not updated.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[LOG] Error opening EditTeacherSubjectLinkDialog: " + e.getMessage());
            }
        }
    }

    private void handleEditLink(TeacherSubjectLink link) {
        selectedLink = link;
        handleEditLink();
    }

    @FXML
    private void handleRemoveLink() {
        if (selectedLink != null) {
            handleRemoveLink(selectedLink);
        }
    }

    private void handleRemoveLink(TeacherSubjectLink link) {
        try {
            teacherSubjectLinkService.deleteLinkById(link.getId());
            linkList.remove(link);
            linkTable.refresh();
            editLinkButton.setDisable(true);
            removeLinkButton.setDisable(true);
            System.out.println("[LOG] Persisted removal of TeacherSubjectLink: " + link);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[LOG] Error removing TeacherSubjectLink: " + e.getMessage());
        }
    }

    private List<TeacherSubjectLink> loadLinksForSubject(Subject subject) {
        try {
            return teacherSubjectLinkService.getAllLinks()
                .stream()
                .filter(link -> link.getSubject() != null && link.getSubject().getId() == subject.getId())
                .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 
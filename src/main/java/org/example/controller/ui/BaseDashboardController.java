package org.example.controller.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public abstract class BaseDashboardController {
    
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    public void switchToHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardHome.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToRooms(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardRooms.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToAllocations(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardAllocations.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTeacher(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardTeacher.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSubject(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardSubject.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToClassroom(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardClassroom.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardReports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);

        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
} 
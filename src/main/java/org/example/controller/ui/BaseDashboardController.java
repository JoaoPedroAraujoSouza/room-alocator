package org.example.controller.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller base abstrato para todas as telas Dashboard
 * Fornece métodos de navegação comuns
 */
public abstract class BaseDashboardController {
    
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    /**
     * Navega para a tela Home
     */
    public void switchToHome(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardHome.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Salas
     */
    public void switchToRooms(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardRooms.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Alocações
     */
    public void switchToAllocations(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardAllocations.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Professores
     */
    public void switchToTeacher(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardTeacher.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Disciplinas
     */
    public void switchToSubject(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardSubject.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Turmas
     */
    public void switchToClassroom(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardClassroom.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega para a tela de Relatórios
     */
    public void switchToReports(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/DashBoardReports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1315, 890);
        // Aplicar CSS
        String css = getClass().getResource("/resources/css/DashBoardBaseStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
} 
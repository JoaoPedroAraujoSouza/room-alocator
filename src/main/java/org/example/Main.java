package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args); 
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("/resources/css/layout.css").toExternalForm();
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e)  {
            e.printStackTrace();
        }

    }
}
package de.thkoeln.swp.wawi.admingui.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        Parent content;
        try {

            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Hauptmenue.fxml"));
            content = viewLoader.load();

            stage = new Stage();
            stage.setTitle("Admin GUI - Hauptmenü");
            stage.setScene(new Scene(content, 960, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAdminApp() {
        if (stage != null)
            stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

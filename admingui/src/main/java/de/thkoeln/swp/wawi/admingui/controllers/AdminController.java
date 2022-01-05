package de.thkoeln.swp.wawi.admingui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminController {


    @FXML
    private void oeffneProdukte(Event e) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Produkte.fxml"));
            Parent content = viewLoader.load();

            Scene produkte = new Scene(content, 960, 540);
            Scene currentScene = ((Button) e.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("Admin GUI - Produkte");
            stage.setScene(produkte);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void oeffneKategorien(Event e) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Kategorien.fxml"));
            Parent content = viewLoader.load();

            Scene kategorien = new Scene(content, 960, 540);
            Scene currentScene = ((Button) e.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();

            stage.setTitle("Admin GUI - Kategorien");
            stage.setScene(kategorien);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void oeffneLagerverkehr(Event e) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Lagerverkehr.fxml"));
            Parent content = viewLoader.load();

            Scene lagerverkehr = new Scene(content, 960, 540);
            Scene currentScene = ((Button) e.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("Admin GUI - Lagerverkehr");
            stage.setScene(lagerverkehr);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void schliessen(Event e) {
        Scene currentScene = (((Button) e.getSource()).getScene());
        Stage stage = (Stage) currentScene.getWindow();
        if (stage != null) {
            stage.close();
        }
    }

}


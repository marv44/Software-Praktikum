package de.thkoeln.swp.wawi.admingui.controllers;


import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.LagerortGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.ProduktGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.impl.IKategorieVerwaltungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.impl.ILagerVerwaltungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.impl.IProduktVerwaltungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.services.IKategorieVerwaltung;
import de.thkoeln.swp.wawi.adminsteuerung.services.ILagerVerwaltung;
import de.thkoeln.swp.wawi.adminsteuerung.services.IProduktVerwaltung;
import de.thkoeln.swp.wawi.adminsteuerung.services.StueckzahlFilter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ProduktController implements Initializable {

    private ProduktGrenz produkt;
    private IProduktVerwaltung produktVerwaltung;
    private IKategorieVerwaltung kategorieVerwaltung;
    private ILagerVerwaltung lagerVerwaltung;
    private List<ProduktGrenz> produkte;
    private List<KategorieGrenz> kategorien;
    private List<LagerortGrenz> lagerorte;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    TableColumn<ProduktGrenz, String> name;

    @FXML
    TableColumn<ProduktGrenz, LocalDate> angelegt;

    @FXML
    TableColumn<ProduktGrenz, Integer> stueckzahl;

    @FXML
    TableColumn<ProduktGrenz, Float> netto;

    @FXML
    TableColumn<ProduktGrenz, Integer> mwst;

    @FXML
    TableColumn<ProduktGrenz, String> lager;

    @FXML
    TableColumn<ProduktGrenz, String> kategorie;

    @FXML
    TableColumn<ProduktGrenz, String> beschreibung;

    @FXML
    TableColumn<ProduktGrenz, String> aktiv;

    @FXML
    TableView<ProduktGrenz> produktTabelle;

    @FXML
    TextField anzahl;

    @FXML
    Button bearbeitenBtn;

    @FXML
    Button loeschenBtn;

    @FXML
    Button erstellenBtn;

    @FXML
    Button aktivierenBtn;

    @FXML
    Button deaktivierenBtn;


    public ProduktController() {
        produktVerwaltung = new IProduktVerwaltungImpl();
        kategorieVerwaltung = new IKategorieVerwaltungImpl();
        lagerVerwaltung = new ILagerVerwaltungImpl();
    }

    public IProduktVerwaltung getProduktVerwaltung() {
        return produktVerwaltung;
    }

    public List<KategorieGrenz> getKategorien() {
        return kategorien;
    }

    public List<LagerortGrenz> getLagerorte() {
        return lagerorte;
    }

    private void produktGewaehlt(ProduktGrenz produktGrenz) {
        produkt = produktGrenz;
        if (produkt == null) {
            produkt = null;
            bearbeitenBtn.setDisable(true);
            loeschenBtn.setDisable(true);
            erstellenBtn.setDisable(true);
        } else {
            bearbeitenBtn.setDisable(false);
            loeschenBtn.setDisable(false);
            erstellenBtn.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        produktTabelle
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> produktGewaehlt(newValue)));

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        angelegt.setCellValueFactory(new PropertyValueFactory<>("angelegt"));
        stueckzahl.setCellValueFactory(new PropertyValueFactory<>("stueckzahl"));
        netto.setCellValueFactory(new PropertyValueFactory<>("nettopreis"));
        mwst.setCellValueFactory(new PropertyValueFactory<>("mwstsatz"));
        lager.setCellValueFactory(cellData -> {
            LagerortGrenz lagerort = lagerorte
                    .stream()
                    .filter(lo -> lo.getLgortid() == cellData.getValue().getLagerort())
                    .findFirst()
                    .orElseThrow();

            return new ReadOnlyObjectWrapper<>(lagerort.getBezeichnung());
        });
        kategorie.setCellValueFactory(cellData -> {
            KategorieGrenz kategorie = kategorien
                    .stream()
                    .filter(k -> k.getKatid() == cellData.getValue().getKategorie())
                    .findFirst()
                    .orElseThrow();

            return new ReadOnlyObjectWrapper<>(kategorie.getName());
        });
        beschreibung.setCellValueFactory(new PropertyValueFactory<>("beschreibung"));
        aktiv.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().isAktiv() ? "Ja" : "Nein")
        );

        ladeProdukte();
    }

    @FXML
    public void ladeProdukte() {
        ladeKategorien();
        ladeLagerorte();
        produkte = produktVerwaltung.getProduktListe();
        produktTabelle.getItems().clear();
        produktTabelle.getItems().addAll(produkte);
        if (produkte.isEmpty()) {
            bearbeitenBtn.setDisable(true);
            loeschenBtn.setDisable(true);
            erstellenBtn.setDisable(true);
        } else {
            bearbeitenBtn.setDisable(false);
            loeschenBtn.setDisable(false);
            erstellenBtn.setDisable(false);
        }
    }

    @FXML
    private void ladeGefilterteProdukte() {
        int parsedAnzahl;
        StueckzahlFilter filter = null;

        try {
            parsedAnzahl = Integer.parseInt(anzahl.getText());
        } catch (Exception e) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle("Ungültige Eingaben");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Die Anzahl ist ungültig. Bitte überprüfe deine Eingaben.");
            notificationAlert.showAndWait();
            return;
        }

        switch (choiceBox.getValue()) {
            case "weniger als":
                filter = StueckzahlFilter.WENIGER_ALS;
                break;
            case "mehr als":
                filter = StueckzahlFilter.MEHR_ALS;
                break;
            case "gleich":
                filter = StueckzahlFilter.GLEICH;
                break;
        }

        if (filter == null) {
            return;
        }

        ladeKategorien();
        ladeLagerorte();
        produkte = produktVerwaltung.getProduktListe(filter, parsedAnzahl);

        produktTabelle.getItems().clear();
        produktTabelle.getItems().addAll(produkte);
        if (produkte.isEmpty()) {
            bearbeitenBtn.setDisable(true);
            loeschenBtn.setDisable(true);
            erstellenBtn.setDisable(true);
        } else {
            bearbeitenBtn.setDisable(false);
            loeschenBtn.setDisable(false);
            erstellenBtn.setDisable(false);
        }
    }

    private void ladeKategorien() {
        kategorien = kategorieVerwaltung.getKategorieListe();
    }

    private void ladeLagerorte() {
        lagerorte = lagerVerwaltung.getLagerortListe();
    }

    public void erstelleProdukt(ActionEvent actionEvent) {
        erstelleUpdateDialog(null);
    }

    public void editiereProdukt(ActionEvent actionEvent) {
        if (produkt == null) {
            return;
        }

        erstelleUpdateDialog(produkt);
    }

    private void erstelleUpdateDialog(ProduktGrenz existingProduct) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/ProduktDialog.fxml"));
            Parent content = viewLoader.load();
            ProduktDialogController dialogController = viewLoader.getController();
            dialogController.setProduktController(this);
            dialogController.setExistingProduct(existingProduct);

            Scene scene = new Scene(content, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void loeschen(ActionEvent actionEvent) {

        ProduktGrenz toDelete = produkt;

        if (toDelete == null) {
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Produkt löschen");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Wollen Sie das Produkt wirklich löschen?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            byte response = produktVerwaltung.produktLoeschen(toDelete.getProdId());

            if (response == 0) {
                Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
                notificationAlert.setTitle("Löschen erfolgreich");
                notificationAlert.setHeaderText(null);
                notificationAlert.setContentText("Das Produkt " + toDelete.getName() + " wurde erfolgreich gelöscht.");
                notificationAlert.showAndWait();
                ladeProdukte();
            } else if (response == 1) {
                Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
                notificationAlert.setTitle("Produkt inaktiv gesetzt");
                notificationAlert.setHeaderText(null);
                notificationAlert.setContentText("Das Produkt " + toDelete.getName() + " wurde auf inaktiv gesetzt, da es bereits bestellt wurde.");
                notificationAlert.showAndWait();
                ladeProdukte();
            } else {
                Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
                notificationAlert.setTitle("Löschen fehlgeschlagen");
                notificationAlert.setHeaderText(null);
                notificationAlert.setContentText("Das Löschen des Produktes " + toDelete.getName() + " ist fehlgeschlagen.");
                notificationAlert.showAndWait();
            }
        }
    }

    @FXML
    private void zurueck(Event e) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Hauptmenue.fxml"));
            Parent content = viewLoader.load();

            Scene hauptmenue = new Scene(content, 960, 510);
            Scene currentScene = ((Button) e.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("Admin GUI - Hauptmenü");
            stage.setScene(hauptmenue);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void produktStatusAnpassen(ActionEvent actionEvent) {
        boolean success;
        boolean activate = ((Button) actionEvent.getSource()).getId().equals("aktivierenBtn");
        int parsedAnzahl;

        try {
            parsedAnzahl = Integer.parseInt(anzahl.getText());
        } catch (Exception e) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle("Ungültige Eingaben");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Die Anzahl ist ungültig. Bitte überprüfe deine Eingaben.");
            notificationAlert.showAndWait();
            return;
        }

        success = activate ? produktVerwaltung.produktAktivieren(parsedAnzahl) : produktVerwaltung.produktDeaktivieren(parsedAnzahl);
        if (success) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle("Produkte erfolgreich " + (activate ? "aktiviert" : "deaktiviert"));
            notificationAlert.setHeaderText(null);
            if (activate) {
                notificationAlert.setContentText("Alle Produkte mit einer Stückzahl von mehr als " + parsedAnzahl + " wurden aktiviert.");
            } else {
                notificationAlert.setContentText("Alle Produkte mit einer Stückzahl von weniger als " + parsedAnzahl + " wurden deaktiviert.");
            }
            notificationAlert.showAndWait();
            ladeProdukte();
        } else {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle("Fehler beim " + (activate ? "Aktivieren" : "Deaktivieren"));
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Es gab einen Fehler beim " + (activate ? "Aktivieren" : "Deaktivieren") + ". Sollte das Problem bestehen bleiben, melde dich bitte beim Support.");
            notificationAlert.showAndWait();
        }
    }

    @FXML
    private void auswahlChoiceBox(ActionEvent actionEvent) {
        switch (choiceBox.getValue()) {
            case "weniger als":
                deaktivierenBtn.setVisible(true);
                aktivierenBtn.setVisible(false);
                break;
            case "mehr als":
                deaktivierenBtn.setVisible(false);
                aktivierenBtn.setVisible(true);
                break;
            case "gleich":
                deaktivierenBtn.setVisible(false);
                aktivierenBtn.setVisible(false);
                break;
        }
    }
}


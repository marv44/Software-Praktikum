package de.thkoeln.swp.wawi.admingui.controllers;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.*;
import de.thkoeln.swp.wawi.adminsteuerung.impl.ILagerVerwaltungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.services.ILagerVerwaltung;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class LagerverkehrController implements Initializable {

    @FXML
    private DatePicker fromInput;
    @FXML
    private DatePicker toInput;
    @FXML
    private Text orLabel;
    @FXML
    private TextField idInput;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<LagerverkehrGrenz> lagerverkehrTable;
    @FXML
    private TableColumn<LagerverkehrGrenz, Integer> lagerverkehrId;
    @FXML
    private TableColumn<LagerverkehrGrenz, String> lagerverkehrTyp;
    @FXML
    private TableColumn<LagerverkehrGrenz, BigDecimal> lagerverkehrGesamtpreis;

    @FXML
    private TableView<EinlieferungGrenz> einlieferungenTable;
    @FXML
    private TableColumn<EinlieferungGrenz, Integer> einlieferungId;
    @FXML
    private TableColumn<EinlieferungGrenz, BigDecimal> einlieferungGesamtpreis;
    @FXML
    private TableColumn<EinlieferungGrenz, String> einlieferungLieferant;
    @FXML
    private TableColumn<EinlieferungGrenz, LocalDate> einlieferungDatum;

    @FXML
    private TableView<BestellungGrenz> bestellungenTable;
    @FXML
    private TableColumn<BestellungGrenz, Integer> bestellungId;
    @FXML
    private TableColumn<BestellungGrenz, BigDecimal> bestellungGesamtpreis;
    @FXML
    private TableColumn<BestellungGrenz, String> bestellungKunde;
    @FXML
    private TableColumn<BestellungGrenz, LocalDate> bestellungDatum;
    @FXML
    private TableColumn<BestellungGrenz, String> bestellungStatus;

    @FXML
    private Pane lieferpositionenPane;
    @FXML
    private TableView<LieferpositionGrenz> lieferpositionenTable;
    @FXML
    private TableColumn<LieferpositionGrenz, String> lieferpositionProduktname;
    @FXML
    private TableColumn<LieferpositionGrenz, Integer> lieferpositionAnzahl;
    @FXML
    private TableColumn<LieferpositionGrenz, BigDecimal> lieferpositionKaufpreis;

    @FXML
    private Pane bestellpositionenPane;
    @FXML
    private TableView<BestellungspositionGrenz> bestellungspositionenTable;
    @FXML
    private TableColumn<BestellungspositionGrenz, String> bestellpositionProduktname;
    @FXML
    private TableColumn<BestellungspositionGrenz, Integer> bestellpositionAnzahl;

    private ILagerVerwaltung lagerVerwaltung;

    public LagerverkehrController() {
        lagerVerwaltung = new ILagerVerwaltungImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add event listeners.
        tabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> wennTabGewechselt(newValue)));
        lagerverkehrTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> wennLagerverkehrGewaehlt(newValue)));
        bestellungenTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> wennBestellungGewaehlt(newValue)));
        einlieferungenTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> wennEinlieferungGewaehlt(newValue)));

        // Add cell value factories for all the tables.
        lagerverkehrId.setCellValueFactory(cellData -> {
            LagerverkehrGrenz lv = cellData.getValue();
            return new ReadOnlyObjectWrapper<>(lv.getBid() != null ? lv.getBid() : lv.getElfid());
        });
        lagerverkehrTyp.setCellValueFactory(new PropertyValueFactory<>("typ"));
        lagerverkehrGesamtpreis.setCellValueFactory(new PropertyValueFactory<>("gesamtPreis"));

        einlieferungId.setCellValueFactory(new PropertyValueFactory<>("elfid"));
        einlieferungGesamtpreis.setCellValueFactory(new PropertyValueFactory<>("gesamtPreis"));
        einlieferungLieferant.setCellValueFactory(new PropertyValueFactory<>("lieferant"));
        einlieferungDatum.setCellValueFactory(new PropertyValueFactory<>("angelegt"));

        bestellungId.setCellValueFactory(new PropertyValueFactory<>("bid"));
        bestellungGesamtpreis.setCellValueFactory(new PropertyValueFactory<>("gesamtPreis"));
        bestellungKunde.setCellValueFactory(new PropertyValueFactory<>("kunde"));
        bestellungDatum.setCellValueFactory(new PropertyValueFactory<>("angelegt"));
        bestellungStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        lieferpositionProduktname.setCellValueFactory(new PropertyValueFactory<>("produkt"));
        lieferpositionAnzahl.setCellValueFactory(new PropertyValueFactory<>("anzahl"));
        lieferpositionKaufpreis.setCellValueFactory(new PropertyValueFactory<>("kaufpreis"));

        bestellpositionProduktname.setCellValueFactory(new PropertyValueFactory<>("produkt"));
        bestellpositionAnzahl.setCellValueFactory(new PropertyValueFactory<>("anzahl"));

        // Set initial visibility
        lieferpositionenPane.setVisible(false);
        bestellpositionenPane.setVisible(false);
        orLabel.setVisible(false);
        idInput.setVisible(false);

        laden();
    }

    private void wennTabGewechselt(Tab tab) {
        lieferpositionenPane.setVisible(false);
        bestellpositionenPane.setVisible(false);

        if (tab.getText().equals("Lagerverkehr")) {
            orLabel.setVisible(false);
            idInput.setVisible(false);
        } else {
            orLabel.setVisible(true);
            idInput.setVisible(true);
        }

        laden();
    }

    private void wennLagerverkehrGewaehlt(LagerverkehrGrenz lagerverkehr) {
        if (lagerverkehr.getElfid() != null) {
            lieferpositionenPane.setVisible(true);
            bestellpositionenPane.setVisible(false);

            List<LieferpositionGrenz> lieferpositionen = lagerVerwaltung.getLieferpositionen(lagerverkehr.getElfid());
            lieferpositionenTable.getItems().clear();
            lieferpositionenTable.getItems().addAll(lieferpositionen);
        }

        if (lagerverkehr.getBid() != null) {
            lieferpositionenPane.setVisible(false);
            bestellpositionenPane.setVisible(true);

            List<BestellungspositionGrenz> bestellungspositionen = lagerVerwaltung.getBestellungspositionen(lagerverkehr.getBid());
            bestellungspositionenTable.getItems().clear();
            bestellungspositionenTable.getItems().addAll(bestellungspositionen);
        }
    }

    private void wennBestellungGewaehlt(BestellungGrenz bestellung) {
        bestellpositionenPane.setVisible(true);

        List<BestellungspositionGrenz> bestellungspositionen = lagerVerwaltung.getBestellungspositionen(bestellung.getBid());
        bestellungspositionenTable.getItems().clear();
        bestellungspositionenTable.getItems().addAll(bestellungspositionen);
    }

    private void wennEinlieferungGewaehlt(EinlieferungGrenz einlieferung) {
        lieferpositionenPane.setVisible(true);

        List<LieferpositionGrenz> lieferpositionen = lagerVerwaltung.getLieferpositionen(einlieferung.getElfid());
        lieferpositionenTable.getItems().clear();
        lieferpositionenTable.getItems().addAll(lieferpositionen);
    }

    @FXML
    private void laden() {
        LocalDate von = fromInput.getValue();
        LocalDate bis = toInput.getValue();
        Integer id = null;

        if (von != null && bis == null) {
            bis = LocalDate.now();
        }

        if (von == null && bis != null) {
            von = LocalDate.MIN;
        }

        if (!idInput.getText().trim().isEmpty()) {
            try {
                id = Integer.parseInt(idInput.getText());
            } catch (Exception e) {
                Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
                notificationAlert.setTitle("Ungültige Eingaben");
                notificationAlert.setHeaderText(null);
                notificationAlert.setContentText("Die ID ist ungültig. Bitte überprüfe deine Eingaben.");
                notificationAlert.showAndWait();
                return;
            }
        }

        switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
            case "Lagerverkehr":
                List<LagerverkehrGrenz> lagerverkehr = (von != null && bis != null)
                        ? lagerVerwaltung.getLagerverkehrListe(von, bis)
                        : lagerVerwaltung.getLagerverkehrListe();

                lagerverkehrTable.getItems().clear();
                lagerverkehrTable.getItems().addAll(lagerverkehr);

                break;
            case "Einlieferungen":
                einlieferungenTable.getItems().clear();

                if (id != null) {
                    EinlieferungGrenz einlieferung = lagerVerwaltung.getEinlieferungById(id);
                    if (einlieferung != null) {
                        einlieferungenTable.getItems().add(einlieferung);
                        einlieferungenTable.getSelectionModel().selectFirst();
                    }
                } else {
                    List<EinlieferungGrenz> einlieferungen = (von != null && bis != null)
                            ? lagerVerwaltung.getEinlieferungsListe(von, bis)
                            : lagerVerwaltung.getEinlieferungsListe();

                    einlieferungenTable.getItems().addAll(einlieferungen);
                }

                break;
            case "Bestellungen":
                bestellungenTable.getItems().clear();

                if (id != null) {
                    BestellungGrenz bestellung = lagerVerwaltung.getBestellungById(id);
                    if (bestellung != null) {
                        bestellungenTable.getItems().add(bestellung);
                        bestellungenTable.getSelectionModel().selectFirst();
                    }
                } else {
                    List<BestellungGrenz> bestellungen = (von != null && bis != null)
                            ? lagerVerwaltung.getBestellungsListe(von, bis)
                            : lagerVerwaltung.getBestellungsListe();

                    bestellungenTable.getItems().addAll(bestellungen);
                }


                break;
        }
    }

    @FXML
    private void zurueckHauptmenue(ActionEvent actionEvent) {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/Hauptmenue.fxml"));
            Parent content = viewLoader.load();

            Scene currentScene = ((Button) actionEvent.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("Admin GUI - Hauptmenü");
            stage.setScene(new Scene(content, 960, 540));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}


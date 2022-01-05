package de.thkoeln.swp.wawi.admingui.controllers;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.LagerortGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.ProduktGrenz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;


public class ProduktDialogController {
    private ProduktController produktController;
    private ProduktGrenz existingProduct;

    @FXML private Text title;
    @FXML private ChoiceBox<KategorieGrenz> categoryChoiceBox;
    @FXML private ChoiceBox<LagerortGrenz> lagerortChoiceBox;
    @FXML private TextField pName;
    @FXML private TextArea pBeschreibung;
    @FXML private TextField pPreis;
    @FXML private TextField pStueck;
    @FXML private TextField pMwst;
    @FXML private CheckBox pAktiv;

    @FXML private Button abbrechenBtn;
    @FXML private Button speichernBtn;

    public void setProduktController(ProduktController produktController) {
        this.produktController = produktController;
        initCategoryChoiceBox();
        initLagerortChoiceBox();
    }

    public void initCategoryChoiceBox() {
        if (categoryChoiceBox != null && produktController != null && produktController.getKategorien() != null) {
            categoryChoiceBox.getItems().clear();
            categoryChoiceBox.getItems().addAll(produktController.getKategorien());
        }
    }

    public void initLagerortChoiceBox() {
        if (lagerortChoiceBox != null && produktController != null && produktController.getLagerorte() != null) {
            lagerortChoiceBox.getItems().clear();
            lagerortChoiceBox.getItems().addAll(produktController.getLagerorte());
        }
    }

    public void setCategory(int katid) {
        if (categoryChoiceBox == null) {
            return;
        }

        try {
            KategorieGrenz category = categoryChoiceBox
                .getItems()
                .stream()
                .filter(k -> k.getKatid() == katid)
                .findFirst()
                .orElseThrow();

            categoryChoiceBox.setValue(category);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void setLagerort(int lid) {
        if (lagerortChoiceBox == null) {
            return;
        }

        try {
            LagerortGrenz lagerort = lagerortChoiceBox
                    .getItems()
                    .stream()
                    .filter(l -> l.getLgortid() == lid)
                    .findFirst()
                    .orElseThrow();

            lagerortChoiceBox.setValue(lagerort);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void setExistingProduct(ProduktGrenz existingProduct) {
        this.existingProduct = existingProduct;
        if (existingProduct != null) {
            title.setText("Produkt aktualisieren");
            int lid = existingProduct.getLagerort();
            setLagerort(lid);
            int kid = existingProduct.getKategorie();
            setCategory(kid);
            pName.setText(existingProduct.getName());
            pBeschreibung.setText(existingProduct.getBeschreibung());
            pPreis.setText(existingProduct.getNettopreis().toString());
            pStueck.setText(String.valueOf(existingProduct.getStueckzahl()));
            pMwst.setText(String.valueOf(existingProduct.getMwstsatz()));
            pAktiv.setSelected(existingProduct.isAktiv());
        } else {
            title.setText("Produkt erstellen");
            categoryChoiceBox.setValue(null);
            lagerortChoiceBox.setValue(null);
            pName.setText("");
            pBeschreibung.setText("");
            pPreis.setText("");
            pStueck.setText("");
            pMwst.setText("");
            pAktiv.setSelected(false);
        }
    }

    @FXML
    private void submit(ActionEvent e) {
        ProduktGrenz product;
        boolean success;
        boolean isUpdate = existingProduct != null;

        if (isUpdate) {
            product = existingProduct;
        } else {
            product = new ProduktGrenz();
        }

        try {
          if (pName.getText().trim().isEmpty()) {
            throw new Exception("Ungültiger Name.");
          }
          product.setName(pName.getText());
          product.setBeschreibung(pBeschreibung.getText());
          product.setAktiv(pAktiv.isSelected());
          product.setAngelegt(LocalDate.now());
          product.setLagerort(lagerortChoiceBox.getValue().getLgortid());
          product.setNettopreis(new BigDecimal(pPreis.getText()));
          product.setStueckzahl(Integer.parseInt(pStueck.getText()));
          product.setMwstsatz(Integer.parseInt(pMwst.getText()));
          product.setKategorie(categoryChoiceBox.getValue().getKatid());
        } catch (Exception exc) {
          Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
          notificationAlert.setTitle("Ungültige Eingaben");
          notificationAlert.setHeaderText(null);
          notificationAlert.setContentText("Bitte überprüfe deine Eingaben.");
          notificationAlert.showAndWait();
          return;
        }

        if (isUpdate) {
            success = produktController.getProduktVerwaltung().produktBearbeiten(product);
        } else {
            success = produktController.getProduktVerwaltung().produktAnlegen(product);
        }

        if (success) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle((isUpdate ? "Aktualisierung" : "Erstellung") + " erfolgreich");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Das Produkt " + product.getName() + " wurde erfolgreich " + (isUpdate ? "aktualisiert" : "erstellt") + ".");
            notificationAlert.showAndWait();
            close(e);
            produktController.ladeProdukte();
        } else {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle((isUpdate ? "Aktualisierung" : "Erstellung") + " fehlgeschlagen");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Das " + (isUpdate ? "Aktualisieren" : "Erstellen") + " des Produktes " + product.getName() + " ist fehlgeschlagen.");
            notificationAlert.showAndWait();
        }
    }

    @FXML
    private void close(ActionEvent actionEvent) {
        Node source = (Node)actionEvent.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}

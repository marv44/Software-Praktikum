package de.thkoeln.swp.wawi.admingui.controllers;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.NoSuchElementException;

public class KategorieDialogController {

    private KategorienController kategorienController;
    private KategorieGrenz existingCategory;
    @FXML
    private Text title;
    @FXML
    private ChoiceBox<KategorieGrenz> parentCategoryChoiceBox;
    @FXML
    private TextField name;
    @FXML
    private TextArea description;

    public void setKategorienController(KategorienController kategorienController) {
        this.kategorienController = kategorienController;
        initKategorieChoiceBox();
    }

    public void initKategorieChoiceBox() {
        if (parentCategoryChoiceBox != null && kategorienController != null && kategorienController.getKategorien() != null) {
            parentCategoryChoiceBox.getItems().clear();
            KategorieGrenz dummyKategorie = new KategorieGrenz(0, 0, "Keine Oberkategorie", "");
            parentCategoryChoiceBox.getItems().add(dummyKategorie);
            parentCategoryChoiceBox.getItems().addAll(kategorienController.getKategorien());
            parentCategoryChoiceBox.setValue(dummyKategorie);
        }
    }

    public void setExistierendeKategorie(KategorieGrenz existingCategory) {
        this.existingCategory = existingCategory;
        if (existingCategory != null) {
            title.setText("Kategorie aktualisieren");
            setParentKategorie(existingCategory.getParentkatid());
            name.setText(existingCategory.getName());
            description.setText(existingCategory.getBeschreibung());
        } else {
            title.setText("Kategorie erstellen");
            parentCategoryChoiceBox.setValue(null);
            name.setText("");
            description.setText("");
        }
    }

    public void setParentKategorie(int parentkatid) {
        if (parentCategoryChoiceBox == null) {
            return;
        }

        try {
            KategorieGrenz parentCategory = parentCategoryChoiceBox
                    .getItems()
                    .stream()
                    .filter(k -> k.getKatid() == parentkatid)
                    .findFirst()
                    .orElseThrow();

            parentCategoryChoiceBox.setValue(parentCategory);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void einreichen(ActionEvent e) {
        KategorieGrenz category;
        boolean success;
        boolean isUpdate = existingCategory != null;

        if (isUpdate) {
            category = existingCategory;
        } else {
            category = new KategorieGrenz();
        }

        try {
            if (name.getText().trim().isEmpty()) {
                throw new Exception("Ungültiger Name.");
            }
            category.setName(name.getText());
            category.setBeschreibung(description.getText());
            category.setParentkatid(parentCategoryChoiceBox.getValue().getKatid());
        } catch (Exception exc) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle("Ungültige Eingaben");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Bitte überprüfe deine Eingaben.");
            notificationAlert.showAndWait();
            return;
        }

        if (isUpdate) {
            success = kategorienController.getKategorieVerwaltung().kategorieBearbeiten(category);
        } else {
            success = kategorienController.getKategorieVerwaltung().kategorieAnlegen(category);
        }

        if (success) {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle((isUpdate ? "Aktualisierung" : "Erstellung") + " erfolgreich");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Die Kategorie " + category.getName() + " wurde erfolgreich " + (isUpdate ? "aktualisiert" : "erstellt") + ".");
            notificationAlert.showAndWait();
            schliessen(e);
            kategorienController.loadCategories();
        } else {
            Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
            notificationAlert.setTitle((isUpdate ? "Aktualisierung" : "Erstellung") + " fehlgeschlagen");
            notificationAlert.setHeaderText(null);
            notificationAlert.setContentText("Das " + (isUpdate ? "Aktualisieren" : "Erstellen") + " der Kategorie " + category.getName() + " ist fehlgeschlagen.");
            notificationAlert.showAndWait();
        }
    }

    @FXML
    private void schliessen(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}

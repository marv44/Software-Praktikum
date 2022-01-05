package de.thkoeln.swp.wawi.admingui.controllers;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.impl.IKategorieVerwaltungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.services.IKategorieVerwaltung;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class KategorienController implements Initializable {

  private IKategorieVerwaltung kategorieVerwaltung;
  private List<KategorieGrenz> kategorien;
  private KategorieGrenz selectedCategory;

  @FXML private TreeView<KategorieGrenz> categoryList;
  @FXML private Text categoryName;
  @FXML private Text categoryDescription;
  @FXML private AnchorPane anchorPane;
  @FXML private ToolBar buttonBar;


  public KategorienController() {
    kategorieVerwaltung = new IKategorieVerwaltungImpl();
  }

  public List<KategorieGrenz> getKategorien() {
    return kategorien;
  }

  public IKategorieVerwaltung getKategorieVerwaltung() {
    return kategorieVerwaltung;
  }

  @FXML
  private void backToHome(Event e) {
    try {
      FXMLLoader viewLoader = new FXMLLoader();
      viewLoader.setLocation(getClass().getResource("/Hauptmenue.fxml"));
      Parent content = viewLoader.load();

      Scene currentScene = ((Button)e.getSource()).getScene();
      Stage stage = (Stage)currentScene.getWindow();
      stage.setTitle("Admin GUI - Hauptmenü");
      stage.setScene(new Scene(content, 960, 540));
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    categoryList
      .getSelectionModel()
      .selectedItemProperty()
      .addListener(((observable, oldValue, newValue) -> onCategorySelected(newValue)));

    loadCategories();
  }

  private void onCategorySelected(TreeItem<KategorieGrenz> treeItem) {
    if (treeItem == null) {
      selectedCategory = null;
      categoryName.setText("");
      categoryDescription.setText("");
      anchorPane.setPrefHeight(75);
      buttonBar.setVisible(false);
    } else {
      selectedCategory = treeItem.getValue();
      categoryName.setText(selectedCategory.getName());
      categoryDescription.setText(selectedCategory.getBeschreibung());
      anchorPane.setPrefHeight(
        75 + categoryDescription.getLayoutBounds().getHeight()
      );
      buttonBar.setVisible(true);
    }
  }

  private void addCategoriesToTree(TreeItem<KategorieGrenz> root, Integer parentkatid) {
    root.getChildren().clear();

    kategorien
      .stream()
      .filter(k -> {
        if (parentkatid == null) {
          // Oberkategorien bekommen. Oberkategorien haben entweder parentkatid = 0 oder parentkatid = katid.
          return k.getParentkatid() == 0 || k.getParentkatid() == k.getKatid();
        } else {
          return k.getParentkatid() == parentkatid && k.getKatid() != parentkatid;
        }
      })
      .sorted(Comparator.comparing(KategorieGrenz::getName))
      .forEach(k -> {
        TreeItem<KategorieGrenz> treeItem = new TreeItem(k);
        root.getChildren().add(treeItem);
        if (k.getKatid() != parentkatid) {
          addCategoriesToTree(treeItem, k.getKatid());
          treeItem.setExpanded(true);
        }
      });
  }

  @FXML
  public void loadCategories() {
    // Clear currently rendered categories.
    onCategorySelected(null);
    TreeItem<KategorieGrenz> currentRoot = categoryList.getRoot();
    if (currentRoot != null) {
      currentRoot.getChildren().clear();
    }

    kategorien = kategorieVerwaltung.getKategorieListe();

    // Create an invisible dummy root element which is required for a TreeView.
    KategorieGrenz dummyRootCategory = new KategorieGrenz(0, 0, "Kategorien", "");
    TreeItem<KategorieGrenz> root = new TreeItem<>(dummyRootCategory);
    root.setExpanded(true);
    categoryList.setRoot(root);
    categoryList.setShowRoot(false);

    addCategoriesToTree(root, null);
  }

  private void openCreateUpdateDialog(KategorieGrenz existingKategorie, int parentKatid) {
    try {
      FXMLLoader viewLoader = new FXMLLoader();
      viewLoader.setLocation(getClass().getResource("/KategorieDialog.fxml"));
      Parent content = viewLoader.load();
      KategorieDialogController dialogController = viewLoader.getController();
      dialogController.setKategorienController(this);
      dialogController.setExistierendeKategorie(existingKategorie);
      dialogController.setParentKategorie(parentKatid);

      Scene scene = new Scene(content, 600, 400);
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(scene);
      stage.showAndWait();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void createCategory(ActionEvent actionEvent) {
    openCreateUpdateDialog(null, 0);
  }

  public void createSubCategory(ActionEvent actionEvent) {
    if (selectedCategory == null) {
      return;
    }

    openCreateUpdateDialog(null, selectedCategory.getKatid());
  }

  public void editCategory(ActionEvent actionEvent) {
    if (selectedCategory == null) {
      return;
    }

    openCreateUpdateDialog(selectedCategory, selectedCategory.getParentkatid());
  }

  public void deleteCategory(ActionEvent actionEvent) {
    // Save the category that should be deleted. The selectedCategory variable might
    // get changed while the alert is open.
    KategorieGrenz categoryToBeDeleted = selectedCategory;

    if (categoryToBeDeleted == null) {
      return;
    }

    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Kategorie löschen");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
      "Du bist im Begriff die Kategorie " + categoryToBeDeleted.getName() + " zu löschen.\n" +
      "Das funktioniert nur, wenn keine Produkte mehr der Kategorie zugewiesen sind.\n" +
      "Unterkategorien werden in die Oberkategorie übertragen." +
      "\nBist du dir sicher?"
    );
    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.get() == ButtonType.OK) {
      boolean success = kategorieVerwaltung.kategorieLoeschen(categoryToBeDeleted.getKatid());

      if (success) {
        Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle("Löschen erfolgreich");
        notificationAlert.setHeaderText(null);
        notificationAlert.setContentText("Die Kategorie " + categoryToBeDeleted.getName() + " wurde erfolgreich gelöscht.");
        notificationAlert.showAndWait();
        loadCategories();
      } else {
        Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle("Löschen fehlgeschlagen");
        notificationAlert.setHeaderText(null);
        notificationAlert.setContentText("Das Löschen der Kategorie " + categoryToBeDeleted.getName() + " ist fehlgeschlagen. Existieren eventuell noch Produkte in dieser Kategorie?");
        notificationAlert.showAndWait();
      }
    }
  }

}

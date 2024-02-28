package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import entities.Avis;
import entities.enums.TypeAvis;
import javafx.stage.Stage;
import services.AvisCrud;

import java.io.IOException;

public class ModifierAvisController {

    @FXML
    private ChoiceBox<TypeAvis> ratingChoiceBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button submitRatingButton;

    @FXML
    private Button returntolist;
    private AvisCrud avisCrud = new AvisCrud();
    private Avis avisToModify;

    @FXML
    private void initialize() {
        ObservableList<TypeAvis> ratingList = FXCollections.observableArrayList(TypeAvis.values());
        ratingChoiceBox.setItems(ratingList);
        ratingChoiceBox.setValue(TypeAvis.MEDIOCRE);
        System.out.println("ratingChoiceBox: " + ratingChoiceBox);
    }

    public void setAvisToModify(Avis avis) {
        this.avisToModify = avis;
        ratingChoiceBox.setValue(avis.getType());
        descriptionTextArea.setText(avis.getDescription());
    }

    @FXML
    void modifierAvis() {
        System.out.println("Modifying Avis...");
        TypeAvis typeAvis = ratingChoiceBox.getValue();
        String description = descriptionTextArea.getText();
        avisToModify.setType(typeAvis);
        avisToModify.setDescription(description);
        avisCrud.modifierAvis(avisToModify);
        showAlert("Avis Modified", "Avis has been successfully modified.");
        redirectToAfficherAvis();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
@FXML
    private void redirectToAfficherAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAvis.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returntolist.getScene().getWindow();
            stage.setScene(new Scene(root));
            ModifierAvisController modifierEventController = loader.getController();
            modifierEventController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import entities.Avis;
import entities.enums.TypeAvis;

public class AvisController {

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> ratingChoiceBox;

    @FXML
    private Button submitRatingButton;

    @FXML
    void initialize() {
        ratingChoiceBox.getItems().addAll("MEDIOCRE", "PASSABLE", "MOYEN", "BIEN", "EXCELLENT");
    }

    @FXML
    void ajouterAvis(ActionEvent event) {
        String description = descriptionTextArea.getText();
        String selectedRating = ratingChoiceBox.getValue();

        Avis avis = new Avis();
        avis.setDescription(description);
        avis.setType(TypeAvis.valueOf(selectedRating));


        showAlert(Alert.AlertType.INFORMATION, "Rating Submitted", "Your rating has been submitted successfully!");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

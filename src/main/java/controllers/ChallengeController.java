package controllers;

import entities.Challenge;
import entities.enums.TypeDifficulty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.ChallengeCrud;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChallengeController {

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    @FXML
    private Button submitButton;

    private static final List<String> motsInterdits = Arrays.asList("mot1", "mot2", "mot3");

    @FXML
    void initialize() {
        difficultyChoiceBox.getItems().addAll("SIMPLE", "MOYEN", "DIFFICILE");
    }

    @FXML
    void ajouterChallenge(ActionEvent challenge1) {
        String description = descriptionTextArea.getText();
        String selectedDifficulty = difficultyChoiceBox.getValue();

        if (contientMotsInterdits(description)) {
            showAlert(Alert.AlertType.WARNING, "Challenge non ajouté", "La description contient des mots interdits.");
            return;
        }

        Challenge challenge = new Challenge(0, TypeDifficulty.valueOf(selectedDifficulty), description);
        ChallengeCrud challengeCrud = new ChallengeCrud();
        challengeCrud.ajouterChallenge(challenge);

        showAlert(Alert.AlertType.INFORMATION, "Challenge Added", "The challenge has been added successfully!");
        redirectToAfficherChallenge();
    }

    private boolean contientMotsInterdits(String description) {
        for (String mot : motsInterdits) {
            if (description.toLowerCase().contains(mot)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void redirectToAfficherChallenge() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherChallenge.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            AfficherChallengeController afficherChallengeController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

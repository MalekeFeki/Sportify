package controllers;

import entities.Challenge;
import entities.enums.TypeDifficulty;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.ChallengeCrud;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ModifierChallengeController {

    @FXML
    private ChoiceBox<TypeDifficulty> difficultyChoiceBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea challengeNameTextArea; // Added for the new attribute

    @FXML
    private Button submitModificationButton;

    @FXML
    private Button returnToListButton;

    private ChallengeCrud challengeCrud = new ChallengeCrud();
    private Challenge challengeToModify;
    private static final List<String> motsInterdits = Arrays.asList("mot1", "mot2", "mot3");

    @FXML
    private void initialize() {
        ObservableList<TypeDifficulty> difficultyList = FXCollections.observableArrayList(TypeDifficulty.values());
        difficultyChoiceBox.setItems(difficultyList);
        difficultyChoiceBox.setValue(TypeDifficulty.SIMPLE);
    }

    public void setChallengeToModify(Challenge challenge) {
        this.challengeToModify = challenge;
        difficultyChoiceBox.setValue(challenge.getDifficulty());
        descriptionTextArea.setText(challenge.getDescription());
        challengeNameTextArea.setText(challenge.getNom()); // Set the new attribute
    }

    @FXML
    void modifierChallenge() {
        System.out.println("Modifying Challenge...");

        TypeDifficulty typeDifficulty = difficultyChoiceBox.getValue();
        String description = descriptionTextArea.getText().trim();
        String name = challengeNameTextArea.getText().trim(); // Get the new attribute

        if (description.isEmpty() || typeDifficulty == null || name.isEmpty()) {
            showAlert("Challenge non modifié", "Le nom, la description et la difficulté ne peuvent pas être vides.");
            return;
        }

        if (contientMotsInterdits(name) || contientMotsInterdits(description)) {
            showAlert("Challenge non modifié", "Le nom ou la description contient des mots interdits.");
            return;
        }

        challengeToModify.setDifficulty(typeDifficulty);
        challengeToModify.setDescription(description);
        challengeToModify.setNom(name); // Set the new attribute
        challengeCrud.modifierChallenge(challengeToModify);
        showAlert("Challenge Modified", "Challenge has been successfully modified.");
        redirectToAfficherChallenge();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
            Stage stage = (Stage) descriptionTextArea.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean contientMotsInterdits(String description) {
        for (String mot : motsInterdits) {
            if (description.toLowerCase().contains(mot)) {
                return true;
            }
        }
        return false;
    }
}

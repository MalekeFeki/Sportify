package controllers;

import entities.Challenge;
import entities.enums.TypeDifficulty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ChallengeCrud;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherChallengeController  {
    private ChallengeCrud challengeCrud;
    @FXML
    private TableView<Challenge> challengeTableView;

    @FXML
    private ObservableList<Challenge> observableChallenges = FXCollections.observableArrayList();

    private void loadChallenges() {
        observableChallenges.clear();
        List<Challenge> challenges = challengeCrud.afficherChallenges();
        System.out.println(challenges);
        observableChallenges.addAll(challenges);
        challengeTableView.setItems(observableChallenges);
        challengeTableView.setItems(observableChallenges);
    }

    public AfficherChallengeController() {
        this.challengeCrud = new ChallengeCrud();
    }

    public void ajouterChallenge(TypeDifficulty difficulty, String description) {
        Challenge challenge = new Challenge(0, difficulty, description);
        challengeCrud.ajouterChallenge(challenge);
    }

    public void afficherChallenges() {
        List<Challenge> challengeList = challengeCrud.afficherChallenges();
        if (challengeList.isEmpty()) {
            System.out.println("Aucun challenge disponible");
        } else {
            for (Challenge challenge : challengeList) {
                System.out.println(challenge.toString());
            }
        }
    }

    public void modifierChallenge(int id, TypeDifficulty difficulty, String description) {
        Challenge challenge = new Challenge(id, difficulty, description);
        challengeCrud.modifierChallenge(challenge);
    }

    public void supprimerChallenge(int id) {
        challengeCrud.supprimerChallenge(id);
    }

    @FXML
    public void initialize() {
        TableColumn<Challenge, Integer> idC = new TableColumn<>("idC");
        TableColumn<Challenge, String> difficulty = new TableColumn<>("difficulty");
        TableColumn<Challenge, String> description = new TableColumn<>("description");
        TableColumn<Challenge, Void> optionsColumn = new TableColumn<>("options");

        idC.setCellValueFactory(new PropertyValueFactory<>("idC"));
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        optionsColumn.setCellFactory(param -> new TableCell<Challenge, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");

            {
                deleteButton.setOnAction(challenge -> {
                    Challenge challengeToDelete = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Challenge");
                    alert.setContentText("Voulez-vous vraiment supprimer le challenge " + challengeToDelete.getIdC() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        challengeCrud.supprimerChallenge(challengeToDelete.getIdC());
                        loadChallenges();
                        showAlert("Challenge Deleted", "Challenge has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Challenge challengeToModify = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Challenge");
                    alert.setContentText("Voulez-vous vraiment modifier le challenge " + challengeToModify.getIdC() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                       redirectToModifierChallenge(challengeToModify);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

        challengeTableView.getColumns().addAll(idC, difficulty, description);
        challengeTableView.getColumns().add(optionsColumn);
        loadChallenges();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToModifierChallenge(Challenge challengeToModify) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierChallenge.fxml"));
            Parent root = loader.load();
            ModifierChallengeController modifierChallengeController = loader.getController();
            modifierChallengeController.setChallengeToModify(challengeToModify);
            Stage stage = (Stage) challengeTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

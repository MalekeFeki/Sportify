package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SalleProfilPropController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private TableView<Salle> gymsTableView;

    @FXML
    private URL location;

    @FXML
    private Label adresseLabel;
    @FXML
    private Button modifierButton1;

    @FXML
    private Button ajouterButton1;

    @FXML
    private Label nomLabel;

    @FXML
    private Label regionLabel;

    @FXML
    private Button retourButton;
    @FXML
    private Button shareOnTwitterButton;
    @FXML
    private WebView webView;


    @FXML
    void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("about:blank"); // Load a blank page initially

        // Other initialization logic...

        // Initialize social media sharing button
        shareOnTwitterButton.setOnAction(event -> shareOnTwitter());

    }



    public void addSeance(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeanceAjout.fxml"));
        try {
            Parent root = loader.load();

            // Set up the stage
            Stage stage = new Stage();
            stage.setTitle("Add Seance Page");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (optional)
            Stage currentStage = (Stage) ajouterButton1.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void handleUpdateButtonAction() {
        Salle salle = gymsTableView.getSelectionModel().getSelectedItem();
        if (salle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une salle à modifier.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment modifier cette salle ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleModifier.fxml"));
            try {
                Parent root = loader.load();
                // Set the controller
                SalleModifController controller = loader.getController();
                controller.setSalle(salle);
                // Set up the stage
                Stage stage = new Stage();
                stage.setTitle("Modif Page");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current stage (optional)
                Stage currentStage = (Stage) modifierButton1.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }

    private void shareOnTwitter() {
        WebEngine webEngine = webView.getEngine();

        // Replace the following URL and text with your salle information
        String tweetText = "Check out this amazing salle! #SalleInfo";
        String tweetURL = "https://example.com/salle-info";

        // Open Twitter sharing dialog
        webEngine.executeScript("window.open('https://twitter.com/intent/tweet?text=" + tweetText + "&url=" + tweetURL + "','_blank');");
    }
}

package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import entities.Salle;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.SalleCrud;
import services.UtilisateurCrud;

public class SalleAjoutController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField adresseTextField;

    @FXML
    private Button annulerButton;

    @FXML
    private CheckBox climatisationCheckBox;

    @FXML
    private Button enregistrerButton;

    @FXML
    private TextField nomTextField;

    @FXML
    private CheckBox nutritionnisteCheckBox;

    @FXML
    private CheckBox parkingCheckBox;

    @FXML
    private TextField regionTextField;

    @FXML
    private CheckBox wifiCheckBox;

    // Liste des régions tunisiennes valides
    private static final List<String> REGIONS_TUNISIENNES = Arrays.asList(
            "Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
            "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
    );

    @FXML
    void initialize() {
    }

    @FXML
    private void handleAnnulerButton() {
        // Add code here to handle the cancel button action
        // For example, close the window or clear the form fields
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveSalle(ActionEvent event) {
        // Validate input fields
        if (isValidInput()) {
            // Input is valid, proceed to save the salle in the BD

            String region = regionTextField.getText().trim();
            if (!isValidRegion(region)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir une région tunisienne valide.", ButtonType.OK);
                alert.show();
                return;
            }

            Set<String> options = new HashSet<>();

            if (wifiCheckBox.isSelected()) {
                options.add("wifi");
            }

            if (parkingCheckBox.isSelected()) {
                options.add("parking");
            }

            if (nutritionnisteCheckBox.isSelected()) {
                options.add("nutritionniste");
            }

            if (climatisationCheckBox.isSelected()) {
                options.add("climatisation");
            }
            Salle s = new Salle(nomTextField.getText(), adresseTextField.getText(), regionTextField.getText(), options);
            SalleCrud sc = new SalleCrud();
            sc.ajouterSalle(s);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salle ajouté", ButtonType.OK);
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleListe.fxml"));
            try {
                Parent root = loader.load();
                SalleListeController luc = loader.getController();

                nomTextField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidInput() {
        if (nomTextField.getText().isEmpty() || adresseTextField.getText().isEmpty() || regionTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return false;
        }

        // You can add more specific validation criteria if needed

        return true;
    }



    // Méthode pour vérifier si la région est valide
    private boolean isValidRegion(String region) {
        return REGIONS_TUNISIENNES.contains(region);
    }



}

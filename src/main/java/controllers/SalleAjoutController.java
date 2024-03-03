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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.SalleCrud;
import java.io.File;
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
    private ComboBox<String> regionComboBox;

    @FXML
    private CheckBox wifiCheckBox;
    private File selectedImageFile;
    @FXML
    private ImageView imageView;
    @FXML
    private Button selectImageButton;

    // Liste des régions tunisiennes valides
    private static final List<String> REGIONS_TUNISIENNES = Arrays.asList(
            "Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
            "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
    );

    @FXML
    void initialize() {
        // Initialize the ComboBox with the predefined values
        regionComboBox.getItems().addAll("Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
                "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan");
    }

    @FXML
    private void handleAnnulerButton() {

        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveSalle(ActionEvent event) {
        // Validate input fields
        if (isValidInput()) {
            String region = regionComboBox.getValue();
//            if (!isValidRegion(region)) {
//                showAlert("Erreur de validation", "Veuillez saisir une région tunisienne valide.", Alert.AlertType.ERROR);
//                return;
//            }

            Set<String> options = new HashSet<>();
            if (wifiCheckBox.isSelected()) options.add("wifi");
            if (parkingCheckBox.isSelected()) options.add("parking");
            if (nutritionnisteCheckBox.isSelected()) options.add("nutritionniste");
            if (climatisationCheckBox.isSelected()) options.add("climatisation");

            Salle s = new Salle(nomTextField.getText(), adresseTextField.getText(), region, options);
            SalleCrud sc = new SalleCrud();

            // Try saving la salle
            try {
                sc.ajouterSalle(s);
                showAlert("Confirmation", "Salle ajoutée avec succès.", Alert.AlertType.INFORMATION);
                loadSalleListeFXML();
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la salle.", Alert.AlertType.ERROR);
                e.printStackTrace(); // Consider logging the exception for further analysis
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSalleListeFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleListe.fxml"));
            Parent root = loader.load();
            SalleListeController luc = loader.getController();
            nomTextField.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page suivante.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // charger l'image dans imageView
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(selectedImage);

        }
    }

    private boolean isValidInput() {
        if (nomTextField.getText().isEmpty() || adresseTextField.getText().isEmpty() || regionComboBox.getValue().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return false;
        }



        return true;
    }



    // Méthode pour vérifier si la région est valide
//    private boolean isValidRegion(String region) {
//        return REGIONS_TUNISIENNES.contains(region);
//    }



}

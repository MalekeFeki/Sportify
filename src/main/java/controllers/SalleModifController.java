package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import entities.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.SalleCrud;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class SalleModifController {


    @FXML
    private TextField latTextField;

    @FXML
    private TextField lonTextField;
    @FXML
    private WebView mapView;
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
    private ImageView imageView;

    @FXML
    private TextField nomTextField;

    @FXML
    private CheckBox nutritionnisteCheckBox;

    @FXML
    private CheckBox parkingCheckBox;

    @FXML
    private ComboBox<String> regionComboBox;

    @FXML
    private Button selectImageButton;

    @FXML
    private CheckBox wifiCheckBox;

    private Salle salle; // The Salle object to be modified




    @FXML
    void initialize() {
        // Initialize the ComboBox with the predefined values
        regionComboBox.getItems().addAll("Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
                "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan");
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/leaftletmap.html").toExternalForm());
        webEngine.setOnAlert(event -> {
            System.out.println("WebView alert: " + event.getData());

            // Check for the custom event 'locationUpdated'
            if ("locationUpdated".equals(event.getData())) {
                // Retrieve the location information from the event
                Object latitudeObj = webEngine.executeScript("getSelectedLocation.latitude");
                Object longitudeObj = webEngine.executeScript("getSelectedLocation.longitude");
                Object locationNameObj = webEngine.executeScript("getSelectedLocation.locationName");

                if (latitudeObj instanceof Double && longitudeObj instanceof Double && locationNameObj instanceof String) {
                    Double latitude = (Double) latitudeObj;
                    Double longitude = (Double) longitudeObj;
                    String locationName = (String) locationNameObj;

                    // Update the JavaFX controls (adresseTextField, regionComboBox, etc.)
                    adresseTextField.setText(locationName);
                    lonTextField.setText(longitude.toString());
                    latTextField.setText(latitude.toString());

                    // You can also update other controls or perform additional actions
                }
            }
        });

    }

    @FXML
    private void handleAnnulerButton() {

        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
    public void setSalle(Salle salle) {
        this.salle = salle;

        // Update the UI with the Salle data
        if (salle != null) {
            nomTextField.setText(salle.getNomS());
            adresseTextField.setText(salle.getAdresse());
            regionComboBox.setValue(salle.getRegion());

            // Update CheckBoxes based on the options in the Salle object
            wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
            parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
            nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
            climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));

        }
    }

    @FXML
    void saveSalle(ActionEvent event) {


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

            // Update the existing Salle object with the modified values
            if (salle != null) {
                salle.setNomS(nomTextField.getText());
                salle.setAdresse(adresseTextField.getText());
                salle.setRegion(regionComboBox.getValue());
                System.out.println("Options to be saved: " + options);
                salle.setOptions(options);
            }

            SalleCrud sc = new SalleCrud();
            sc.modifierSalle(salle);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salle modifié", ButtonType.OK);
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


    @FXML
    public void updateLocationButtonClicked() {
        WebEngine webEngine = mapView.getEngine();

        // Get the location from the JavaScript and update the lieuTextField
        Object latitudeObj = webEngine.executeScript("getSelectedLocation().latitude");
        Object longitudeObj = webEngine.executeScript("getSelectedLocation().longitude");
        String locationName = (String) webEngine.executeScript("getSelectedLocation().locationName");

        if (latitudeObj instanceof Double && longitudeObj instanceof Double) {
            Double latitude = (Double) latitudeObj;
            Double longitude = (Double) longitudeObj;

            adresseTextField.setText(locationName);

            // Now, you can use latitude, longitude, and locationName as needed
            System.out.println("Latitude: " + latitude + ", Longitude: " + longitude + ", Location: " + locationName);
            lonTextField.setText(longitude.toString());
            latTextField.setText(latitude.toString());
            // Update your JavaFX controls (e.g., lieuTextField) here
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'emplacement'.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Charger l'imafe dans imageView
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






}

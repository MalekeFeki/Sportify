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

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.controlsfx.control.Notifications;

public class SalleAjoutController {

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
//    private Optional<cityEV> extractCityFromText(String text) {
//        // Convert the text to lowercase for case-insensitive comparison
//        String lowerText = text.toLowerCase();
//
//        // Iterate through all enum values and check if the text contains a city name
//        return Arrays.stream(cityEV.values())
//                .filter(city -> lowerText.contains(city.toString().toLowerCase()))
//                .findFirst();
//    }
//
//    @FXML
//    private void updateCityComboBox() {
//        String lieuText = adresseTextField.getText();
//
//        // Extract the city from the text
//        Optional<cityEV> extractedCity = extractCityFromText(lieuText);
//
//        // Set the extracted city to the ComboBox if found
//        extractedCity.ifPresent(regionComboBox::setValue);
//    }
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



//    @FXML
//    public void updateLocationButtonClicked() {
//        WebEngine webEngine = mapView.getEngine();
//
//        // Get the location from the JavaScript and update the lieuTextField
//        Object latitudeObj = webEngine.executeScript("getSelectedLocation().latitude");
//        Object longitudeObj = webEngine.executeScript("getSelectedLocation().longitude");
//        String locationName = (String) webEngine.executeScript("getSelectedLocation().locationName");
//
//        if (latitudeObj instanceof Double && longitudeObj instanceof Double) {
//            Double latitude = (Double) latitudeObj;
//            Double longitude = (Double) longitudeObj;
//
//            adresseTextField.setText(locationName);
//
//            // Now, you can use latitude, longitude, and locationName as needed
//            System.out.println("Latitude: " + latitude + ", Longitude: " + longitude + ", Location: " + locationName);
//            lonTextField.setText(longitude.toString());
//            latTextField.setText(latitude.toString());
//            // Update your JavaFX controls (e.g., lieuTextField) here
//        } else {
//            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'emplacement'.", Alert.AlertType.ERROR);
//        }
//    }


    private void loadMap() {
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/maptest.html").toExternalForm());

        // JavaScript code to get selected latitude and longitude
        String javascriptCode = "function getSelectedLatitude() {" +
                "    return selectedLatitude;" +
                "}" +
                "function getSelectedLongitude() {" +
                "    return selectedLongitude;" +
                "}" +
                "function getSelectedLocationName() {" +
                "    return selectedLocationName;" +
                "}";

        webEngine.executeScript(javascriptCode);
    }
    public void setLocationOnMap(double latitude, double longitude) {
        String jsCode = String.format("setLocationOnMap(%f, %f);", latitude, longitude);
        mapView.getEngine().executeScript(jsCode);
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
            showNotification();
        }
    }

    private void showNotification() {
        // Create and show a notification
        Notifications.create()
                .title("Salle Ajouté")
                .text("Votre salle à été ajouté avec succés.")
                .darkStyle()  // You can customize the style
                .showInformation();
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
        String nom = nomTextField.getText();
        String adresse = adresseTextField.getText();
        String region = regionComboBox.getValue();

        // Check if any of the required fields is empty
        if (nom.isEmpty() || adresse.isEmpty() || region == null || region.isEmpty()) {
            showAlert("Champs obligatoires", "Veuillez remplir tous les champs obligatoires.", Alert.AlertType.ERROR);
            return false;
        }

        // Additional validation for specific fields (customize as needed)
        if (nom.length() < 3) {
            showAlert("Validation", "Le nom de la salle doit contenir au moins 3 caractères.", Alert.AlertType.ERROR);
            return false;
        }

        // Check if the address is already associated with an existing salle
        SalleCrud salleCrud = new SalleCrud();
        if (salleCrud.isAddressAlreadyUsed(adresse)) {
            showAlert("Validation", "Cette adresse est déjà associée à une salle existante.", Alert.AlertType.ERROR);
            return false;
        }

        // Add more validations as needed...

        return true;
    }






}

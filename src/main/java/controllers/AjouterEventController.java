package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import entities.Evenement;
import entities.enums.GenreEv;
import entities.enums.cityEV;
import entities.enums.typeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.EvenementCrud;
import javafx.scene.control.Button;
public class AjouterEventController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TextField nomEvenementTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private DatePicker dateDebutDatePicker;

    @FXML
    private DatePicker dateFinDatePicker;

    @FXML
    private ComboBox<String> hoursComboBox;

    @FXML
    private ComboBox<String> minutesComboBox;

    @FXML
    private TextField lieuTextField;
    @FXML
    private TextField cityTextField;

    @FXML
    private TextField numTeleTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fbLinkTextField;

    @FXML
    private TextField igLinkTextField;

    @FXML
    private ComboBox<GenreEv> genreEvenementComboBox;

    @FXML
    private ComboBox<typeEvent> typeEventComboBox;
    @FXML
    private ComboBox<cityEV> cityEVComboBox;
    @FXML
    private TextField capaciteTextField;

    @FXML
    private Button uploadButton;

    @FXML
    private ImageView imageView;
    @FXML
    private Button ajouterEventButton;
    @FXML
    private Button returntolist;
    private EvenementCrud evenementCrud = new EvenementCrud();




        @FXML
        private void initialize() {
            ObservableList<String> hoursList = FXCollections.observableArrayList();
            for (int i = 0; i <= 23; i++) {
                String formattedHours = String.format("%02d", i);
                hoursList.add(formattedHours);
            }
            hoursComboBox.setItems(hoursList);

            ObservableList<String> minutesList = FXCollections.observableArrayList();
            for (int i = 0; i <= 59; i += 2) {
                String formattedMinutes = String.format("%02d", i);
                minutesList.add(formattedMinutes);
            }
            minutesComboBox.setItems(minutesList);

            hoursComboBox.setValue("00");
            minutesComboBox.setValue("00");

            ObservableList<typeEvent> typeEventList = FXCollections.observableArrayList(typeEvent.values());
            typeEventComboBox.setItems(typeEventList);

            ObservableList<GenreEv> genreEvenementList = FXCollections.observableArrayList(GenreEv.values());
            genreEvenementComboBox.setItems(genreEvenementList);
            ObservableList<cityEV> cityEVList = FXCollections.observableArrayList(cityEV.values());
            cityEVComboBox.setItems(cityEVList);
            typeEventComboBox.setValue(typeEvent.PublicEvent);
            genreEvenementComboBox.setValue(GenreEv.competition);
            System.out.println("nomEvenementTextField: " + nomEvenementTextField);
        assert hoursComboBox != null : "fx:id=\"hoursComboBox\" was not injected: check your FXML file 'AjouterEvent.fxml'.";
        assert minutesComboBox != null : "fx:id=\"minutesComboBox\" was not injected: check your FXML file 'AjouterEvent.fxml'.";
        }
    String filePath ;
        @FXML
    void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            filePath = selectedFile.getAbsolutePath();
            System.out.println("Selected Image Path: " + filePath);
        } else {
            showAlert("No Image Selected", "Please select an image file.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void ajouterEvent() {

        System.out.println("Submitting Event...");
        if (!validateGeneralInput()) {
            return;
        }


        if (!validateAdditionalInput()) {
            return;
        }

        String nomEvenement = nomEvenementTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate dateDebut = dateDebutDatePicker.getValue();
        LocalDate dateFin = dateFinDatePicker.getValue();
        String heure = hoursComboBox.getValue() + ":" + minutesComboBox.getValue();
        String lieu = lieuTextField.getText();
        cityEV city = cityEVComboBox.getValue();
        String numTele = numTeleTextField.getText();
        String email = emailTextField.getText();
        String fbLink = fbLinkTextField.getText();
        String igLink = igLinkTextField.getText();
        GenreEv genreEvenement = genreEvenementComboBox.getValue();
        typeEvent typeEvenement = typeEventComboBox.getValue();
        int capacite = Integer.parseInt(capaciteTextField.getText());


        Evenement newEvent = new Evenement(nomEvenement, Date.valueOf(dateDebut), Date.valueOf(dateFin), heure,
                description, filePath, lieu,city, numTele, email, fbLink, igLink, genreEvenement,typeEvenement, capacite);
        System.out.println(newEvent);

        evenementCrud.ajouterEvent(newEvent);


        showAlert1("Event Added", "Event has been successfully added to the database.");
        redirectToGestionEvent();
    }

    private void showAlert1(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void redirectToGestionEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returntolist.getScene().getWindow();
            stage.setScene(new Scene(root));
            AjouterEventController ajouterEventController = loader.getController();
            ajouterEventController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean validateGeneralInput() {
        if (nomEvenementTextField.getText().isEmpty() || dateDebutDatePicker.getValue() == null ||
                lieuTextField.getText().isEmpty() || numTeleTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() || fbLinkTextField.getText().isEmpty() ||
                igLinkTextField.getText().isEmpty() || genreEvenementComboBox.getValue() == null ||
                typeEventComboBox.getValue() == null || cityEVComboBox.getValue() == null ||
                capaciteTextField.getText().isEmpty()) {

            showAlert("Input Validation", "Please fill in all required fields.");
            return false;
        }


        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        if (!emailTextField.getText().matches(emailRegex)) {
            showAlert("Input Validation", "Please enter a valid email address.");
            return false;
        }

        return true;
    }
    private boolean validateAdditionalInput() {
        StringBuilder errorMessage = new StringBuilder();


        LocalDate startDate = dateDebutDatePicker.getValue();
        LocalDate endDate = dateFinDatePicker.getValue();
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            errorMessage.append("End date must be after start date.\n");
        }


        if (startDate != null && startDate.isBefore(LocalDate.now())) {
            errorMessage.append("Start date must not be in the past.\n");
        }


        String numTele = numTeleTextField.getText();
        if (!numTele.matches("\\d+")) {
            errorMessage.append("Phone number must contain only numbers.\n");
        }


        String capacite = capaciteTextField.getText();
        if (!capacite.matches("\\d+")) {
            errorMessage.append("Capacity must contain only numbers.\n");
        }


        if (errorMessage.length() > 0) {
            showAlert("Validation Error", errorMessage.toString());
            return false;
        }

        return true;
    }


}


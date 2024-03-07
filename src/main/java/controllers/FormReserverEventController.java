package controllers;

import entities.Evenement;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import entities.EventReservation;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.EventReservationCrud;
import services.UtilisateurCrud;
import tools.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormReserverEventController implements Initializable {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField cinField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField numTeleField;

    private EventReservationCrud eventReservationCrud = new EventReservationCrud();

    private Evenement event1;
    private int reservationId;

    public void setEvent(Evenement event1) {
        this.event1 = event1;
    }



    @FXML
    void handleReserverButton(ActionEvent event) {
        if (!validateGeneralInput()) {
            return;
        }


        if (!validateAdditionalInput()) {
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        int cin = Integer.parseInt(cinField.getText());
        String email = emailField.getText();
        int numTele = Integer.parseInt(numTeleField.getText());


        EventReservation reservation = new EventReservation(event1.getIDevent(), reservationId, nom, prenom, cin, email, numTele);


        eventReservationCrud.ajouterReservation(reservation);


        showAlert("Success", "Reservation successful!", Alert.AlertType.INFORMATION);
        closeForm();
    }

    private boolean validateGeneralInput() {
        if (nomField.getText().isEmpty() ||
                cinField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || numTeleField.getText().isEmpty()) {

            showAlert("Input Validation", "Please fill in all required fields.");
            return false;
        }


        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        if (!emailField.getText().matches(emailRegex)) {
            showAlert("Input Validation", "Please enter a valid email address.");
            return false;
        }

        return true;
    }
    private boolean validateAdditionalInput() {
        StringBuilder errorMessage = new StringBuilder();


        String numTele = numTeleField.getText();
        if (!numTele.matches("\\d+")) {
            errorMessage.append("Phone number must contain only numbers.\n");
        }
        String numcin = cinField.getText();
        if (!numcin.matches("\\d+")) {
            errorMessage.append("Phone number must contain only numbers.\n");
        }


        if (errorMessage.length() > 0) {
            showAlert("Validation Error", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void closeForm() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        returntoEventDetails();
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private FlowPane eventFlowPane;
    @FXML
    private Button returntoevent;
    @FXML
    private void returntoEventDetails() {
        Evenement selectedEvent = event1 ;

        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventINFO.fxml"));
                Parent root = loader.load();

                EventINFOController eventINFOController = loader.getController();
                eventINFOController.setEventDetails(selectedEvent);

                Stage stage = (Stage) returntoevent.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        } else {
            System.out.println("Selected event details not found.");
        }
    }
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud() ;
    private Utilisateur ut = new Utilisateur() ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int iduser = MyConnection.getInstance().getId() ;
        ut=utilisateurCrud.getUtilisateurById(iduser);
        nomField.setText(ut.getNom());
        prenomField.setText(ut.getPrenom());
        emailField.setText(ut.getEmail());
        numTeleField.setText(String.valueOf(ut.getNum_tel()));
        cinField.setText(String.valueOf(ut.getCin()));
        
    }
}

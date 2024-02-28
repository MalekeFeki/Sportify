package controllers;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import entities.EventReservation;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.EventReservationCrud;

import java.io.IOException;
import java.time.LocalDate;

public class FormReserverEventController {

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

    private Evenement event1; // This will be set before showing the form
    private int reservationId; // Auto-incremented in the database

    public void setEvent(Evenement event1) {
        this.event1 = event1;
    }



    @FXML
    void handleReserverButton(ActionEvent event) {
        if (!validateGeneralInput()) {
            return; // Stop processing if general input validation fails
        }

        // Validate additional input constraints
        if (!validateAdditionalInput()) {
            return; // Stop processing if additional input validation fails
        }
        // Retrieve data from the form
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        int cin = Integer.parseInt(cinField.getText());
        String email = emailField.getText();
        int numTele = Integer.parseInt(numTeleField.getText());

        // Create an EventReservation object
        EventReservation reservation = new EventReservation(event1.getIDevent(), reservationId, nom, prenom, cin, email, numTele);

        // Call the CRUD method to add the reservation
        eventReservationCrud.ajouterReservation(reservation);

        // Show appropriate message based on reservation success
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

        // Validate email format
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        if (!emailField.getText().matches(emailRegex)) {
            showAlert("Input Validation", "Please enter a valid email address.");
            return false;
        }

        return true;
    }
    private boolean validateAdditionalInput() {
        StringBuilder errorMessage = new StringBuilder();

        // Check if the phone number contains only numbers
        String numTele = numTeleField.getText();
        if (!numTele.matches("\\d+")) {
            errorMessage.append("Phone number must contain only numbers.\n");
        }
        String numcin = cinField.getText();
        if (!numcin.matches("\\d+")) {
            errorMessage.append("Phone number must contain only numbers.\n");
        }

        // Display error message if any validation failed
        if (errorMessage.length() > 0) {
            showAlert("Validation Error", errorMessage.toString());
            return false;
        }

        return true; // Input is valid
    }
    // Close the reservation form
    private void closeForm() {
        Stage stage = (Stage) nomField.getScene().getWindow();
//        stage.close();
        returntoEventDetails();
    }

    // Display an alert with the given title, content, and alert type
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
        // Fetch the details of the selected event using the eventId
        Evenement selectedEvent = event1 ;

        // Check if the event details are not null
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventINFO.fxml"));
                Parent root = loader.load();

                // Get the controller of the EventINFO.fxml
                EventINFOController eventINFOController = loader.getController();
                eventINFOController.setEventDetails(selectedEvent);

                // Get the current stage
                Stage stage = (Stage) returntoevent.getScene().getWindow();

                // Set the new scene to the current stage
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
}

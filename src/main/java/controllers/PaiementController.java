package controllers;

import entities.Paiement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.PaiementCrud;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaiementController {

    @FXML
    private TextField tfPostalCode;

    @FXML
    private TextField tfcardnumber;

    @FXML
    private TextField tfexpiration;

    @FXML
    private PasswordField tfccv;

    @FXML
    private TextField tfpromocode;

    @FXML
    private Button btn_proceed;

    @FXML
    private Button btn_cancel;

    private PaiementCrud paiementCrud = new PaiementCrud();

    // Setter method for PaiementCrud
    public void setPaiementCrud(PaiementCrud paiementCrud) {
        this.paiementCrud = paiementCrud;
    }

    @FXML
    public void proceedPayment() {
        try {
            // Retrieve values from text fields in the same order as in the FXML file
            String postalCodeStr = tfPostalCode.getText().trim();
            String cardNumber = tfcardnumber.getText().trim();
            String expirationInput = tfexpiration.getText().trim();
            String ccv = tfccv.getText().trim();
            String promocode = tfpromocode.getText().trim();
            // Validate postal code
            if (postalCodeStr.length() != 4 || !postalCodeStr.matches("\\d{4}")) {
                showAlert(AlertType.ERROR, "Error", "Postal Code must be a 4-digit number!");
                return;
            }
            int postalCode = Integer.parseInt(postalCodeStr);

            // Validate card number, expiration, and ccv
            // (You may need to implement further validation logic for card number and expiration)
            if (cardNumber.isEmpty() || expirationInput.isEmpty() || ccv.isEmpty() || promocode.isEmpty()) {
                showAlert(AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            // Validate and parse the expiration date
            LocalDate expiration = null;
            if (!expirationInput.isEmpty()) {
                // Define the expected date format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                // Attempt to parse the expiration date
                try {
                    expiration = YearMonth.parse(expirationInput, formatter).atDay(1);
                } catch (DateTimeParseException e) {
                    showAlert(AlertType.ERROR, "Error", "Invalid date format for expiration date!");
                    return;
                }

            }
            // Validate CCV
            if (ccv.length() != 3 || !ccv.matches("\\d{3}")) {
                showAlert(AlertType.ERROR, "Error", "CCV must be a 3-digit number!");
                return;
            }

            // Validate promocode
            if (promocode.length() != 9) {
                showAlert(AlertType.ERROR, "Error", "PromoCode must be 9 characters long!");
                return;
            }

            // Create a new Paiement object with the retrieved values
            Paiement paiement = new Paiement(cardNumber, ccv, expiration, promocode, postalCode);

            // Check if PaiementCrud is initialized
            if (paiementCrud != null) {
                // Call the create method in PaiementCrud to save the payment information
                paiementCrud.create(paiement);

                // Display a success message
                showAlert(AlertType.INFORMATION, "Success", "Payment saved successfully!");
            } else {
                // If PaiementCrud is not initialized, display an error message
                showAlert(AlertType.ERROR, "Error", "PaiementCrud is not initialized!");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Postal Code must be a valid integer!");
        }
    }

    @FXML
    public void cancelPayment() {
        try {
            // Load the FXML file for the new interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("your_other_fxml_file.fxml"));
            Parent root = loader.load();

            // Get the stage from the current button
            Stage stage = (Stage) btn_cancel.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur while loading the new interface
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package controllers;

import entities.Adhesion;
import entities.Paiement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.PaiementCrud;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    @FXML
    private Button btn_Show ;

    @FXML
    private TextField debutDateLabel;

    @FXML
    private TextField endDateLabel;

    @FXML
    private TextField priceLabel ;

    private PaiementCrud paiementCrud = new PaiementCrud();

    public void initialize() {
        // Set text field event listeners for input validation
        tfPostalCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 4) {
                tfPostalCode.setText(oldValue);
            }
        });

        tfccv.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                tfccv.setText(oldValue);
            }
        });

        tfpromocode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 9) {
                tfpromocode.setText(oldValue);
            }

        });
    }

    public void initData(Adhesion adhesionInfo) {
        // Set labels with data from MembershipData object
        debutDateLabel.setText("Debut Date: " + adhesionInfo.getDateDebut());
        endDateLabel.setText("End Date: " + adhesionInfo.getDateFin());
        priceLabel.setText("Price: $" + String.format("%.2f", adhesionInfo.getPrice()));
    }

    private static String hashStringWithMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            // Pad with leading zeros to ensure the hash has a consistent length
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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
            String hashedCardNumberMd5 = hashStringWithMD5(cardNumber);
            String hashedCvvMd5 = hashStringWithMD5(ccv);
            String dateDebut = debutDateLabel.getText();
            String dateFin = endDateLabel.getText();
            String price = priceLabel.getText().trim();

            // Validate postal code
            if (postalCodeStr.length() > 4) {
                postalCodeStr = postalCodeStr.substring(0, 4); // Take the first four characters only
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
            if (ccv.length() > 3 || !ccv.matches("\\d{1,3}")) {
                ccv = ccv.substring(0, Math.min(ccv.length(), 3)); // Take the first three digits only
            }

            // Validate promocode
            if (promocode.length() > 9) {
                promocode = promocode.substring(0, 9); // Take the first nine characters only
            }

            LocalDate dateDebutAbo = LocalDate.parse(dateDebut);
            LocalDate dateFinAbo =   LocalDate.parse(dateFin);
            double newPriceFormat = Double.parseDouble(price);
            // Create a new Paiement object with the retrieved values
            Paiement paiement = new Paiement(hashedCardNumberMd5, hashedCvvMd5, expiration, promocode, postalCode,dateDebutAbo,dateFinAbo,newPriceFormat);


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
        // Get the stage from the current button
        Stage stage = (Stage) btn_cancel.getScene().getWindow();

        // Close the stage
        stage.close();
    }
    public void ShowPayments() {
        try {
            // Load the FXML file for the new interface
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ListePaiements.fxml"));
            Parent root = loader.load();

            // Get the stage from the current button
            Stage stage = (Stage) btn_Show.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // FXML file loading failed
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
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
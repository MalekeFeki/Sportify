package controllers;

import java.lang.String ;
import entities.Adhesion;
import entities.Paiement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.AdhesionCrud;
import services.PaiementCrud;
import tools.MyConnection;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
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
    private Button btn_cancel;

    @FXML
    private Button btn_Show;

    @FXML
    private TextField debutDateLabel;

    @FXML
    private TextField endDateLabel;

    @FXML
    private TextField priceLabel;
    private Runnable onSuccessCallback;
    private final PaiementCrud paiementCrud = new PaiementCrud();

    private final Connection connection = MyConnection.getInstance().getCnx(); // Get the database connection



    public void initialize() {

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

    public void initData(Adhesion adhesionInfo, Runnable onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
        if (adhesionInfo != null) {
            debutDateLabel.setText(String.valueOf(adhesionInfo.getDateDebut()));
            endDateLabel.setText(String.valueOf(adhesionInfo.getDateFin()));
            priceLabel.setText(String.format("%.2f", adhesionInfo.getPrice()));
        } else {

            System.err.println("Adhesion info is null!");
        }
    }

    private static String hashStringWithMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = hashText + "0";
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
            if (!postalCodeStr.matches("\\d+")) {
                showAlert(AlertType.ERROR, "Error", "Postal Code must be a valid integer!");
                return;
            }

            // Validate postal code
            if (postalCodeStr.length() > 4) {
                postalCodeStr = postalCodeStr.substring(0, 4); // Take the first four characters only
            }

            int postalCode = Integer.parseInt(postalCodeStr);

            // Validate card number, expiration, and ccv
            if (cardNumber.isEmpty() || expirationInput.isEmpty() || ccv.isEmpty() || promocode.isEmpty() || postalCodeStr.isEmpty()) {
                showAlert(AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            // Validate and parse the expiration date
            LocalDate expiration = null;

            // Define the expected date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Attempt to parse the expiration date
            try {
                expiration = YearMonth.parse(expirationInput, formatter).atDay(1);
            } catch (DateTimeParseException e) {
                showAlert(AlertType.ERROR, "Error", "Invalid date format for expiration date!");
                return;
            }

            // Validate CCV
            if (ccv.length() > 3 || !ccv.matches("\\d{1,3}")) {
                ccv = ccv.substring(0, Math.min(ccv.length(), 3)); // Take the first three digits only
            }

            // Validate promocode
            if (promocode.length() > 9) {
                promocode = promocode.substring(0, 9); // Take the first nine characters only
            }

            dateDebut = dateDebut.substring(dateDebut.indexOf(":") + 1); // Extracting date after ": "
            dateFin = dateFin.substring(dateFin.indexOf(":") + 1); // Extracting date after ": "
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebutAbo = LocalDate.parse(dateDebut.trim(), dateFormatter);
            LocalDate dateFinAbo = LocalDate.parse(dateFin.trim(), dateFormatter);
            double newPriceFormat = Double.parseDouble(price);
            // Create a new Paiement object with the retrieved values

            Paiement paiement = new Paiement(hashedCardNumberMd5, hashedCvvMd5, expiration, promocode, postalCode, dateDebutAbo, dateFinAbo, newPriceFormat);


            // Check if PaiementCrud is initialized
            // Call the create method in PaiementCrud to save the payment information
            paiementCrud.create(paiement);

            // Display a success message
            showAlert(AlertType.INFORMATION, "Success", "Payment saved successfully!");
            if (onSuccessCallback != null) {
                onSuccessCallback.run(); // Call the onSuccessCallback
            }
            processPaymentSuccess();

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void processPaymentSuccess() {
        // Here you can create the adhesion or perform any other action after a successful payment
        System.out.println("Payment successful! Creating adhesion...");

        // Attempt to create the adhesion
        LocalDate debutDate = LocalDate.parse(debutDateLabel.getText().trim());
        LocalDate endDate = LocalDate.parse(endDateLabel.getText().trim());
        double price = Double.parseDouble(priceLabel.getText().trim());

        Connection connection = MyConnection.getInstance().getCnx();


        Adhesion adhesion = new Adhesion(debutDate, endDate, price);
        AdhesionCrud adhesionCrud = new AdhesionCrud(connection);
        boolean adhesionCreated = adhesionCrud.createAdhesion(adhesion);

        if (adhesionCreated) {
            System.out.println("Adhesion created successfully!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Adhesion created successfully!");

        } else {
            System.err.println("Error: Failed to create adhesion!");
            // Handle the case where adhesion creation failed
            // For example, show an error message to the user
            showAlert("Failed to create adhesion!");
        }
    }


}








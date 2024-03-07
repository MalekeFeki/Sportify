package controllers;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.io.File;
import java.lang.String ;
import entities.Adhesion;
import entities.PDFGenerator;
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
import java.util.HashMap;
import java.util.Map;

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
    private Button btn_Delete;

    @FXML
    private TextField debutDateLabel;

    @FXML
    private TextField endDateLabel;

    @FXML
    private TextField priceLabel;
    private Runnable onSuccessCallback;
    private final PaiementCrud paiementCrud = new PaiementCrud();

    private final Connection connection = MyConnection.getInstance().getCnx();



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

        tfcardnumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 16) {
                tfcardnumber.setText(oldValue);
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


            if (!postalCodeStr.matches("\\d+")) {
                showAlert(AlertType.ERROR, "Error", "Postal Code must be a valid integer!");
                return;
            }

            if (postalCodeStr.length() > 4) {
                postalCodeStr = postalCodeStr.substring(0, 4);
            }

            int postalCode = Integer.parseInt(postalCodeStr);

            if (cardNumber.isEmpty() || expirationInput.isEmpty() || ccv.isEmpty() || promocode.isEmpty() || postalCodeStr.isEmpty()) {
                showAlert(AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            LocalDate expiration = null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                expiration = YearMonth.parse(expirationInput, formatter).atDay(1);
            } catch (DateTimeParseException e) {
                showAlert(AlertType.ERROR, "Error", "Invalid date format for expiration date!");
                return;
            }

            if (ccv.length() > 3 || !ccv.matches("\\d{1,3}")) {
                ccv = ccv.substring(0, Math.min(ccv.length(), 3));
            }

            if (promocode.length() > 9) {
                promocode = promocode.substring(0, 9);
            }

            Map<String, Double> promoCodePercentageMap = new HashMap<>();
            promoCodePercentageMap.put("PulseMany", 0.03);
            promoCodePercentageMap.put("PulseGGWP", 0.06);
            promoCodePercentageMap.put("PulseWins", 0.09);
            promoCodePercentageMap.put("PulseFire", 0.12);
            promoCodePercentageMap.put("PulseGood", 0.15);
            promoCodePercentageMap.put("PulseXmas", 0.18);
            promoCodePercentageMap.put("PulseMoul", 0.21);
            promoCodePercentageMap.put("Pulsechek", 0.24);
            promoCodePercentageMap.put("PulseBale", 0.27);
            promoCodePercentageMap.put("PulseRMDN", 0.30);
            promoCodePercentageMap.put("PulseAide", 0.33);
            promoCodePercentageMap.put("PulsePrem", 0.36);

            if (promoCodePercentageMap.containsKey(promocode)) {
                double percentage = promoCodePercentageMap.get(promocode);
                double discountedPrice = Double.parseDouble(price) * (1 - percentage);
                priceLabel.setText(String.format("%.2f", discountedPrice));
                String promoMessage = String.format("Thank you for using '%s'. The following percentage will be applied to the price of adhesion: %.2f%%", promocode, percentage * 100);
                showAlert(AlertType.INFORMATION, "Promo Code Applied", promoMessage);
            } else {
                showAlert(AlertType.WARNING, "Warning", "Invalid promo code entered. Proceeding without discount.");
                priceLabel.setText(String.format("%.2f", Double.parseDouble(price)));
            }
            dateDebut = dateDebut.substring(dateDebut.indexOf(":") + 1);
            dateFin = dateFin.substring(dateFin.indexOf(":") + 1);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebutAbo = LocalDate.parse(dateDebut.trim(), dateFormatter);
            LocalDate dateFinAbo = LocalDate.parse(dateFin.trim(), dateFormatter);
            double newPriceFormat = Double.parseDouble(price);
            Paiement paiement = new Paiement(hashedCardNumberMd5, hashedCvvMd5, expiration, promocode, postalCode, dateDebutAbo, dateFinAbo, newPriceFormat);



            paiementCrud.create(paiement);

            showAlert(AlertType.INFORMATION, "Success", "Payment saved successfully!");
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
            processPaymentSuccess();

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        Text paymentDateText = new Text("Payment Date:");
        Text paymentDateValue = new Text(LocalDate.now().toString());
        Text amountText = new Text("Amount:");
        Text amountValue = new Text("$" + priceLabel.getText());
        Text cardNumberText = new Text("Card Number:");
        Text cardNumberValue = new Text(tfcardnumber.getText());
        Text expirationDateText = new Text("Expiration Date:");
        Text expirationDateValue = new Text(tfexpiration.getText());
        Text cvcText = new Text("CVC:");
        Text cvcValue = new Text(tfccv.getText());

        gridPane.add(paymentDateText, 0, 0);
        gridPane.add(paymentDateValue, 1, 0);
        gridPane.add(amountText, 0, 1);
        gridPane.add(amountValue, 1, 1);
        gridPane.add(cardNumberText, 0, 2);
        gridPane.add(cardNumberValue, 1, 2);
        gridPane.add(expirationDateText, 0, 3);
        gridPane.add(expirationDateValue, 1, 3);
        gridPane.add(cvcText, 0, 4);
        gridPane.add(cvcValue, 1, 4);

        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop";
        String filePath = desktopPath + File.separator + "payment_info.pdf";

        // Generate the PDF with the GridPane content
        PDFGenerator.generatePDF(gridPane, filePath);

        // Display success message to the user
        showAlert(AlertType.INFORMATION, "Success", "Payment information saved to PDF successfully!");

        // Show an alert with the path where the PDF was saved
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PDF Generated");
        alert.setHeaderText(null);
        alert.setContentText("Payment information saved to: " + filePath);
        alert.showAndWait();

        System.out.println(filePath);

    }
    @FXML
    public void cancelPayment() {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
    private int getExpirationMonth(String expiration) {
        // Extract and parse expiration month
        return Integer.parseInt(expiration.split("/")[0]);
    }

    private int getExpirationYear(String expiration) {
        // Extract and parse expiration year
        return Integer.parseInt(expiration.split("/")[1]);
    }

    public void ShowPayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ListePaiements.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btn_Show.getScene().getWindow();
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
        System.out.println("Payment successful! Creating adhesion...");
        // Attempt to create the adhesion
        LocalDate debutDate = LocalDate.parse(debutDateLabel.getText().trim());
        LocalDate endDate = LocalDate.parse(endDateLabel.getText().trim());
        double price = Double.parseDouble(priceLabel.getText().trim());
        Adhesion adhesion = new Adhesion(debutDate, endDate, price);
        AdhesionCrud adhesionCrud = new AdhesionCrud();
        boolean adhesionCreated = adhesionCrud.createAdhesion(adhesion);

        if (adhesionCreated) {
            System.out.println("Adhesion created successfully!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Adhesion created successfully!");

        } else {
            System.err.println("Error: Failed to create adhesion!");

         // show an error message to the user
            showAlert("Failed to create adhesion!");
        }
    }

    @FXML
    public void deletePayment() {
        try {
            // Retrieve the adhesion information from the UI
            LocalDate debutDate = LocalDate.parse(debutDateLabel.getText().trim());
            LocalDate endDate = LocalDate.parse(endDateLabel.getText().trim());
            double price = Double.parseDouble(priceLabel.getText().trim());

            // Create an Adhesion object
            Adhesion adhesion = new Adhesion(debutDate, endDate, price);

            // Delete the adhesion using AdhesionCrud
            AdhesionCrud adhesionCrud = new AdhesionCrud();
            boolean deleted = adhesionCrud.deleteAdhesionByPaymentInfo(adhesion);

            if (deleted) {
                showAlert(AlertType.INFORMATION, "Success", "Adhesion deleted successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to delete adhesion!");
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to delete adhesion! Invalid date or price format.");
            e.printStackTrace();
        }
    }



}








package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import entities.Adhesion;
import entities.Paiement ;
import javafx.util.Duration;
import services.AdhesionCrud;
import services.PaiementCrud;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdhesionController {
    @FXML
    private Button payerButton = new Button();
    @FXML
    private DatePicker dateDebutPicker = new DatePicker();
    @FXML
    private DatePicker dateFinPicker = new DatePicker();
    @FXML
    private Button calculateButton = new Button();
    @FXML
    private Button afficheradhesion = new Button();
    @FXML
    private Label priceLabel;
    @FXML
    private Label remainingTimeLabel;
    private final double DAILY_PRICE = 0.4;
    private Adhesion adhesion;
    private ScheduledExecutorService scheduler;
    private Paiement paiement;
    private PaiementCrud paiementCrud;
    private AdhesionCrud adhésionCrud;
    private Runnable onSuccessCallback;
    @FXML
    private void calculatePrice() {
        LocalDate debutDate = dateDebutPicker.getValue();
        LocalDate finDate = dateFinPicker.getValue();

        if (debutDate != null && finDate != null && !finDate.isBefore(debutDate)) {
            long days = ChronoUnit.DAYS.between(debutDate, finDate);
            double price = days * DAILY_PRICE;
            priceLabel.setText(String.format("Price: $%.2f", price));
            adhesion = new Adhesion(debutDate, finDate, price);
        } else {
            priceLabel.setText("Invalid Dates");
        }
    }

    private void updateRemainingTime() {
        LocalDate finDate = dateFinPicker.getValue();
        if (finDate != null) {
            LocalDateTime finDateTime = finDate.atStartOfDay(); // Convert LocalDate to LocalDateTime

            // Stop the scheduler if it's running
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }

            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                long remainingSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), finDateTime);
                long days = remainingSeconds / (24 * 60 * 60);
                remainingSeconds %= (24 * 60 * 60);
                long hours = remainingSeconds / (60 * 60);
                remainingSeconds %= (60 * 60);
                long minutes = remainingSeconds / 60;
                long seconds = remainingSeconds % 60;

                // Format the remaining time
                String countdownText = String.format("Remaining Time: %d days, %02d:%02d:%02d", days, hours, minutes, seconds);
                Platform.runLater(() -> remainingTimeLabel.setText(countdownText));
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    @FXML
    public void loadPaiementInterface() {
        if (adhesion != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Paiement.fxml"));
                Parent root = loader.load();
                PaiementController controller = loader.getController();
                controller.initData(adhesion, onSuccessCallback);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception gracefully (e.g., show an error message)
            }
        } else {
            // Calculate the price before loading the payment interface
            calculatePrice();
            // Removed recursive call to loadPaiementInterface()
            if (adhesion != null) {
                loadPaiementInterface(); // Removed recursive call
            } else {
                // Handle the case where adhesion is still null
                System.err.println("Error: Adhesion info is null even after calculating the price!");
                // You can display an error message to the user or take other appropriate actions
            }
        }
    }

    @FXML
    public void initialize() {
        calculateButton.setOnAction(event -> calculatePrice());
        payerButton.setOnAction(event -> loadPaiementInterface());


        dateDebutPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateRemainingTime(); // Update remaining time when dateDebut changes
        });

        dateFinPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateRemainingTime(); // Update remaining time when dateFin changes
        });

        updateRemainingTime(); // Call the method to update remaining time
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateRemainingTime())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showAdhesions() {
        try {
            // Load the FXML file for the new interface
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ListeAdhesion.fxml"));
            Parent root = loader.load();

            // Get the stage from the current button
            Stage stage = (Stage) afficheradhesion.getScene().getWindow();

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

}
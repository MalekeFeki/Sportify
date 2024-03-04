package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import entities.Evenement;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import services.EvenementCrud;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import javafx.scene.web.WebView;

public class EventINFOController implements Initializable {

    @FXML
    private ImageView eventImageView;

    @FXML
    private Text title;

    @FXML
    private Text description;

    @FXML
    private VBox infoVBox;

    @FXML
    private Label emailLabel;

    @FXML
    private Label numTeleLabel;

    @FXML
    private Label CityLabel;

    @FXML
    private Label LieuLabel;

    @FXML
    private Text genreevent;

    @FXML
    private Text typeevent;

    @FXML
    private Button interestButton1;

    public Evenement selectedEvent;

    private EvenementCrud evenementCrud = new EvenementCrud();
    @FXML
    private Button reserveButton;
    @FXML
    private WebView mapView = new WebView();

@FXML
private VBox eventBox ;
    private double eventLatitude;
@FXML
    Text startDateText = new Text();
    @FXML
    Text endDateText = new Text();
    private double eventLongitude;

    public VBox setEventDetails(Evenement event) {
        selectedEvent = event;

        title.setText(event.getNomEv());
        description.setText(event.getDescrptionEv());
        eventLatitude = event.getLat();
        eventLongitude = event.getLon();

        startDateText.setText("Start Date: " + event.getDatedDebutEV().toString());
        endDateText.setText("End Date: " + event.getDatedFinEV().toString());
        endDateText.getStyleClass().add("date-text-moreinfo");  // Add style class to end date text
        startDateText.getStyleClass().add("date-text-moreinfo");  // Add style class to start date text
        eventImageView.setImage(new Image("file:" + event.getPhoto()));

        emailLabel.setText("Email: " + event.getEmail());
        numTeleLabel.setText("Num Tele: " + event.getTele());
        CityLabel.setText("City: " + event.getCity());
        LieuLabel.setText("Lieu: " + event.getLieu());

        title.getStyleClass().add("title");
        description.getStyleClass().add("description");
        emailLabel.getStyleClass().add("label");
        numTeleLabel.getStyleClass().add("label");
        CityLabel.getStyleClass().add("label");
        LieuLabel.getStyleClass().add("label");


        emailLabel.getStyleClass().add("value");
        numTeleLabel.getStyleClass().add("value");
        CityLabel.getStyleClass().add("value");
        LieuLabel.getStyleClass().add("value");


        genreevent.setText("Genre: " + event.getGenreEvenement().toString());
        typeevent.setText("Type: " + event.getTypeEV().toString());
// Disable the reserveButton if the event type is "publicevent"
        if ("publicevent".equalsIgnoreCase(event.getTypeEV().toString())) {
            reserveButton.setDisable(true);
        } else {
            reserveButton.setDisable(false);
        }

        updateInterestButtonText(interestButton1, evenementCrud.getInterestStatus(event.getIDevent()));
        interestButton1.setOnAction(event1 -> {
            int newInterestCount = (interestButton1.getText().equals("Show Interest")) ? 1 : -1;

            evenementCrud.updateInterestStatus(selectedEvent.getIDevent(), newInterestCount);


            updateInterestButtonText(interestButton1, newInterestCount);
        });
        System.out.println(event.getDatedFinEV());
        if ("publicevent".equalsIgnoreCase(event.getTypeEV().toString())) {
            reserveButton.setDisable(true);
        } else {
            reserveButton.setDisable(false);
        }
        Label countdownLabel = new Label();
        updateCountdownLabel(countdownLabel,event.getDatedDebutEV(),event.getDatedFinEV(),event.getHeureEV(),reserveButton);
        countdownLabel.getStyleClass().add("countdown-label");
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event1 -> updateCountdownLabel(countdownLabel, event.getDatedDebutEV(),event.getDatedFinEV(),event.getHeureEV()))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        System.out.println("Timeline started");
        infoVBox.setStyle("-fx-background-color: transparent;");
        // Directly add the countdownLabel to the infoVBox
        infoVBox.getChildren().add(countdownLabel);

        // Existing code...

        updateMapView();
        return infoVBox;


    }

    private void updateMapView() {
        String mapUrl = getClass().getResource("/map.html").toExternalForm();
        mapView.getEngine().load(mapUrl);
        mapView.getEngine().setJavaScriptEnabled(true);

        mapView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Define the initMap function
                mapView.getEngine().executeScript("function initMap(latitude, longitude) {" +
                        "var map = L.map('map').setView([latitude, longitude], 13);" +
                        "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);" +
                        "L.marker([latitude, longitude]).addTo(map);" +
                        "}");

                // Call the initMap function with latitude and longitude
                mapView.getEngine().executeScript("initMap(" + eventLatitude + ", " + eventLongitude + ")");
            }
        });
    }


    private void updateCountdownLabel(Label countdownLabel, Date eventdebutDate, Date eventfinDate, String heureEV,Button reserveButton) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime eventTime = LocalTime.parse(heureEV, formatter);
        LocalDateTime eventDebutDateTime = LocalDateTime.of(eventdebutDate.toLocalDate(), eventTime);
        LocalDateTime eventFinDateTime = LocalDateTime.of(eventfinDate.toLocalDate(), eventTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(eventDebutDateTime)) {
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, eventDebutDateTime);
            updateLabelWithCountdown(countdownLabel, seconds);
        } else if (currentDateTime.isAfter(eventFinDateTime)) {
            countdownLabel.setText("Event has ended.");
            reserveButton.setDisable(true);
        } else {
            countdownLabel.setText("Event is happening now.");
            reserveButton.setDisable(true);
        }
    }
    private void updateCountdownLabel(Label countdownLabel, Date eventdebutDate, Date eventfinDate, String heureEV) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime eventTime = LocalTime.parse(heureEV, formatter);
        LocalDateTime eventDebutDateTime = LocalDateTime.of(eventdebutDate.toLocalDate(), eventTime);
        LocalDateTime eventFinDateTime = LocalDateTime.of(eventfinDate.toLocalDate(), eventTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(eventDebutDateTime)) {
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, eventDebutDateTime);
            updateLabelWithCountdown(countdownLabel, seconds);
        } else if (currentDateTime.isAfter(eventFinDateTime)) {
            countdownLabel.setText("Event has ended.");

        } else {
            countdownLabel.setText("Event is happening now.");

        }
    }

    private void updateLabelWithCountdown(Label countdownLabel, long seconds) {
        long days = seconds / (24 * 60 * 60);
        long hours = (seconds % (24 * 60 * 60)) / 3600;
        long minutes = ((seconds % (24 * 60 * 60)) % 3600) / 60;
        seconds = ((seconds % (24 * 60 * 60)) % 3600) % 60;

        String countdownText = String.format("Time left: %02d days %02d:%02d:%02d", days, hours, minutes, seconds);
        countdownLabel.setText(countdownText);
    }
    private void updateInterestButtonText(Button interestButton, int interestCount) {
        interestButton.setText((interestCount > 0) ? "Remove Interest" : "Show Interest");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        interestButton1.setOnAction(this::handleInterestButtonClick);
        updateInterestButtonText(interestButton1, 0);
        System.out.println(selectedEvent);


    }
    @FXML
    private void handleInterestButtonClick(ActionEvent event) {
        int newInterestCount = (interestButton1.getText().equals("Show Interest")) ? 1 : -1;

        evenementCrud.updateInterestStatus(selectedEvent.getIDevent(), newInterestCount);

        updateInterestButtonText(interestButton1, newInterestCount);
    }
    @FXML
    private Button returntolist;
    @FXML
    private void redirectToAllEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returntolist.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void redirectToReservationForm() {
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormReserverEvent.fxml"));
                Parent root = loader.load();
                FormReserverEventController formController = loader.getController();
                formController.setEvent(selectedEvent);

                Stage currentStage = (Stage) eventImageView.getScene().getWindow();

                currentStage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("selectedEvent is null. Make sure setEventDetails is called before redirectToReservationForm.");
        }
    }


}

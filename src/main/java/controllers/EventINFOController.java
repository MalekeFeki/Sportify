package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import entities.Evenement;
import javafx.stage.Stage;
import services.EvenementCrud;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private Text genreevent; // Add this

    @FXML
    private Text typeevent; // Add this

    @FXML
    private Button interestButton1; // Reference the button in FXML

    public Evenement selectedEvent; // Declare the variable here

    private EvenementCrud evenementCrud = new EvenementCrud();
    @FXML
    private Button reserveButton; // Add this



    public void setEventDetails(Evenement event) {
        // Set event details to the UI components
        selectedEvent = event; // Set the selectedEvent here
        title.setText(event.getNomEv());
        description.setText(event.getDescrptionEv());

        // Set event image
        eventImageView.setImage(new Image("file:" + event.getPhoto()));

        // Set additional details in the VBox labels
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

        // Apply specific styles to labels
        emailLabel.getStyleClass().add("value");
        numTeleLabel.getStyleClass().add("value");
        CityLabel.getStyleClass().add("value");
        LieuLabel.getStyleClass().add("value");

        // Set genre and type
        genreevent.setText("Genre: " + event.getGenreEvenement().toString());
        typeevent.setText("Type: " + event.getTypeEV().toString());

        // Set interest button text and action
        updateInterestButtonText(interestButton1, evenementCrud.getInterestStatus(event.getIDevent()));
        interestButton1.setOnAction(event1 -> {
            int newInterestCount = (interestButton1.getText().equals("Show Interest")) ? 1 : -1;

            evenementCrud.updateInterestStatus(selectedEvent.getIDevent(), newInterestCount);

            // Update the button text
            updateInterestButtonText(interestButton1, newInterestCount);
        });
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

        // Update the button text
        updateInterestButtonText(interestButton1, newInterestCount);
    }
    @FXML
    private Button returntolist;
    @FXML
    private void redirectToAllEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returntolist.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void redirectToReservationForm() {
        // Ensure that selectedEvent is not null before redirecting
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormReserverEvent.fxml"));
                Parent root = loader.load();
                // Get the controller of the FormReserverEvent
                FormReserverEventController formController = loader.getController();
                // Set the event ID in FormReserverEventController
                formController.setEvent(selectedEvent);

                // Get the current stage
                Stage currentStage = (Stage) eventImageView.getScene().getWindow();

                // Update the scene with the new content
                currentStage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("selectedEvent is null. Make sure setEventDetails is called before redirectToReservationForm.");
        }
    }

}

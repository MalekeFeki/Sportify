package controllers;

import entities.Evenement;
import entities.EventReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.EventReservationCrud;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.ResourceBundle;

public class ListOfEventReservation {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<EventReservation> reservationTableView;

    @FXML
    private TableColumn<EventReservation, Integer> eventIdColumn;

    @FXML
    private TableColumn<EventReservation, Integer> reservationIdColumn;

    @FXML
    private TableColumn<EventReservation, String> nomColumn;

    @FXML
    private TableColumn<EventReservation, String> prenomColumn;

    @FXML
    private TableColumn<EventReservation, Integer> cinColumn;

    @FXML
    private TableColumn<EventReservation, String> emailColumn;

    @FXML
    private TableColumn<EventReservation, Integer> numTeleColumn;
    @FXML
    private Text nomevent;
    private EventReservationCrud eventReservationCrud = new EventReservationCrud();
    private int eventId; // Variable to store the eventId

    public void setEventId(Evenement event) {
        TableColumn<EventReservation, Integer> eventIdColumn = new TableColumn<>("Event ID");
        TableColumn<EventReservation, Integer> reservationIdColumn = new TableColumn<>("Reservation ID");
        TableColumn<EventReservation, String> nomColumn = new TableColumn<>("Nom");
        TableColumn<EventReservation, String> prenomColumn = new TableColumn<>("Prenom");
        TableColumn<EventReservation, Integer> cinColumn = new TableColumn<>("CIN");
        TableColumn<EventReservation, String> emailColumn = new TableColumn<>("Email");
        TableColumn<EventReservation, Integer> numTeleColumn = new TableColumn<>("Num Tele");
        System.out.println(eventId);
        // Set column cell values
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        numTeleColumn.setCellValueFactory(new PropertyValueFactory<>("num_tele"));
        reservationTableView.getColumns().addAll(eventIdColumn, reservationIdColumn, nomColumn, prenomColumn, cinColumn, emailColumn, numTeleColumn);
        this.eventId = event.getIDevent();
        nomevent.setText(event.getNomEv());
    }
    @FXML
    void initialize() {
        // Load reservations for the selected event
        loadReservations(eventId);
    }

    private void loadReservations(int eventId) {
        List<EventReservation> reservations = eventReservationCrud.afficherReservation(eventId);
        System.out.println(reservations);
        reservationTableView.getItems().addAll(reservations);
    }
    @FXML
    private Button returntolist;
    @FXML
    private void redirectToGestionEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returntolist.getScene().getWindow();
            stage.setScene(new Scene(root));
            ListOfEventReservation listOfEventReservation = loader.getController();
            listOfEventReservation.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

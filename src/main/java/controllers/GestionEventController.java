package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.EvenementCrud;
import entities.Evenement;

public class GestionEventController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ObservableList<Evenement> observableEvents = FXCollections.observableArrayList();

    private EvenementCrud evenementCrud = new EvenementCrud();
    @FXML
    private TableColumn<Evenement, Integer> idColumn;
    @FXML
    private TableColumn<Evenement, String> nomColumn;
    @FXML
    private TableColumn<Evenement, String> dateDebutColumn;
    @FXML
    private TableColumn<Evenement, String> datefinColumn;
    @FXML
    private TableColumn<Evenement, String> lieuColumn;
    @FXML
    private TableColumn<Evenement, String> GenreColumn;
    @FXML
    private TableColumn<Evenement, String> typeColumn;
    @FXML
    private TableColumn<Evenement, Integer> nombrePersonneInteresseColumn;
    @FXML
    private TableColumn<Evenement, Integer> CapaciteColumn;
    @FXML
    private TableView<Evenement> eventTableView;
    @FXML
    private Button AjouterEvenement;

    @FXML
    private void initialize() {
        TableColumn<Evenement, Integer> idColumn = new TableColumn<>("IDevent");
        TableColumn<Evenement, String> nomColumn = new TableColumn<>("Nom");
        TableColumn<Evenement, String> dateDebutColumn = new TableColumn<>("Date Debut");
        TableColumn<Evenement, String> datefinColumn = new TableColumn<>("Date Fin");
        TableColumn<Evenement, String> lieuColumn = new TableColumn<>("Lieu");
        TableColumn<Evenement, String> GenreColumn = new TableColumn<>("Genre Evenement");
        TableColumn<Evenement, String> typeColumn = new TableColumn<>("Type Evenement");
        TableColumn<Evenement, Integer> nombrePersonneInteresseColumn = new TableColumn<>("Nombre Personne Interesse");
        TableColumn<Evenement, Integer> CapaciteColumn = new TableColumn<>("Capacite");
        TableColumn<Evenement, Void> optionsColumn = new TableColumn<>("options");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("IDevent"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NomEv"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("DatedDebutEV"));
        datefinColumn.setCellValueFactory(new PropertyValueFactory<>("DatedFinEV"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        GenreColumn.setCellValueFactory(new PropertyValueFactory<>("GenreEvenement"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeEV"));
        nombrePersonneInteresseColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePersonneInteresse"));
        CapaciteColumn.setCellValueFactory(new PropertyValueFactory<>("Capacite"));
        optionsColumn.setCellFactory(param -> new TableCell<Evenement, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");
            private final Button reservationButton = new Button("reservation");

            {
                deleteButton.setOnAction(event -> {
                    Evenement eventToDelete = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Event");
                    alert.setContentText("Voulez-vous vraiment supprimer l'événement " + eventToDelete.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        evenementCrud.supprimerEvent(eventToDelete.getIDevent());
                        loadEvents();
                        showAlert("Event Deleted", "Event has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());


                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Event");
                    alert.setContentText("Voulez-vous vraiment modifier l'événement " + eventToModify.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        redirectToModifierEvent(eventToModify);
                    }
                });
                reservationButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());
                    System.out.println(eventToModify);
                    redirectToEventReservations(eventToModify);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton,reservationButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });
// Set preferred column widths
        idColumn.setPrefWidth(70);  // Adjust the value based on your preference
        nomColumn.setPrefWidth(120);  // Adjust the value based on your preference
        dateDebutColumn.setPrefWidth(120);  // Adjust the value based on your preference
        datefinColumn.setPrefWidth(120);  // Adjust the value based on your preference
        lieuColumn.setPrefWidth(100);  // Adjust the value based on your preference
        GenreColumn.setPrefWidth(120);  // Adjust the value based on your preference
        typeColumn.setPrefWidth(120);  // Adjust the value based on your preference
        nombrePersonneInteresseColumn.setPrefWidth(150);  // Adjust the value based on your preference
        CapaciteColumn.setPrefWidth(70);  // Adjust the value based on your preference

        eventTableView.getColumns().addAll(idColumn, nomColumn, dateDebutColumn, datefinColumn, lieuColumn, GenreColumn, typeColumn, nombrePersonneInteresseColumn, CapaciteColumn);
        eventTableView.getColumns().add(optionsColumn);
        loadEvents();
    }


    private void loadEvents() {
        observableEvents.clear();
        List<Evenement> events = evenementCrud.afficherEvent();
        observableEvents.addAll(events);
        eventTableView.setItems(observableEvents);
    }
    @FXML
    private void redirectToEventReservations(Evenement selectedEvent) {
//        Evenement selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedEvent);
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListOfEventReservation.fxml"));
                Parent root = loader.load();
                ListOfEventReservation eventReservationController = loader.getController();
                eventReservationController.setEventId(selectedEvent);
                Stage stage = (Stage) eventTableView.getScene().getWindow();
                stage.setScene(new Scene(root));
                eventReservationController.initialize();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        } else {
            System.err.println("Event not selected.");
        }
    }

    @FXML
    private void redirectToAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AjouterEvenement.getScene().getWindow();
            stage.setScene(new Scene(root));
            GestionEventController gestionEventController = loader.getController();
            gestionEventController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToModifierEvent(Evenement eventToModify) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvent.fxml"));
            Parent root = loader.load();
            ModifierEventController modifierEventController = loader.getController();
            modifierEventController.setEventToModify(eventToModify);

            Stage stage = (Stage) eventTableView.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


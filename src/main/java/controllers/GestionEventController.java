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
        // Set up columns
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
        // Set column values to attribute names
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

            {
                deleteButton.setOnAction(event -> {
                    Evenement eventToDelete = getTableView().getItems().get(getIndex());

                    // Display confirmation alert
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Event");
                    alert.setContentText("Voulez-vous vraiment supprimer l'événement " + eventToDelete.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        evenementCrud.supprimerEvent(eventToDelete.getIDevent());
                        loadEvents(); // Refresh the TableView after deletion
                        showAlert("Event Deleted", "Event has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());

                    // Display confirmation alert
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Event");
                    alert.setContentText("Voulez-vous vraiment modifier l'événement " + eventToModify.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        redirectToModifierEvent(eventToModify);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

// ...



// Add the optionsColumn to the table

        // Add columns to the table
        eventTableView.getColumns().addAll(idColumn, nomColumn, dateDebutColumn, datefinColumn, lieuColumn, GenreColumn, typeColumn, nombrePersonneInteresseColumn, CapaciteColumn);
        eventTableView.getColumns().add(optionsColumn);
        // Load events into the ObservableList and set them to the TableView
        loadEvents();
    }


    private void loadEvents() {
        observableEvents.clear(); // Clear existing items
        List<Evenement> events = evenementCrud.afficherEvent();
        observableEvents.addAll(events);

        // Set the items in the TableView
        eventTableView.setItems(observableEvents);
    }

    @FXML
    private void redirectToAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AjouterEvenement.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            GestionEventController gestionEventController = loader.getController();
            gestionEventController.initialize(); // Call the initialize method if needed
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
            modifierEventController.setEventToModify(eventToModify); // Pass the event to be modified

            // Get the current stage
            Stage stage = (Stage) eventTableView.getScene().getWindow();

            // Set the new scene to the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


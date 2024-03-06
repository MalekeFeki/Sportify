package controllers;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Salle;
import entities.Seance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.SalleCrud;
import services.SeanceCrud;

public class SeanceListeAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button afficherButton;

    @FXML
    private TableColumn<?, ?> dateDebutColumn;

    @FXML
    private TableColumn<?, ?> debutColumn;

    @FXML
    private TableColumn<?, ?> finColumn;

    @FXML
    private TableColumn<?, ?> nomSeanceColumn;

    @FXML
    private TableView<Seance> seancesTableView;

    @FXML
    private Button supprimerButton1;
    private SeanceCrud seanceCrud;

    SeanceCrud seanceservices = new SeanceCrud();

    @FXML
    void initialize() {
        seanceCrud = new SeanceCrud();

        // Set up cell value factories for each column
        nomSeanceColumn.setCellValueFactory(new PropertyValueFactory<>("nomSeance"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateS"));
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));

        // Load seances data into the TableView
        loadSeancesData();
    }

    private void loadSeancesData() {
        List<Seance> seanceList = seanceCrud.afficherSeance();
        ObservableList<Seance> observableSeanceList = FXCollections.observableArrayList(seanceList);
        seancesTableView.setItems(observableSeanceList);
    }

    public void deleteSeance(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cette salle ?");



        Optional<ButtonType> result = alert.showAndWait();
        Seance selectedSeance = seancesTableView.getSelectionModel().getSelectedItem();

        if (result.isPresent() && result.get() == ButtonType.OK && selectedSeance != null) {
            seanceservices.supprimerSeance(selectedSeance.getNomSeance());
            System.out.println("Supprimé du tableview");

            // Mettre à jour l'affichage en supprimant la salle de la TableView
            seancesTableView.getItems().remove(selectedSeance);
        }
    }
}




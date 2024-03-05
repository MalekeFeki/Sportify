package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.SeanceCrud;
import entities.Seance;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SeanceListeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Seance, String> nomSeanceColumn;

    @FXML
    private TableColumn<Seance, String> dateDebutColumn;

    @FXML
    private TableColumn<Seance, String> debutColumn;

    @FXML
    private TableColumn<Seance, String> finColumn;

    @FXML
    private TableView<Seance> seancesTableView;

    @FXML
    private Button afficherButton;

    private SeanceCrud seanceCrud;

    @FXML
    void initialize() {
        seanceCrud = new SeanceCrud();

        // Set up cell value factories for each column
        nomSeanceColumn.setCellValueFactory(new PropertyValueFactory<>("nomSeance"));
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateS"));
        // Load seances data into the TableView
        loadSeancesData();
    }

    private void loadSeancesData() {
        List<Seance> seanceList = seanceCrud.afficherSeance();
        ObservableList<Seance> observableSeanceList = FXCollections.observableArrayList(seanceList);
        seancesTableView.setItems(observableSeanceList);
    }

    // Other methods...

}

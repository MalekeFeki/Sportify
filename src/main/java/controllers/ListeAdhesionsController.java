package controllers;

import entities.Adhesion;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.AdhesionCrud;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeAdhesionsController {

    @FXML
    private TableView<Adhesion> tableViewAdhesion;

    @FXML
    private TableColumn<Adhesion, String> descriptionColumn;

    @FXML
    private TableColumn<Adhesion, String> nameColumn;

    @FXML
    private TableColumn<Adhesion, Double> priceColumn;

    @FXML
    private TableColumn<Adhesion, String> dateDebutColumn;

    @FXML
    private TableColumn<Adhesion, String> dateFinColumn;

    private AdhesionCrud adhesionCrud = new AdhesionCrud();

    @FXML
    public void initialize() {
        // Initialize table columns
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        dateDebutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateDebut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        dateFinColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateFin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // Load adhésions data into the table
        loadAdhesionsData();
    }

    private void loadAdhesionsData() {
        // Retrieve adhésions data from the database
        List<Adhesion> adhesions = (List<Adhesion>) adhesionCrud.getAdhesionById(1);

        // Convert the list to an observable list
        ObservableList<Adhesion> observableAdhesions = FXCollections.observableArrayList(adhesions);

        // Load data into the table view
        tableViewAdhesion.setItems(observableAdhesions);
    }

    private void loadAllAdhesionsData() {
        // Retrieve adhésions data from the database
        List<Adhesion> adhesions = adhesionCrud.getAllAdhesions();

        // Convert the list to an observable list
        ObservableList<Adhesion> observableAdhesions = FXCollections.observableArrayList(adhesions);

        // Load data into the table view
        tableViewAdhesion.setItems(observableAdhesions);
    }
}
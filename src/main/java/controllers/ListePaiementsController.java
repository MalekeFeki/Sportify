package controllers;

import entities.Paiement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.PaiementCrud;

import java.util.List;

public class ListePaiementsController {

    @FXML
    private TableView<Paiement> tableViewPaiement;

    @FXML
    private TableColumn<Paiement, String> numeroCarteBancaire;

    @FXML
    private TableColumn<Paiement, String> ccv;

    @FXML
    private TableColumn<Paiement, String> expirationDate;

    @FXML
    private TableColumn<Paiement, String> datePayment;

    @FXML
    private TableColumn<Paiement, String> hourPayment;

    @FXML
    private TableColumn<Paiement, String> promoCode;

    @FXML
    private TableColumn<Paiement, Integer> postalCode;

    @FXML
    private TableColumn<Paiement, String> dateDebutAbonnement;

    @FXML
    private TableColumn<Paiement, String> dateFinAbonnement;

    private PaiementCrud paiementCrud = new PaiementCrud();

    @FXML
    public void initialize() {
        // Initialize table columns
        numeroCarteBancaire.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroCarteBancaire()));
        ccv.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCcv()));
        expirationDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpirationDate().toString()));
        datePayment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatePayment().toString()));
        hourPayment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHourPayment().toString()));
        promoCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPromoCode()));
        postalCode.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPostalCode()).asObject());
        dateDebutAbonnement.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateDebutAbonnement().toString()));
        dateFinAbonnement.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateFinAbonnement().toString()));


        // Load payments data into the table
        loadPaymentsData();
    }

    private void loadPaymentsData() {
        //Retrieve payments data from the database
      List<Paiement> payments = paiementCrud.read();

        // Convert the list to an observable list
        ObservableList<Paiement> observablePayments = FXCollections.observableArrayList(payments);

        // Load data into the table view
        tableViewPaiement.setItems(observablePayments);
    }
}

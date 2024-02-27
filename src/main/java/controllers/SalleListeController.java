package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.stage.Stage;
import entities.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.SalleCrud;


public class SalleListeController {

    @FXML
    private Button AdminButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Salle, String> adresseColumn;

    @FXML
    private Button afficherButton;

    @FXML
    private TableView<Salle> gymsTableView;

    @FXML
    private Button modifierButton;

    @FXML
    private TableColumn<Salle, String> idColumn;

    @FXML
    private TableColumn<Salle, String> nomColumn;

    @FXML
    private TableColumn<Salle, String> regionColumn;

    @FXML
    private TableColumn<Salle, String> optionsColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button supprimerButton;

    private Hyperlink hyperlink;

    private SalleCrud salleCrud;
    SalleCrud salleservices = new SalleCrud();

    @FXML
    void initialize() {
        AdminButton.setOnAction(event -> {
            try {
                // Chargez la page d'authentification depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleListeAdmin.fxml"));
                Parent root = loader.load();

                // Créez une nouvelle scène et un nouveau stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Affichez la nouvelle scène
                stage.show();

                // Fermez la fenêtre actuelle
                ((Stage) hyperlink.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace(); // Gérez l'exception selon vos besoins
            }
        });

        salleCrud = new SalleCrud();
        // Set up cell value factories for each column

        nomColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("nomS"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("adresse"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("region"));
        optionsColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("options"));

        // Retrieve and print the list of Salle objects from the database
        List<Salle> salleList = salleCrud.afficherSalle();

        // Debug: Print the list of Salle objects
        for (Salle salle : salleList) {
            System.out.println(salle);
        }

        // Convert the list to an ObservableList
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList(salleList);

        // Set the items of the TableView with the ObservableList
        gymsTableView.setItems(salleObservableList);
        loadGymsData();
    }


    private void loadGymsData() {
        // Retrieve gyms from the database
        ObservableList<Salle> gymsList = FXCollections.observableArrayList(salleCrud.getAllSalles());

        // Set the items in the TableView
        gymsTableView.setItems(gymsList);
    }


    private void refreshTableViewData() {
        // Effacez toutes les lignes existantes dans la TableView
        gymsTableView.getItems().clear();
        // Chargez à nouveau toutes les données depuis votre base de données
        List<Salle> allSalles = SalleCrud.getInstance().getAllSalles();
        // Ajoutez les données rechargées dans la TableView
        gymsTableView.getItems().addAll(allSalles);
    }

    public void handleAdminButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/SalleListeAdmin.fxml"));
            Parent rootParent= loader.load();

            Scene rootScene = new Scene(rootParent);

            Stage stage = new Stage();
            stage.setTitle("Salle Liste Admin");
            stage.setScene(rootScene);

            Stage cureentStage= (Stage) AdminButton.getScene().getWindow();
            cureentStage.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Rafraîchir la TableView après la suppression
//            refreshTableViewData();
}




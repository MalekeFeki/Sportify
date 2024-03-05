package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
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
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;



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


    private void refreshTableView() {
        // get data de la BD
        ObservableList<Salle> gymsList = FXCollections.observableArrayList(salleCrud.getAllSalles());

        // Mettre les items dans la table
        gymsTableView.setItems(gymsList);
    }
    SalleCrud salleservices = new SalleCrud();





    @FXML
    void initialize() {
        salleCrud = new SalleCrud();

        nomColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("nomS"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("adresse"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<Salle, String>("region"));
        optionsColumn.setCellValueFactory(cellData -> {
            Set<String> options = cellData.getValue().getOptions();
            String optionsString = options != null ? String.join(", ", options) : "";
            return new SimpleStringProperty(optionsString);
        });

        // Refresh la table
        gymsTableView.refresh();


        List<Salle> salleList = salleCrud.afficherSalle();

        // list to observable list
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList(salleList);


        gymsTableView.setItems(salleObservableList);

//        // Set up auto-completion for the search TextField
//        List<String> salleNames = salleCrud.getAllSalles().stream()
//                .map(Salle::getNomS)
//                .collect(Collectors.toList());
//
//        TextFields.bindAutoCompletion(searchTextField, salleNames);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filtrer la TableView en fonction du texte de recherche
            filterTableView(newValue);


        });
    }

    private void filterTableView(String searchText) {
        // Créer un prédicat pour filtrer les éléments de la TableView
        Predicate<Salle> predicate = salle ->
                salle.getNomS().toLowerCase().contains(searchText.toLowerCase());

        // Créer une liste filtrée à partir de la liste originale des utilisateurs
        List<Salle> filteredList = salleCrud.getAllSalles().stream()
                .filter(predicate)
                .collect(Collectors.toList());

        // Effacer toutes les lignes existantes dans la TableView
        gymsTableView.getItems().clear();

        // Ajouter les éléments filtrés à la TableView
        gymsTableView.getItems().addAll(filteredList);
    }
    private void loadGymsData() {

        ObservableList<Salle> gymsList = FXCollections.observableArrayList(salleCrud.getAllSalles());

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

    @FXML
    private void handleAdminButton(ActionEvent event) {
        // Load the SalleListeAdmin.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleListeAdmin.fxml"));
        try {
            Parent root = loader.load();

            // Set up the stage
            Stage stage = new Stage();
            stage.setTitle("Admin Page");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) AdminButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void handleAfficherButton(ActionEvent event) {
        // LoadSalleListeAdmin.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleProfil.fxml"));
        Salle salle = gymsTableView.getSelectionModel().getSelectedItem();
        try {
            Parent root = loader.load();
            SalleProfilController controller = loader.getController();
            controller.setSalle(salle);

            // Set up the stage
            Stage stage = new Stage();
            stage.setTitle("Salle Profil");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (optional)
            Stage currentStage = (Stage) afficherButton.getScene().getWindow();
            currentStage.close();



        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void openSalleProfile(Salle salle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleProfil.fxml"));
            Parent root = loader.load();
            SalleProfilController salleProfileController = loader.getController();
            salleProfileController.initData(salle);

            gymsTableView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    // Rafraîchir la TableView après la suppression
//            refreshTableViewData();
}




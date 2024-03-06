package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entities.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.SalleCrud;


public class SalleListeAdminController {

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

    private SalleCrud salleCrud;
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

        //
        gymsTableView.refresh();

        //
        List<Salle> salleList = salleCrud.afficherSalle();

        //
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList(salleList);

        //
        gymsTableView.setItems(salleObservableList);

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


    private void loadGymsData() {
        // Prendre données de la BD
        ObservableList<Salle> gymsList = FXCollections.observableArrayList(salleCrud.getAllSalles());


        gymsTableView.setItems(gymsList);
    }


    private void refreshTableViewData() {
        // Effacez toutes les lignes existantes dans la TableView
        gymsTableView.getItems().clear();
        // Chargez toutes les données depuis la bBD
        List<Salle> allSalles = SalleCrud.getInstance().getAllSalles();
        // Ajoutez les données dans la Table
        gymsTableView.getItems().addAll(allSalles);
    }






    @FXML
    private void handleUpdateButtonAction() {
        Salle salle = gymsTableView.getSelectionModel().getSelectedItem();
        if (salle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une salle à modifier.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment modifier cette salle ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SalleModifier.fxml"));
            try {
                Parent root = loader.load();
                // Set the controller
                SalleModifController controller = loader.getController();
                controller.setSalle(salle);
                // Set up the stage
                Stage stage = new Stage();
                stage.setTitle("Admin Page");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current stage (optional)
                Stage currentStage = (Stage) modifierButton.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }


    @FXML
    void buttonSupprimer(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cette salle ?");



        Optional<ButtonType> result = alert.showAndWait();
        Salle selectedSalle = gymsTableView.getSelectionModel().getSelectedItem();

        if (result.isPresent() && result.get() == ButtonType.OK && selectedSalle != null) {
            salleservices.supprimerSalle(selectedSalle.getIdS());
            System.out.println("Supprimé du tableview");

            // Mettre à jour l'affichage en supprimant la salle de la TableView
            gymsTableView.getItems().remove(selectedSalle);
        }
    }

    @FXML
    void buttonToutSupprimer(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cette salle ?");



        Optional<ButtonType> result = alert.showAndWait();
        Salle selectedSalle = gymsTableView.getSelectionModel().getSelectedItem();

        if (result.isPresent() && result.get() == ButtonType.OK && selectedSalle != null) {
            salleservices.supprimertoutSalle(selectedSalle.getNomS());
            System.out.println("Supprimé du tableview");

            // Mettre à jour l'affichage en supprimant la salle de la TableView
            gymsTableView.getItems().remove(selectedSalle);
        }
    }



}




package controllers;

import entities.Coach;
import entities.enums.Seance;
import entities.enums.Sexe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Stage;
import services.CoachCrud;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CoachController {

    @FXML
    private TableView<Coach> Tab1;

    @FXML
    private PieChart statgenre;
    @FXML
    private Label nbtotal;

    @FXML
    private TableColumn<Coach, String> colNom;

    @FXML
    private TableColumn<Coach, String> colPrenom;

    @FXML
    private TableColumn<Coach, Sexe> colSexe;

    @FXML
    private TableColumn<Coach, Seance> colSeance;

    @FXML
    private TableColumn<Coach, String> colDescription;

    @FXML
    private TextField tfChercher;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button tfModifier;

    @FXML
    private Button tfSupprimer;
    @FXML
    private Button tfdeconnexion;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private TextField tfDiscription;

    @FXML
    private ComboBox<Sexe> tfSexe;

    @FXML
    private ComboBox<Seance> tfSeance;

    private CoachCrud coachCrud;
    private ObservableList<Coach> coachesObservableList;
    @FXML
    private Label nomUtilisateur;

    @FXML
    public void initialize() {
        tfdeconnexion.setOnAction(event -> {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the user clicks "OK", close the current window and open the authentication page
                Stage stage = (Stage) tfdeconnexion.getScene().getWindow();
                stage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                    Parent root = loader.load();
                    Stage authStage = new Stage();
                    authStage.setScene(new Scene(root));
                    authStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // maysir chay
            }
        });
        // Ajouter un écouteur de changement de texte pour le champ de recherche
        tfChercher.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filtrer la TableView en fonction du texte de recherche
            filterTableView(newValue);


        });

        coachCrud = new CoachCrud();
        tfSexe.getItems().addAll(Sexe.values());
        tfSeance.getItems().addAll(Seance.values());
        chargerDonnees();

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        colSeance.setCellValueFactory(new PropertyValueFactory<>("seance"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        Tab1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherCoachSelectionne(newSelection);
            }
        });

        // Appel de la méthode coachSearch() après avoir chargé les données


        // Mettre à jour le PieChart
        updatePieChart();
    }

    @FXML
    void ajouterCoach(javafx.event.ActionEvent event) {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String description = tfDiscription.getText();
        Sexe sexe = tfSexe.getValue();
        Seance seance = tfSeance.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || description.isEmpty() || sexe == null || seance == null) {
            afficherMessageErreur("Veuillez remplir tous les champs.");
            return;
        }

        Coach coach = new Coach(nom, prenom, description, sexe, seance);
        coachCrud.ajouterCoach(coach);
        afficherMessage("Coach ajouté avec succès");
        viderChamps();
        chargerDonnees();
        updatePieChart();
    }
    private void filterTableView(String searchText) {
        // Créer un prédicat pour filtrer les éléments de la TableView
        Predicate<Coach> predicate = utilisateur ->
                utilisateur.getNom().toLowerCase().contains(searchText.toLowerCase());

        // Créer une liste filtrée à partir de la liste originale des utilisateurs
        List<Coach> filteredList = coachCrud.afficherCoaches().stream()
                .filter(predicate)
                .collect(Collectors.toList());

        // Effacer toutes les lignes existantes dans la TableView
        Tab1.getItems().clear();

        // Ajouter les éléments filtrés à la TableView
        Tab1.getItems().addAll(filteredList);
    }
    @FXML
    void modifierCoach(javafx.event.ActionEvent event) {
        Coach coach = creerCoachAPartirDesChamps();
        if (coach != null) {

            Coach coachSelectionne = Tab1.getSelectionModel().getSelectedItem();
            if (coachSelectionne != null) {

                String ancienNom = coachSelectionne.getNom();

                coachCrud.modifierCoach(coach, ancienNom);
                afficherMessage("Coach modifié avec succès");
                viderChamps();
                chargerDonnees();
                updatePieChart();
            } else {
                afficherMessageErreur("Veuillez sélectionner un coach à modifier.");
            }
        }
    }

    @FXML
    void supprimerCoach(javafx.event.ActionEvent event) {
        Coach coachSelectionne = Tab1.getSelectionModel().getSelectedItem();
        if (coachSelectionne != null) {
            String nom = coachSelectionne.getNom();

            coachCrud.supprimerCoach(nom);
            afficherMessage("Coach supprimé avec succès");
            viderChamps();
            chargerDonnees();
            updatePieChart();
        } else {
            afficherMessageErreur("Veuillez sélectionner un coach à supprimer.");
        }
    }

    private Coach creerCoachAPartirDesChamps() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String description = tfDiscription.getText();
        Sexe sexe = tfSexe.getValue();
        Seance seance = tfSeance.getValue();
        if (!nom.isEmpty()) {
            return new Coach(nom, prenom, description, sexe, seance);
        }
        return null;
    }

    private void chargerDonnees() {
        List<Coach> coaches = coachCrud.afficherCoaches();
        coachesObservableList = FXCollections.observableArrayList(coaches);
        Tab1.setItems(coachesObservableList);
    }

    private void viderChamps() {
        tfNom.clear();
        tfPrenom.clear();
        tfDiscription.clear();
        tfSexe.getSelectionModel().clearSelection();
        tfSeance.getSelectionModel().clearSelection();
    }

    private void afficherMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.show();
    }

    private void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }

    @FXML
    void afficherCoachSelectionne(Coach newSelection) {
        if (newSelection != null) {
            tfNom.setText(newSelection.getNom());
            tfPrenom.setText(newSelection.getPrenom());
            tfDiscription.setText(newSelection.getDescription());
            tfSexe.setValue(newSelection.getSexe());
            tfSeance.setValue(newSelection.getSeance());
        }
    }

   /* public void coachSearch() {
        FilteredList<Coach> filter = new FilteredList<>(coachesObservableList, e -> true);

        // Assuming you have a TextField named tfChercher for searching
        tfChercher.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(coach -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (coach.getNom().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (coach.getPrenom().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (coach.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Coach> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(Tab1.comparatorProperty());
        Tab1.setItems(sortedList);
    }*/

    private void updatePieChart() {
        // Calculer les statistiques sur le nombre total de coachs pour chaque sexe
        Map<Sexe, Long> sexeCountMap = coachesObservableList.stream()
                .collect(Collectors.groupingBy(Coach::getSexe, Collectors.counting()));
        long totalCoachs = sexeCountMap.values().stream().mapToLong(Long::longValue).sum();
        nbtotal.setText(""+ totalCoachs);


        // Créer les données pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        sexeCountMap.forEach((sexe, count) -> {
            pieChartData.add(new PieChart.Data(sexe.toString(), count));
        });

        // Afficher les données dans le PieChart
        statgenre.setData(pieChartData);
    }
    @FXML
    private Button btnretour ;
    @FXML
    private void btnretour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfilProp.fxml"));
            MyConnection.getInstance().setId(0);
            Parent root = loader.load();
            Stage stage = (Stage) btnretour.getScene().getWindow();
            stage.setScene(new Scene(root));
            CoachController coachController = loader.getController();
            coachController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
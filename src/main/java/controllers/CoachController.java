package controllers;

import entities.Coach;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import entities.enums.Seance;
import entities.enums.Sexe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.CoachCrud;

import java.util.List;

public class CoachController {

    @FXML
    private TableView<Coach> Tab1;

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
    private Button btnAjouter;

    @FXML
    private Button tfModifier;

    @FXML
    private Button tfSupprimer;

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
    public void initialize() {
        coachCrud = new CoachCrud();
        tfSexe.getItems().addAll(Sexe.values());
        tfSeance.getItems().addAll(Seance.values());
        chargerDonnees();


        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        colSeance.setCellValueFactory(new PropertyValueFactory<>("seance"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    void ajouterCoach(ActionEvent event) {
        Coach coach = creerCoachAPartirDesChamps();
        coachCrud.ajouterCoach(coach);
        afficherMessage("Coach ajouté avec succès");
        viderChamps();
        chargerDonnees();
    }

    @FXML
    void modifierCoach(ActionEvent event) {
        Coach coach = creerCoachAPartirDesChamps();
        coachCrud.modifierCoach(coach);
        afficherMessage("Coach modifié avec succès");
        viderChamps();
        chargerDonnees();
    }

    @FXML
    void supprimerCoach(ActionEvent event) {
        String nom = tfNom.getText();
        coachCrud.supprimerCoach(nom);
        afficherMessage("Coach supprimé avec succès");
        viderChamps();
        chargerDonnees();
    }

    private Coach creerCoachAPartirDesChamps() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String description = tfDiscription.getText();
        Sexe sexe = tfSexe.getValue();
        Seance seance = tfSeance.getValue();
        return new Coach(nom, prenom, description, sexe, seance);
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
}

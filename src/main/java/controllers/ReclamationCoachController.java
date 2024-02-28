package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ReclamationCrud;

import java.util.List;

public class ReclamationCoachController {

    @FXML
    private TableView<Reclamation> tableViewReclamations;

    @FXML
    private TableColumn<Reclamation, String> colReclamation;

    @FXML
    private Button tfajoutreclamation;

    @FXML
    private Button tfmodifreclamation;

    @FXML
    private Button tfsuppressionreclamation;


    @FXML
    private TextField texte_reclamation;

    private ReclamationCrud reclamationCrud;
    private ObservableList<Reclamation> reclamationsObservableList;

    @FXML
    public void initialize() {
        reclamationCrud = new ReclamationCrud();
        chargerDonnees();

        colReclamation.setCellValueFactory(new PropertyValueFactory<>("texte"));

        tableViewReclamations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherReclamationSelectionnee(newSelection);
            }
        });
    }

    @FXML
    void ajouterReclamation(ActionEvent event) {
        String texteReclamation = texte_reclamation.getText();
        if (!texteReclamation.isEmpty()) {
            Reclamation nouvelleReclamation = new Reclamation(texteReclamation);
            reclamationCrud.ajouterReclamation(nouvelleReclamation);
            afficherMessage("Réclamation ajoutée avec succès");
            viderChamps();
            chargerDonnees();
        } else {
            afficherMessageErreur("Veuillez saisir une réclamation");
        }
    }


    @FXML
    void modifierReclamation(ActionEvent event) {
        Reclamation reclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            String nouveauTexteReclamation = texte_reclamation.getText();
            if (!nouveauTexteReclamation.isEmpty()) {

                String ancienTexteReclamation = reclamation.getTexte();


                reclamation.setTexte(nouveauTexteReclamation);
                reclamationCrud.modifierReclamation(reclamation, ancienTexteReclamation);


                afficherMessage("Réclamation modifiée avec succès");


                viderChamps();
                chargerDonnees();
            } else {
                afficherMessageErreur("Veuillez saisir le nouveau texte de la réclamation");
            }
        } else {
            afficherMessageErreur("Veuillez sélectionner une réclamation à modifier");
        }
    }


    @FXML
    void supprimerReclamation(ActionEvent event) {
        Reclamation reclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            String textereclamation = reclamation.getTexteReclamation();
            reclamationCrud.supprimerReclamation(textereclamation);
            afficherMessage("Réclamation supprimée avec succès");
            viderChamps();
            chargerDonnees();
        } else {
            afficherMessageErreur("Veuillez sélectionner une réclamation à supprimer");
        }
    }


    private void chargerDonnees() {
        List<Reclamation> reclamations = reclamationCrud.afficherReclamations();
        reclamationsObservableList = FXCollections.observableArrayList(reclamations);
        tableViewReclamations.setItems(reclamationsObservableList);
    }

    private void viderChamps() {
        texte_reclamation.clear();
    }

    private void afficherMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void afficherReclamationSelectionnee(Reclamation reclamation) {
        if (reclamation != null) {
            texte_reclamation.setText(reclamation.getTexte());
        }
    }
}

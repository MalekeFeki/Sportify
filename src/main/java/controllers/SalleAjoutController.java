package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import entities.Salle;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.SalleCrud;
import services.UtilisateurCrud;

public class SalleAjoutController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField adresseTextField;

    @FXML
    private Button annulerButton;

    @FXML
    private CheckBox climatisationCheckBox;

    @FXML
    private Button enregistrerButton;

    @FXML
    private TextField nomTextField;

    @FXML
    private CheckBox nutritionnisteCheckBox;

    @FXML
    private CheckBox parkingCheckBox;

    @FXML
    private TextField regionTextField;

    @FXML
    private CheckBox wifiCheckBox;

    @FXML
    void initialize() {
    }

    @FXML
    private void handleAnnulerButton() {
        // Add code here to handle the cancel button action
        // For example, close the window or clear the form fields
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveSalle(ActionEvent event) {
//Sauvegarde la salle dans la BD
        Set<String> options = new HashSet<>();

        if (wifiCheckBox.isSelected()) {
            options.add("wifi");
        }

        if (parkingCheckBox.isSelected()) {
            options.add("parking");
        }

        if (nutritionnisteCheckBox.isSelected()) {
            options.add("nutritionniste");
        }

        if (climatisationCheckBox.isSelected()) {
            options.add("climatisation");
        }
        Salle s=new Salle(nomTextField.getText(),adresseTextField.getText(),regionTextField.getText(),options);
        SalleCrud sc=new SalleCrud();
        sc.ajouterSalle(s);
        Alert alert =new Alert(Alert.AlertType.INFORMATION,"Salle ajouté", ButtonType.OK);
        alert.show();

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/SalleListe.fxml"));
        try {
            Parent root= loader.load();
            SalleListeController luc=loader.getController();

            nomTextField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }



}

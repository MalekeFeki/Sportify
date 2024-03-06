package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;


import java.sql.Date;
import java.time.LocalDate;

import entities.Salle;
import entities.Seance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.SalleCrud;
import services.SeanceCrud;

public class SeanceModifController {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField debutTextField;

    @FXML
    private TextField finTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button annulerButton;

    @FXML
    private Button enregistrerButton;

    private Seance seance; // The Salle object to be modified




    @FXML
    void initialize() {
    }

    @FXML
    private void handleAnnulerButton() {

        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
    public void setSeance(Seance seance) {
        this.seance = seance;

        // Update the UI with the Salle data
        if (seance != null) {
            nomTextField.setText(seance.getNomSeance());
            debutTextField.setText(seance.getDebut());
            finTextField.setText(seance.getFin());
            finTextField.setText(seance.getDateS().toString());

        }
    }

    @FXML
    void saveSeance(ActionEvent event) {
        // Validate input fields
        if (isValidInput()) {

            LocalDate localDate = datePicker.getValue();
            // Update the existing Salle object with the modified values
            if (seance != null) {
                seance.setNomSeance(nomTextField.getText());
                seance.setDebut(Integer.parseInt(debutTextField.getText()));
                seance.setDebut(Integer.parseInt(debutTextField.getText()));
                LocalDate selectedLocalDate = datePicker.getValue();
                seance.setDateS(Date.valueOf(selectedLocalDate));
            }

            SeanceCrud sc = new SeanceCrud();
            sc.modifierSeance(seance);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Seance modifié", ButtonType.OK);
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeanceListe.fxml"));
            try {
                Parent root = loader.load();
                SalleListeController luc = loader.getController();

                enregistrerButton.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private boolean isValidInput() {
        if (nomTextField.getText().isEmpty() || debutTextField.getText().isEmpty() || finTextField.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return false;
        }



        return true;
    }





}

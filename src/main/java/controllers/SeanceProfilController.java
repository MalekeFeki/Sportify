package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import entities.Seance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.SeanceCrud;

public class SeanceProfilController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button annulerButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField debutTextField;

    @FXML
    private Button enregistrerButton;

    @FXML
    private TextField finTextField;

    @FXML
    private Label nomLabel;
    private Seance seance;

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
            nomLabel.setText(seance.getNomSeance());
            debutTextField.setText(seance.getDebut());
            finTextField.setText(seance.getFin());
            finTextField.setText(seance.getDateS().toString());

        }
    }

    @FXML
    void saveSalle(ActionEvent event) {



            LocalDate localDate = datePicker.getValue();
            // Update the existing Salle object with the modified values
            if (seance != null) {
                seance.setNomSeance(nomLabel.getText());
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



package controllers;

import controllers.SeanceListeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import entities.Seance;
import services.SeanceCrud;

public class SeanceAjoutController {

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

    @FXML
    void saveSeance(ActionEvent event) {
        // Validate input fields
        if (isValidInput()) {

            Seance s = new Seance(nomTextField.getText(), Integer.parseInt(debutTextField.getText()),
                    Integer.parseInt(finTextField.getText()), java.sql.Date.valueOf(datePicker.getValue()));
            SeanceCrud sc = new SeanceCrud();
            sc.ajouterSeance(s);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Séance ajoutée", ButtonType.OK);
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeanceListe.fxml"));
            try {
                Parent root = loader.load();
                SeanceListeController slc = loader.getController();

                nomTextField.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidInput() {
        String nomSeance = nomTextField.getText();
        String debutText = debutTextField.getText();
        String finText = finTextField.getText();
        LocalDate date = datePicker.getValue();

        if (nomSeance.isEmpty() || debutText.isEmpty() || finText.isEmpty() || date == null) {
            showAlert("Veuillez remplir tous les champs.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }
}

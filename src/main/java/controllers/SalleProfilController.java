package controllers;

import entities.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.SalleCrud;
import tools.MyConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class SalleProfilController {

    @FXML
    private TextField tfId;
    @FXML
    private Button propButton1;

    @FXML
    private Button retourButton;

    @FXML
    private Label nomLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label regionLabel;

    @FXML
    private CheckBox wifiCheckBox;
    @FXML
    private CheckBox parkingCheckBox;
    @FXML
    private CheckBox nutritionnisteCheckBox;
    @FXML
    private CheckBox climatisationCheckBox;
    private Salle selectedSalle;






        @FXML
        private Label optionsLabel;

        private Salle salle;




        public void initData(Salle salle) throws SQLException {
            this.salle = salle;
            displaySalleDetails(salle);
        }

        private void displaySalleDetails(Salle selectedSalle) throws SQLException {
            Connection cnx = MyConnection.getInstance().getCnx();
            String req = "SELECT * from salle WHERE id =?";

            PreparedStatement pst = cnx.prepareStatement(req);

            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                nomLabel.setText(String.valueOf(salle.getNomS()));
                adresseLabel.setText(String.valueOf(salle.getAdresse()));
                regionLabel.setText(String.valueOf(salle.getRegion()));
                optionsLabel.setText(String.valueOf(salle.getOptions()));

                // Update CheckBoxes based on the options in the Salle object
                wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
                parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
                nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
                climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));
            }};


    public void setSalle(Salle salle) {
        this.salle = salle;

        // Update the UI with the Salle data
        if (salle != null) {
            nomLabel.setText(salle.getNomS());
            adresseLabel.setText(salle.getAdresse());
            regionLabel.setText(salle.getRegion());

            // Update CheckBoxes based on the options in the Salle object
            wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
            parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
            nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
            climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));

        }
    }


    public void afficherDetailsProfil(Salle salle) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from salle WHERE id =?";

        PreparedStatement pst = cnx.prepareStatement(req);

        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            nomLabel.setText(String.valueOf(salle.getNomS()));
            adresseLabel.setText(String.valueOf(salle.getAdresse()));
            regionLabel.setText(String.valueOf(salle.getRegion()));
            optionsLabel.setText(String.valueOf(salle.getOptions()));

            // Update CheckBoxes based on the options in the Salle object
            wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
            parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
            nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
            climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));
        }

//        public void setSalle(Salle salle) {
//            this.salle = salle;
//
//            // Update the UI with the Salle data
//            if (salle != null) {
//                nomLabel.setText(salle.getNomS());
//                adresseLabel.setText(salle.getAdresse());
//                regionLabel.setText(salle.getRegion());
//
//                // Update CheckBoxes based on the options in the Salle object
//                wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
//                parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
//                nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
//                climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));
//
//            }
//        }

    }


}

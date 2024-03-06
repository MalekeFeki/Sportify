package controllers;

import entities.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.SalleCrud;
import tools.MyConnection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class SalleProfilController  {

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
    @FXML
    private ImageView salleImageView;
    private Salle selectedSalle;






        @FXML
        private Label optionsLabel;

        private Salle salle;

//@FXML
//    void initialize() {
//
//    }

    private void loadImage() {
        String salleImage = salle.getImageSalle();
        if (salleImage != null && !salleImage.isEmpty()) {
            try {
                // Set the image for salleImageView
                salleImageView.setImage(new Image("file:"+salleImage));
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void initData(Salle salle) throws SQLException {
            this.salle = salle;
            displaySalleDetails(salle);
        }

    private void displaySalleDetails(Salle selectedSalle) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from salle WHERE idS =?";

        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1, selectedSalle.getIdS()); // Set the ID parameter

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            nomLabel.setText(String.valueOf(selectedSalle.getNomS()));
            adresseLabel.setText(String.valueOf(selectedSalle.getAdresse()));
            regionLabel.setText(String.valueOf(selectedSalle.getRegion()));
            optionsLabel.setText(String.valueOf(selectedSalle.getOptions()));

            String salleImage = salle.getImageSalle();
            System.out.println("displaySalleDetails"+salleImage);
            if (salleImage != null && !salleImage.isEmpty()) {
                // Set the image for salleImageView
//                Image image = new Image(new File(salleImage).toURI().toString());
//                System.out.println(salleImage);
           salleImageView.setImage(new Image("file:"+salleImage));
            }
            // Update CheckBoxes based on the options in the Salle object
            wifiCheckBox.setSelected(selectedSalle.getOptions().contains("wifi"));
            parkingCheckBox.setSelected(selectedSalle.getOptions().contains("parking"));
            nutritionnisteCheckBox.setSelected(selectedSalle.getOptions().contains("nutritionniste"));
            climatisationCheckBox.setSelected(selectedSalle.getOptions().contains("climatisation"));
        }
    }



    public void setSalle(Salle salle) {
//        this.salle = salle;

        // Update the UI with the Salle data
        if (salle != null) {
            nomLabel.setText("faziz"+salle.getNomS());
            adresseLabel.setText(salle.getAdresse());
            regionLabel.setText(salle.getRegion());

            // Update CheckBoxes based on the options in the Salle object
            wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
            parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
            nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
            climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));

            String salleImage = salle.getImageSalle();
            System.out.println(salleImage);
            if (salleImage != null && !salleImage.isEmpty()) {
                // Set the image for salleImageView
//                Image image = new Image(new File(salleImage).toURI().toString());
//                System.out.println(salleImage);
                salleImageView.setImage(new Image("file:"+salleImage));
            }
        }
    }


//    public void afficherDetailsProfil(Salle salle) throws SQLException {
//        Connection cnx = MyConnection.getInstance().getCnx();
//        String req = "SELECT * from salle WHERE id =?";
//
//        PreparedStatement pst = cnx.prepareStatement(req);
//
//        ResultSet rs = pst.executeQuery();
//        if(rs.next()){
//            nomLabel.setText(String.valueOf(salle.getNomS()));
//            adresseLabel.setText(String.valueOf(salle.getAdresse()));
//            regionLabel.setText(String.valueOf(salle.getRegion()));
//            optionsLabel.setText(String.valueOf(salle.getOptions()));
//
//            // Update CheckBoxes based on the options in the Salle object
//            wifiCheckBox.setSelected(salle.getOptions().contains("wifi"));
//            parkingCheckBox.setSelected(salle.getOptions().contains("parking"));
//            nutritionnisteCheckBox.setSelected(salle.getOptions().contains("nutritionniste"));
//            climatisationCheckBox.setSelected(salle.getOptions().contains("climatisation"));
//        }

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








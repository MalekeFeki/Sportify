package controllers;

import entities.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Salle selectedSalle;




        @FXML
        private Label optionsLabel;

        private Salle salle;




        public void initData(Salle salle) {
            this.salle = salle;
            displaySalleDetails();
        }

        private void displaySalleDetails() {
            if (salle != null) {
                tfId.setText(String.valueOf(salle.getIdS()));
                nomLabel.setText(salle.getNomS());
                adresseLabel.setText(salle.getAdresse());
                regionLabel.setText(salle.getRegion());


                StringBuilder optionsStringBuilder = new StringBuilder();
                salle.getOptions().forEach(option -> optionsStringBuilder.append(option).append(", "));
                String optionsString = optionsStringBuilder.toString().replaceAll(", $", ""); // Remove trailing comma
                optionsLabel.setText(optionsString);
            }
        }

    public void propSeance(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeanceAjout.fxml"));
        try {
            Parent root = loader.load();

            // Set up the stage
            Stage stage = new Stage();
            stage.setTitle("Add Seance Page");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (optional)
            Stage currentStage = (Stage) propButton1.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void afficherDetailsProfil(Salle salle) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from salle WHERE id =?";

        PreparedStatement pst = cnx.prepareStatement(req);

        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            tfId.setText(String.valueOf(salle.getIdS()));
            nomLabel.setText(String.valueOf(salle.getNomS()));
            adresseLabel.setText(String.valueOf(salle.getAdresse()));
            regionLabel.setText(String.valueOf(salle.getRegion()));
            optionsLabel.setText(String.valueOf(salle.getOptions()));
        }



    }


}

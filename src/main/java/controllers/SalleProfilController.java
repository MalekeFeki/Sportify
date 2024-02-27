package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SalleProfilController {

    @FXML
    private Label nomLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label regionLabel;

    // You can set the values of the labels using this method
    public void setGymProfile(String nomS, String adresse, String region) {
        nomLabel.setText(nomS);
        adresseLabel.setText(adresse);
        regionLabel.setText(region);
    }

    public void setValeurs(String nom, String adresse, String region) {
        this.nomLabel.setText(nom);
        this.adresseLabel.setText(adresse);
        this.regionLabel.setText(region);
    }

    // You can add more methods or functionality as needed

}

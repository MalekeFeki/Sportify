package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ProfilAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_deco;

    @FXML
    private Button btn_deco1;

    @FXML
    private Button btn_deco2;

    @FXML
    private Button btn_deco3;

    @FXML
    private Button btn_inscri;

    @FXML
    private RadioButton rbmembre;

    @FXML
    private RadioButton rbproprietaire;

    @FXML
    private TextField tfcin;

    @FXML
    private TextField tfemail;

    @FXML
    private PasswordField tfmdp;

    @FXML
    private TextField tfnom;

    @FXML
    private TextField tfnum_tel;

    @FXML
    private TextField tfprenom;
    private Utilisateur admin;

    public void setAdmin(Utilisateur admin) {
        this.admin = admin;
        afficherDetailsProfil(admin);  // Populate the fields when the admin is set
    }
    public void afficherDetailsProfil(Utilisateur admin) {
        tfcin.setText(String.valueOf(admin.getCin()));
        tfnum_tel.setText(String.valueOf(admin.getNum_tel()));
        tfnom.setText(admin.getNom());
        tfprenom.setText(admin.getPrenom());
        tfemail.setText(admin.getEmail());
        tfmdp.setText(admin.getMdp());
        rbproprietaire.setSelected(false);
        rbmembre.setSelected(false);
    }
    @FXML
    void initialize() {


    }

}

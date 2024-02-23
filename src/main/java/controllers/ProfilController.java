package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ProfilController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_deco;

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
    public void initData(Utilisateur utilisateur) {
        tfnom.setText(utilisateur.getNom());
        tfprenom.setText(utilisateur.getPrenom());
        tfemail.setText(utilisateur.getEmail());
        tfmdp.setText(utilisateur.getMdp());
        tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
        tfcin.setText(String.valueOf(utilisateur.getCin()));

        // Vérifier et sélectionner le rôle approprié
        if (utilisateur.getRole() == Role.PROPRIETAIRE) {
            rbproprietaire.setSelected(true);
        } else {
            rbmembre.setSelected(true);
        }
    }

    @FXML
    void savePerson(ActionEvent event) {

    }

    @FXML
    void initialize() {


    }

}

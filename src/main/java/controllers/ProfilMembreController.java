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
import services.UtilisateurCrud;

public class ProfilMembreController {

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
    private Button btn_modif;

    @FXML
    private Button btn_deco3;

    @FXML
    private Button btn_enregismodif;

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

     private Utilisateur utilisateur;
    private UtilisateurCrud utilisateurCrud;
    private String email;
    private String password;
   public ProfilMembreController(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.utilisateurCrud = new UtilisateurCrud();
    }
    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @FXML
    void initialize() {
        // Afficher les informations de l'utilisateur dans les champs correspondants
        tfcin.setText(Integer.toString(utilisateur.getCin()));
        tfnum_tel.setText(Integer.toString(utilisateur.getNum_tel()));
        tfnom.setText(utilisateur.getNom());
        tfprenom.setText(utilisateur.getPrenom());
        tfemail.setText(utilisateur.getEmail());
        tfmdp.setText(utilisateur.getMdp());

        // Désactiver la modification du rôle de l'utilisateur
        rbmembre.setDisable(true);
        rbproprietaire.setDisable(true);
        if (utilisateur.getRole() == Role.MEMBRE) {
            rbmembre.setSelected(true);
        } else {
            rbproprietaire.setSelected(true);
        }

    }
    @FXML
    void modifierProfil() {
        // Mettre à jour les informations de l'utilisateur
        utilisateur.setCin(Integer.parseInt(tfcin.getText()));
        utilisateur.setNum_tel(Integer.parseInt(tfnum_tel.getText()));
        utilisateur.setNom(tfnom.getText());
        utilisateur.setPrenom(tfprenom.getText());
        utilisateur.setEmail(tfemail.getText());
        utilisateur.setMdp(tfmdp.getText());

        // Appeler la méthode de mise à jour dans UtilisateurCrud
        utilisateurCrud.modifierEntite(utilisateur);

        // Afficher un message de succès ou une notification à l'utilisateur
        // Vous pouvez ajouter cela en fonction de vos besoins
    }
}

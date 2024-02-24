package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    private String email;
    private String password;
    public void setMembre(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        afficherDetailsProfil(utilisateur);  // Populate the fields when the admin is set
    }

    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @FXML
    void initialize() {
        btn_deco.setOnAction(event -> {
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) btn_deco.getScene().getWindow();
            stage.close();

            // Charger la page d'authentification
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                Parent root = loader.load();
                Stage authStage = new Stage();
                authStage.setScene(new Scene(root));
                authStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




        // Gérer l'événement du bouton "Modifier profil"
        btn_enregismodif.setOnAction(event -> {
            // Récupérer les nouvelles valeurs des champs de texte
            int cin = Integer.parseInt(tfcin.getText());
            int num_tel = Integer.parseInt(tfnum_tel.getText());
            String nom = tfnom.getText();
            String prenom = tfprenom.getText();
            String email = tfemail.getText();
            String mdp = tfmdp.getText();

            // Mettre à jour les propriétés de l'utilisateur
            utilisateur.setCin(cin);
            utilisateur.setNum_tel(num_tel);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setMdp(mdp);

            // Appeler la méthode de votre classe UtilisateurCrud pour mettre à jour les informations de l'utilisateur dans la base de données
            utilisateurCrud.modifierEntite(utilisateur);
        });
    }
    public void afficherDetailsProfil(Utilisateur admin) {
        tfcin.setText(String.valueOf(admin.getCin()));
        tfnum_tel.setText(String.valueOf(admin.getNum_tel()));
        tfnom.setText(admin.getNom());
        tfprenom.setText(admin.getPrenom());
        tfemail.setText(admin.getEmail());
        tfmdp.setText(admin.getMdp());
        rbproprietaire.setSelected(false);
        rbmembre.setSelected(true);
    }

}

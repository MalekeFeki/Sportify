package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;

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
    private Utilisateur admin;
    private String email;
    private String password;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    @FXML
    private TableView<Utilisateur> tableView;
    @FXML
    private TableColumn<Utilisateur, Integer> colCin;

    @FXML
    private TableColumn<Utilisateur, Integer> colNumTel;

    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, Role> colRole;

    @FXML
    private TableColumn<Utilisateur, Void> colAction;


    public void setAdmin(Utilisateur admin) {
        this.admin = admin;
        afficherDetailsProfil(admin);  // Populate the fields when the admin is set
    }
    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
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

        btn_deco1.setOnAction(event -> {
            afficherProprietairesSalles();
        });

        btn_deco3.setOnAction(event -> {
            try {
                // Charger le fichier FXML de la page ListeUtilisateurs
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUtilisateurs.fxml"));
                Parent root = loader.load();

                // Créer une nouvelle scène avec le contenu chargé du fichier FXML
                Scene scene = new Scene(root);

                // Créer une nouvelle fenêtre (stage) et définir sa scène
                Stage stage = new Stage();
                stage.setScene(scene);

                // Afficher la fenêtre
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_deco1.setOnAction(event -> {
            afficherProprietairesSalles();
        });
    }
    @FXML
    void afficherProprietairesSalles() {
        ObservableList<Utilisateur> utilisateurs = utilisateurCrud.getUtilisateursByRole(Role.PROPRIETAIRE);
        tableView.setItems(utilisateurs);
    }
    }



package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

public class ProfilPropController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;
    @FXML
    private Button btn_deco1;

    @FXML
    private Button btn_deco3;

    @FXML
    private Button btn_deco4;


    @FXML
    private Button btn_deco2;

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


    @FXML
    private Text nomUtilisateur ;

    private Utilisateur utilisateur;

    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    private String email;
    private String password;
    @FXML
    private TextField tfid;
    public void initData(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Pre-fill text fields with user information
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getMdp()); // Consider security implications of displaying password
        }
    }

    @FXML
    void savePerson(ActionEvent event) {

    }

    public void initialize() {
        // Effectuer la liaison bidirectionnelle entre le texte de tfnom et le texte de nomUtilisateur
//        nomUtilisateur.textProperty().bindBidirectional(tfnom.textProperty());

        Utilisateur utilisateur = utilisateurCrud.getUtilisateurById(MyConnection.instance.getId());
        if (utilisateur != null) {
            nomUtilisateur.setText(utilisateur.getNom());
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getMdp());
            rbproprietaire.setSelected(true);
            rbmembre.setSelected(false);
        } else {
            // Handle the case where user details are not found
            System.out.println("User details not found.");
        }

        // Récupérer l'ID de l'utilisateur authentifié depuis MyConnection
        int userId = MyConnection.getInstance().getId();

        // Interroger la base de données pour récupérer les données du profil de l'utilisateur
        // Utiliser une méthode de service pour récupérer les données du profil en fonction de l'ID de l'utilisateur
        UtilisateurCrud utilisateurCrud = new UtilisateurCrud();


        // Mettre à jour les champs de texte dans l'interface utilisateur avec les données du profil
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
        }

        btn_deco2.setOnAction(event -> {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the user clicks "OK", close the current window and open the authentication page
                Stage stage = (Stage) btn_deco2.getScene().getWindow();
                stage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                    Parent root = loader.load();
                    Stage authStage = new Stage();
                    authStage.setScene(new Scene(root));
                    authStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // maysir chay
            }
        });


        // Remplir les champs de texte avec les données de l'utilisateur
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getMdp());
        }

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
        btn_deco1.setOnAction(event -> {
            try {
                // Charger coach.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/coach.fxml"));
                Parent root = loader.load();

                // Afficher la scène associée à coach.fxml
                Stage stage = (Stage) btn_deco1.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private Button btn_EVENT;

    @FXML
    private void GoEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btn_EVENT.getScene().getWindow();
            stage.setScene(new Scene(root));
            ProfilPropController profilPropController = loader.getController();
            profilPropController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void annuler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btn_annul.getScene().getWindow();
            stage.setScene(new Scene(root));
            HomePage coachController = loader.getController();
            coachController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
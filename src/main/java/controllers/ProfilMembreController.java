package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

public class ProfilMembreController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_deco2;

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
    @FXML
    private TextField tfid;
    @FXML
    private Label nomUtilisateur;
    public void setUtilisateur(Utilisateur utilisateur) throws SQLException {
        this.utilisateur = utilisateur;
        afficherDetailsProfil(utilisateur);
    } // Populate the fields when the admin is set


    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @FXML
    void initialize() {
        // Effectuer la liaison bidirectionnelle entre le texte de tfnom et le texte de nomUtilisateur
        nomUtilisateur.textProperty().bindBidirectional(tfnom.textProperty());

        Utilisateur utilisateur = utilisateurCrud.getUtilisateurById(MyConnection.instance.getId());
        if (utilisateur != null) {
            tfid.setText(String.valueOf(utilisateur.getId()));
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getMdp());
            rbproprietaire.setSelected(false);
            rbmembre.setSelected(true);
        } else {
            // Handle the case where user details are not found
            System.out.println("User details not found.");
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

        // Gérer l'événement du bouton "Modifier profil"
        btn_enregismodif.setOnAction(event -> modifierProfil());

    }
    public void afficherDetailsProfil(Utilisateur membre) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from utilisateur WHERE id =?";

        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1,MyConnection.instance.getId());
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            tfid.setText(String.valueOf(membre.getId()));
            tfcin.setText(String.valueOf(membre.getCin()));
            tfnum_tel.setText(String.valueOf(membre.getNum_tel()));
            tfnom.setText(membre.getNom());
            tfprenom.setText(membre.getPrenom());
            tfemail.setText(membre.getEmail());
            tfmdp.setText(membre.getMdp());
            rbproprietaire.setSelected(false);
            rbmembre.setSelected(true);
            nomUtilisateur.setText(tfnom.getText());
        }


    }
    @FXML
    private void modifierProfil() {
        // Récupérer les valeurs des champs de texte
        int id=Integer.parseInt(tfid.getText());
        int cin = Integer.parseInt(tfcin.getText());
        int num_tel = Integer.parseInt(tfnum_tel.getText());
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        String email = tfemail.getText();
        String mdp = tfmdp.getText();
        Role role = Role.MEMBRE;

        // Créer un nouvel objet Utilisateur avec les valeurs récupérées
        Utilisateur u = new Utilisateur(id,cin, num_tel, nom, prenom, email, mdp,role);

        // Appeler la méthode de mise à jour appropriée du CRUD Utilisateur
        utilisateurCrud.modifierEntite(u);
    }


}
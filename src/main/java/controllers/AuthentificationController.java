package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entities.Mailing;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

public class AuthentificationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button btn_auth;

    @FXML
    private TextField tfemail;

    @FXML
    private PasswordField tfmdp;
    private List<Utilisateur> registeredUsers;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

    private String cin;
    private String numTel;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    @FXML
    private Hyperlink hyperlink;

    public void setRegisteredUsers(List<Utilisateur> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.show();
    }
    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Connexion réussie !");
        alert.showAndWait();
    }
    int idconnecte ;
    @FXML
    void initialize() {

        btn_auth.setOnAction(event -> {
            // Récupérer les informations d'identification fournies par l'utilisateur (par exemple, à partir de champs de texte)
            String email = tfemail.getText();
            String password = tfmdp.getText();

            // Appeler la fonction de login de votre classe UtilisateurCrud
            UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

            // Vérifier si les informations d'identification sont valides
            if (utilisateurCrud.authenticateUser(email, password)) {
                // Si l'utilisateur est trouvé, afficher un message de succès
                showSuccessMessage();
                Connection cnx2=MyConnection.instance.getCnx();
                String reqUserId = "SELECT id FROM utilisateur WHERE email = ?";
                PreparedStatement pstUserId = null;
                try {
                    pstUserId = cnx2.prepareStatement(reqUserId);
                    pstUserId.setString(1, tfemail.getText());
                    ResultSet rsUserId = pstUserId.executeQuery();
                    rsUserId.next();
                    idconnecte = rsUserId.getInt(1);
                    MyConnection.getInstance().setId(idconnecte);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Rediriger vers le profil correspondant au rôle de l'utilisateur
                redirectToProfile(utilisateurCrud.getUtilisateurByEmail(email).getRole().toString());

                // Fermer la fenêtre d'authentification
                Stage stage = (Stage) btn_auth.getScene().getWindow();
                stage.close();
            } else {
                // Si l'authentification échoue, afficher un message d'erreur
                showAlert("Email ou mot de passe incorrect !");
            }
          /*  hyperlink.setOnAction(actionEvent -> {
                // Call the method to send the email
                Mailing.envoyerEmailNouveauMdp(destinataire, nouveauMdp, emailUtilisateur, motDePasseUtilisateur);
            });*/

        });
    }
    public void redirectToProfile(String role) {


        try {
            String fxmlFileName;
            switch (role) {
                case "ADMIN":
                    fxmlFileName = "/ProfilAdmin.fxml";
                    break;
                case "MEMBRE":
                    fxmlFileName = "/ProfilMembre.fxml";
                    break;
                case "PROPRIETAIRE":
                    fxmlFileName = "/ProfilProp.fxml";
                    break;
                default:
                    // Gérer d'autres cas si nécessaire
                    return;
            }

            // Charger le fichier FXML correspondant au rôle de l'utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
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
    }


}
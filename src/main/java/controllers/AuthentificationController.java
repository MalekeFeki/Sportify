package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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

public class AuthentificationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button btn;

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
    @FXML
    void initialize() {

        btn.setOnAction(event -> {
            // Récupérer les informations d'identification fournies par l'utilisateur (par exemple, à partir de champs de texte)
            String email = tfemail.getText();
            String password = tfmdp.getText();

            // Appeler la fonction de login de votre classe UtilisateurCrud
            UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
            Utilisateur utilisateur = utilisateurCrud.login(email, password);

            // Si l'utilisateur est trouvé, afficher un message de succès
            if (utilisateur != null) {
                showSuccessMessage();
                redirectToProfile(utilisateur.getRole().toString());
                // Fermer la fenêtre d'authentification
                Stage stage = (Stage) btn.getScene().getWindow();
                stage.close();
            }
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
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.Mailing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    @FXML
    private CheckBox show;
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
    @FXML
    private TextField tfshowpassword;

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
    private String enteredEmail;
    @FXML
    void initialize() {

        show.setOnAction(event -> handleShowPassCheckboxClick());
        tfshowpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfmdp.setText(newValue);
            }
        });

        tfmdp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfshowpassword.setText(newValue);
            }
        });



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
                // Store the entered email in the array
                enteredEmail =  tfemail.getText();;

                // Rediriger vers le profil correspondant au rôle de l'utilisateur
                redirectToProfile(utilisateurCrud.getUtilisateurByEmail(email).getRole().toString());

                // Fermer la fenêtre d'authentification
                Stage stage = (Stage) btn_auth.getScene().getWindow();
                stage.close();
            } else {
                // Si l'authentification échoue, afficher un message d'erreur
                showAlert("Email ou mot de passe incorrect !");
            }

            hyperlink.setOnAction(event1 -> {
                // Handle the hyperlink click event
                if (enteredEmail == null || enteredEmail.isEmpty()) {
                    // Show an alert or handle the case where the email is empty
                    System.out.println("Veuillez entrer votre email pour réinitialiser votre mot de passe");
                } else {
                    // Call the method to send the email
                    sendPasswordResetEmail(enteredEmail);
                }
            });

        });
    }

    @FXML
    void handleShowPassCheckboxClick() {
        if (show.isSelected()) {
            // Afficher le mot de passe en clair
            tfshowpassword.setText(tfmdp.getText());
            tfshowpassword.setVisible(true);
            tfmdp.setVisible(false);
        } else {
            // Masquer le mot de passe en clair et montrer le PasswordField à nouveau
            tfmdp.setText(tfshowpassword.getText());
            tfmdp.setVisible(true);
            tfshowpassword.setVisible(false);
        }
    }


    private void sendPasswordResetEmail(String recipient) {

            String senderEmail = "malekfeki18@gmail.com";
            String senderPassword = "ozgm ipxf foxo uplz";


            String subject = "Réinitialisation de mot de passe";
            String body = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe: [Your Reset Link or Token]";

            // Set the recipient to the value of tfemail.getText()
             recipient = tfemail.getText();

            Mailing.sendEmail(recipient, subject, body);
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
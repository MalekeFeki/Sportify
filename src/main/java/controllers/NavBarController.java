package controllers;
import entities.Utilisateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;
import tools.MyConnection.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Optional;

public class NavBarController {

    @FXML
    private HBox guestBox;

    @FXML
    private HBox userBox;
    @FXML
    private Button sign_in;
    @FXML
    private Button btn_deconn;

    @FXML
    private Button sign_up;
    @FXML
    private Label userNameLabel;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud() ;
    private Utilisateur ut = new Utilisateur() ;
    @FXML
    private void initialize() {
        // Check user authentication status and update the view
        updateView();
    }
    int iduser ;
    private void updateView() {
        boolean userLoggedIn ;
        iduser = MyConnection.getInstance().getId() ;
        if(iduser != 0){
            userLoggedIn = /* Check if the user is logged in */ true;

        }else{
            userLoggedIn = /* Check if the user is logged in */ false;
        }


        guestBox.setVisible(!userLoggedIn);
        userBox.setVisible(userLoggedIn);

        if (userLoggedIn) {
            // Set the username (replace with actual username)
            ut=utilisateurCrud.getUtilisateurById(iduser);

            userNameLabel.setText(ut.getNom());
        }
    }

    @FXML
    private void signUpClicked() {
        try {
            // Load reclamation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/inscription.fxml"));
            Parent root = loader.load();

            // Display the scene associated with reclamation.fxml
            Stage stage = (Stage) sign_up.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Handle sign-up button click
        System.out.println("Sign Up clicked");
    }

    @FXML
    private void signInClicked() {
        try {
            // Load reclamation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
            Parent root = loader.load();
            Scene currentScene = sign_up.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Handle sign-in button click
        System.out.println("Sign In clicked");
    }

    @FXML
    private void profileClicked() {
        ut=utilisateurCrud.getUtilisateurById(iduser);

        try {
            String fxmlFileName;
            switch (ut.getRole().toString()) {
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
            Scene currentScene = sign_up.getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Handle profile button click
        System.out.println("Profile clicked");
    }

    @FXML
    private void signOutClicked() {
        // Display a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // If the user clicks "OK", close the current window and open the authentication page
            MyConnection.getInstance().setId(0);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                Parent root = loader.load();
                Scene currentScene = sign_up.getScene();

                currentScene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // maysir chay
        }
        // Handle sign-out button click
        System.out.println("Sign Out clicked");
    }
}

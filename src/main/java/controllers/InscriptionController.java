package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;

public class InscriptionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button btn_annul;

    @FXML
    private Button btn_inscri;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    void savePerson(ActionEvent event) {
//Sauvegarde de personne dans la BD
        Utilisateur p=new Utilisateur(Integer.parseInt(tfcin.getText()),Integer.parseInt(tfnum_tel.getText()),tfnom.getText(),tfprenom.getText(),tfemail.getText(),tfmdp.getText(),rbmembre.isSelected() ? Role.MEMBRE : Role.PROPRIETAIRE);
        UtilisateurCrud uc=new UtilisateurCrud();
        uc.ajouterEntite(p);
        Role selectedRole = p.getRole();
        Alert alert =new Alert(Alert.AlertType.INFORMATION,"Utilisateur ajouté", ButtonType.OK);
        alert.show();

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/ListeUtilisateurs.fxml"));
        try {
            Parent root= loader.load();
            ListeUtilisateursController luc=loader.getController();
            luc.setResCin(tfcin.getText());
            luc.setResNumTel(tfnum_tel.getText());
            luc.setResNom(tfnom.getText());
            luc.setResPrenom(tfprenom.getText());
            luc.setResEmail(tfemail.getText());
            luc.setResRole(selectedRole);
            tfnom.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void initialize(URL url, ResourceBundle resourceBundle) {
        hyperlink.setOnAction(event -> {
            try {
                // Chargez la page d'authentification depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("authentification.fxml"));
                Parent root = loader.load();

                // Créez une nouvelle scène et un nouveau stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Affichez la nouvelle scène
                stage.show();

                // Fermez la fenêtre actuelle
                ((Stage) hyperlink.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace(); // Gérez l'exception selon vos besoins
            }
        });
    }


    }



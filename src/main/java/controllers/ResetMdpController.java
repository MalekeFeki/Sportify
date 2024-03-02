package controllers;

import entities.MdpHash;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import tools.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ResetMdpController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button tfreinit;

    @FXML
    void initialize() {
        tfreinit.setOnAction(event1 -> {
            // Handle the hyperlink click event
            if (passwordField == null || confirmPasswordField.getText().isEmpty()) {
                // Show an alert or handle the case where the email is empty
                System.out.println("Veuillez entrer votre nouveau mot de passe");
            } else {

            }
        });

    }

    public void modifierEntite(Utilisateur u) {
        // Hacher le nouveau mot de passe avant de le mettre à jour dans la base de données
        String hashedPassword = MdpHash.hashPassword(u.getMdp());
        u.setMdp(hashedPassword);

        // Préparer la requête SQL pour mettre à jour le mot de passe de l'utilisateur
        String req2 = "UPDATE utilisateur SET mdp = ? WHERE id = ?";

        try {
            // Créer une PreparedStatement avec la requête SQL
            Connection cnx2 = MyConnection.getInstance().getCnx();
            PreparedStatement pst = cnx2.prepareStatement(req2);

            // Définir les valeurs des paramètres dans la requête SQL
            pst.setString(1, hashedPassword); // Nouveau mot de passe haché
            pst.setInt(2, u.getId()); // ID de l'utilisateur

            // Exécuter la requête de mise à jour
            int rowsAffected = pst.executeUpdate();

            // Vérifier si la mise à jour a été effectuée avec succès
            if (rowsAffected > 0) {
                System.out.println("Mot de passe de l'utilisateur avec ID " + u.getId() + " modifié avec succès.");
            } else {
                System.out.println("Aucune modification effectuée pour l'utilisateur avec ID " + u.getId() + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du mot de passe de l'utilisateur : " + e.getMessage());
        }
    }

}

package services;

import controllers.Reclamation;
import entities.Coach;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReclamationCrud {
    private Connection cnx;

    public ReclamationCrud() {
        cnx = MyConnection.getInstance().getCnx();
    }

    public void ajouterReclamation(Reclamation reclamation) {
        String requete = "INSERT INTO reclamation (texte_reclamation) VALUES (?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, reclamation.getTexte());
            pst.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }



    public void supprimerReclamation(String texteReclamation) {
        String requete = "DELETE FROM reclamation WHERE texte_reclamation=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, texteReclamation);
            pst.executeUpdate();
            System.out.println("Réclamation supprimée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
        }
    }


    public List<Reclamation> afficherReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        String requete = "SELECT * FROM reclamation";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int idReclamation = rs.getInt("id_reclamation");
                String texteReclamation = rs.getString("texte_reclamation");
                Reclamation reclamation = new Reclamation(idReclamation, texteReclamation);
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
        }
        return reclamations;
    }


    public void modifierReclamation(Reclamation reclamation, String ancienTexteReclamation) {
        String requete = "UPDATE reclamation SET texte_reclamation=? WHERE texte_reclamation=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, reclamation.getTexte());
            pst.setString(2, ancienTexteReclamation);
            pst.executeUpdate();
            System.out.println("Réclamation modifiée avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la réclamation : " + e.getMessage());
        }
    }


}


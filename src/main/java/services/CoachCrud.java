package services;

import entities.Coach;
import entities.enums.Sexe;
import tools.MyConnection;
import entities.enums.Seance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachCrud {
    private Connection cnx;

    public CoachCrud() {
        cnx = MyConnection.getInstance().getCnx();
    }

    public void ajouterCoach(Coach c) {
        String requete = "INSERT INTO coach (NomCo, PrenomCo, Description, Sexe, Seance) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getDescription());
            pst.setString(4, c.getSexe().name());
            pst.setString(5, c.getSeance().name());

            pst.executeUpdate();
            System.out.println("Coach ajouté avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifierCoach(Coach c, String ancienNom) {
        String requete = "UPDATE coach SET NomCo=?, PrenomCo=?, Description=?, Sexe=?, Seance=? WHERE NomCo=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getDescription());
            pst.setString(4, c.getSexe().name());
            pst.setString(5, c.getSeance().name());
            pst.setString(6, ancienNom);

            pst.executeUpdate();
            System.out.println("Coach modifié avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public List<Coach> afficherCoaches() {
        List<Coach> listeCoaches = new ArrayList<>();
        String requete = "SELECT * FROM coach WHERE Sexe IN ('homme', 'femme') AND Seance IN ('a', 'b', 'c', 'd')";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(requete);
            while (rs.next()) {
                Sexe sexe = Sexe.valueOf(rs.getString("Sexe"));
                Seance seance = Seance.valueOf(rs.getString("Seance"));

                Coach c = new Coach(
                        rs.getString("NomCo"),
                        rs.getString("PrenomCo"),
                        rs.getString("Description"),
                        sexe,
                        seance
                );
                listeCoaches.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeCoaches;
    }

    public void supprimerCoach(String nom) {
        String requete = "DELETE FROM coach WHERE NomCo=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(requete);
            pst.setString(1, nom);
            pst.executeUpdate();
            System.out.println("Coach supprimé avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

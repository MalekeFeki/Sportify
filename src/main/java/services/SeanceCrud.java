package services;


import entities.Seance;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceCrud implements ISeanceCrud<Seance> {
    static Connection cnx2;
    public SeanceCrud(){
        cnx2= MyConnection.getInstance().getCnx();
    }

    public static List<Seance> getAllSeances() {
        List<Seance> seances= new ArrayList<>();

        // Préparer la requête SQL pour récupérer tous les Salles
        String sql = "SELECT * FROM seance";
        try (PreparedStatement statement = cnx2.prepareStatement(sql)) {
            // Exécuter la requête et récupérer le résultat
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcourir le résultat et ajouter chaque Salle à la liste
                while (resultSet.next()) {
                    Seance seance = new Seance();
                    seance.setNomSeance(resultSet.getString("nom"));
                    seance.setDebut(resultSet.getInt("debut"));
                        seance.setFin(resultSet.getInt("fin"));
                    seance.setDateS(resultSet.getDate("date"));
                    String options = resultSet.getString("options");
                    seances.add(seance);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return seances;
    }

    @Override
    public void ajouterSeance(Seance s) {
        String req1 = "INSERT INTO seance(idSeance, nomSeance, debut, fin, dateS) VALUES ('" + s.getIdSeance() + "','" + s.getNomSeance() + "','" + s.getDebut()+ "','" + s.getFin()+ "','" + s.getDateS()+"')";
        try {
            Statement st=cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Séance ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Seance> afficherSeance() {
        List<Seance> seances = new ArrayList<>();
        String req3 = "SELECT * FROM seance";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs=stm.executeQuery(req3);
            while (rs.next()){
                Seance s=new Seance();
                s.setIdSeance(rs.getInt(1));
                s.setNomSeance((rs.getString(2)));
                s.setDebut(rs.getInt("Debut"));
                s.setFin(rs.getInt("Fin"));
                s.setDateS(rs.getDate(5));
                seances.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return seances;
    }

    @Override
    public void modifierSeance(Seance s) {
        String req2 = "UPDATE seance SET nomSeance=?, debut=?, fin=?, dateS=? WHERE idSeance=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req2);
            pst.setInt(1, s.getIdSeance());
            pst.setInt(2, Integer.parseInt(s.getDebut()));
            pst.setInt(2, Integer.parseInt(s.getFin()));
            pst.setDate(3, s.getDateS());
            pst.executeUpdate();
            System.out.println("Séance modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerSeance(String nom) {
        String req3 = "DELETE FROM seance WHERE nomSeance=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setString(1, nom);
            pst.executeUpdate();
            System.out.println("Séance supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

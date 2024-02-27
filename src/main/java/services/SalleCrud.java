package services;


import entities.Salle;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SalleCrud implements ISalleCrud<Salle> {
    static Connection cnx2;

    public SalleCrud() {

        cnx2 = MyConnection.getInstance().getCnx();
    }


    private static final SalleCrud instance = new SalleCrud();

    public static SalleCrud getInstance() {
        return instance;
    }


    @Override
    public void ajouterSalle(Salle s) {
        // Assuming options is a Set<String> in your Salle class
        Set<String> options = s.getOptions();
        String optionsString = String.join(",", options);
        String req1 = "INSERT INTO salle (nom, adresse, region, options) " +
                "VALUES ('" + s.getNomS() + "','" + s.getAdresse() + "','" + s.getRegion() + "','" + optionsString + "')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Salle ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Salle> afficherSalle() {
        List<Salle> salles = new ArrayList<>();
        String req3 = "SELECT * FROM salle";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Salle s = new Salle();
                s.setIdS(rs.getInt(1));
                s.setNomS(rs.getString("nom"));
                s.setAdresse(rs.getString(3));
                s.setRegion(rs.getString(4));
                salles.add(s);
            }
            // Print the list of Salle objects
            for (Salle salle : salles) {
                System.out.println(salle);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return salles;
    }


    @Override
    public void modifierSalle(Salle s) {
        String req2 = "UPDATE salle SET nomS=?, adresse=?, region=?  WHERE idS=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req2);
            pst.setInt(1, s.getIdS());
            pst.setString(2, s.getNomS());
            pst.setString(3, s.getAdresse());
            pst.setString(4, s.getRegion());

            pst.executeUpdate();
            System.out.println("Salle modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerSalle(Salle u) {
        String req3 = "DELETE FROM salle WHERE idS = '" + u.getIdS() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req3);
            System.out.println("Salle supprimé");
        } catch (SQLException e) {
            System.out.println("test " + e.getMessage());
        }
    }

    private static final String SELECT_ALL_GYMS_QUERY = "SELECT * FROM salle";

    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();

        try (Connection connection = MyConnection.getInstance().getCnx();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GYMS_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String adresse = resultSet.getString("adresse");
                String region = resultSet.getString("region");
                String options = resultSet.getString("options");

                // Create a Salle object and add it to the list
                Salle salle = new Salle(nom, adresse, region, Collections.singleton(options));
                salles.add(salle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception according to your application's requirements
        }

        return salles;
    }
}

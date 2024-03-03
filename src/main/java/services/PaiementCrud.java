package services;

import entities.Paiement;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementCrud implements IPaiementCrud<Paiement> {
    Connection cnx;

    public PaiementCrud() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public boolean create(Paiement p) {
        String req = "INSERT INTO paiement(numeroCarteBancaire, ccv, expirationDate, datePayment, hourPayment, userId, PromoCode, PostalCode, dateDebutAbonnement, dateFinAbonnement, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // Check if the provided userId exists in the users table
            if (userExists(p.getUserId())) {
                PreparedStatement pst = cnx.prepareStatement(req);
                pst.setString(1, p.getNumeroCarteBancaire());
                pst.setString(2, p.getCcv());
                pst.setDate(3, Date.valueOf(p.getExpirationDate()));
                pst.setDate(4, Date.valueOf(p.getDatePayment()));
                pst.setTime(5, Time.valueOf(p.getHourPayment()));
                pst.setInt(6, p.getUserId());
                pst.setString(7, p.getPromoCode());
                pst.setInt(8, p.getPostalCode());
                pst.setDate(9, Date.valueOf(p.getDateDebutAbonnement()));
                pst.setDate(10, Date.valueOf(p.getDateFinAbonnement()));
                pst.setDouble(11, p.getPrice());

                int rowsAffected = pst.executeUpdate();
                System.out.println(rowsAffected + " rows affected");

                return rowsAffected > 0; // Return true if rows were affected, indicating success
            } else {
                System.out.println("User with ID " + p.getUserId() + " does not exist.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Return false by default if an exception occurred or user does not exist
    }

    @Override
    public List<Paiement> read() {
        List<Paiement> paiements = new ArrayList<>();
        String req = "SELECT * FROM paiement";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Paiement p = new Paiement(
                        rs.getInt("idPayment"),
                        rs.getString("numeroCarteBancaire"),
                        rs.getString("ccv"),
                        rs.getDate("expirationDate").toLocalDate(),
                        rs.getDate("datePayment").toLocalDate(),
                        rs.getTime("hourPayment").toLocalTime(),
                        rs.getInt("userId"),
                        rs.getString("PromoCode"),
                        rs.getInt("PostalCode"),
                        rs.getDate("dateDebutAbonnement").toLocalDate(),
                        rs.getDate("dateFinAbonnement").toLocalDate(),
                        rs.getDouble("price")
                        );
                paiements.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return paiements;
    }

    //@Override
    // public void update(Paiement p) {
    //   String req = "UPDATE paiement SET numeroCarteBancaire=?, ccv=?, expirationDate=?, datePayment=?, hourPayment=?,PromoCode=? WHERE idPayment = ? AND userId = ?";
    //  try {
    //    PreparedStatement pst = cnx.prepareStatement(req);
    //       pst.setString(1, p.getNumeroCarteBancaire());
    //       pst.setString(2, p.getCcv());
    //       pst.setDate(3, Date.valueOf(p.getExpirationDate()));
    //       pst.setDate(4, Date.valueOf(p.getDatePayment()));
    //        pst.setTime(5, Time.valueOf(p.getHourPayment()));
    //       pst.setInt(6, p.getIdPayment());
    //       pst.setInt(7, p.getUserId());
    //       pst.setString(8, p.getPromoCode());
    //       pst.executeUpdate();
    //       System.out.println("Paiement modifié");
    //    } catch (SQLException e) {
    //       System.out.println(e.getMessage());
    //    }
    //  }

    //@Override
    // public void delete(int idPayment, int userId) {
    //  String req = "DELETE FROM paiement WHERE idPayment = ? AND userId = ?";
    //   try {
    //        PreparedStatement pst = cnx.prepareStatement(req);
    //      pst.setInt(1, idPayment);
    //      pst.setInt(2, userId);
    //      pst.executeUpdate();
    //       System.out.println("Paiement supprimé");
    //   } catch (SQLException e) {
    //        System.out.println(e.getMessage());
    //   }
    //  }

    // Helper method to check if a user with the given ID exists
    private boolean userExists(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
package services;


import entities.Adhesion;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdhesionCrud implements IAdhésionCrud {
   public Connection connection = MyConnection.getInstance().getCnx();

    // Constructor to initialize the connection

    public AdhesionCrud() {
    }
    @Override
    public boolean createAdhesion(Adhesion adhesion) {

        if (!userExists(adhesion.getUserId())) {
            System.out.println("User ID does not exist.");
            return false;
        }

        // Check if the gym ID exists
        if (!gymExists(adhesion.getGymId())) {
            System.out.println("Gym ID does not exist.");
            return false;
        }

        String query = "INSERT INTO adhesion (userId, description, name, price, gymId, dateDebut,dateFin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adhesion.getUserId());
            statement.setString(2, adhesion.getDescription());
            statement.setString(3, adhesion.getName());
            statement.setDouble(4, adhesion.getPrice());
            statement.setInt(5, adhesion.getGymId());
            statement.setDate(6, java.sql.Date.valueOf(adhesion.getDateDebut()));
            statement.setDate(7, java.sql.Date.valueOf(adhesion.getDateFin()));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if rows were affected (insertion successful)
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred (insertion failed)
        }
    }

   // @Override
//    public void updateAdhesion(Adhesion adhésion) {
//        String query = "UPDATE adhésion SET userId = ?, description = ?, name = ?, price = ?, gymId = ?, duréé = ? WHERE adhésionId = ? AND userId = ? AND gymId = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, adhésion.getUserId());
//            statement.setString(2, adhésion.getDescription());
//            statement.setString(3, adhésion.getName());
//            statement.setDouble(4, adhésion.getPrice());
//            statement.setInt(5, adhésion.getGymId());
//            statement.setDate(6, java.sql.Date.valueOf(adhésion.getDateDebut()));
//            statement.setInt(7, adhésion.getAdhesionId());
//            statement.setInt(8, adhésion.getUserId());
//            statement.setInt(9, adhésion.getGymId());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean deleteAdhesionByPaymentInfo(Adhesion adhesion) {
        String query = "DELETE FROM adhesion WHERE dateDebut = ? AND dateFin = ? AND price = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(adhesion.getDateDebut()));
            statement.setDate(2, java.sql.Date.valueOf(adhesion.getDateFin()));
            statement.setDouble(3, adhesion.getPrice());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Adhesion getAdhesionByUserIdAndGymId(int userId, int gymId) throws SQLException {
        String query = "SELECT adhésionId, userId, description, name, price, gymId, dateDebut,dateFin FROM adhesion WHERE userId = ? AND gymId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, gymId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int adhesionId = resultSet.getInt("adhésionId");
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                LocalDate dateDebut = resultSet.getDate("dateDebut").toLocalDate();
                LocalDate dateFin = resultSet.getDate("dateFin").toLocalDate();
                return new Adhesion(adhesionId, userId, description, name, price, gymId, dateDebut, dateFin);
            } else {
                throw new SQLException("No adhesion found for user ID: " + userId + " and gym ID: " + gymId);
            }
        }
    }



    @Override
    public List<Adhesion> getAllAdhesions(int userId) {
        List<Adhesion> adhesions = new ArrayList<>();
        String query = "SELECT * FROM adhesion WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                LocalDate dateDebut = resultSet.getDate("dateDebut").toLocalDate();
                LocalDate dateFin = resultSet.getDate("dateFin").toLocalDate();
                adhesions.add(new Adhesion(description, name, price, dateDebut, dateFin));
            }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return adhesions;
    }

    @Override
    public boolean userExists(int userId) {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean gymExists(int gymId) {
        String query = "SELECT * FROM salle WHERE idS = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gymId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
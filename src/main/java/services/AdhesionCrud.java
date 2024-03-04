package services;


import entities.Adhesion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdhesionCrud implements IAdhésionCrud {
    private Connection connection;

    // Constructor to initialize the connection
    public AdhesionCrud(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public void updateAdhesion(Adhesion adhésion) {
        String query = "UPDATE adhésion SET userId = ?, description = ?, name = ?, price = ?, gymId = ?, duréé = ? WHERE adhésionId = ? AND userId = ? AND gymId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adhésion.getUserId());
            statement.setString(2, adhésion.getDescription());
            statement.setString(3, adhésion.getName());
            statement.setDouble(4, adhésion.getPrice());
            statement.setInt(5, adhésion.getGymId());
            statement.setDate(6, java.sql.Date.valueOf(adhésion.getDateDebut()));
            statement.setInt(7, adhésion.getAdhesionId());
            statement.setInt(8, adhésion.getUserId());
            statement.setInt(9, adhésion.getGymId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdhesion(int adhésionId, int userId, int gymId) {
        String query = "DELETE FROM adhésion WHERE adhésionId = ? AND userId = ? AND gymId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adhésionId);
            statement.setInt(2, userId);
            statement.setInt(3, gymId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Adhesion getAdhesionById(int adhesionId) {
        String query = "SELECT adhesionId, userId, description, name, price, gymId, dateDebut FROM adhésion WHERE adhésionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adhesionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                int gymId = resultSet.getInt("gymId");
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                LocalDate dateDebut = resultSet.getDate("dateDebut").toLocalDate();
                return new Adhesion(adhesionId, userId, description, name, price, gymId, dateDebut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Adhesion> getAllAdhesions() {
        List<Adhesion> adhesions = new ArrayList<>();
        String query = "SELECT * FROM adhesion";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int adhesionId = resultSet.getInt("adhesionId");
                int userId = resultSet.getInt("userId");
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int gymId = resultSet.getInt("gymId");
                LocalDate dateDebut = resultSet.getDate("dateDebut").toLocalDate();
                LocalDate dateFin = resultSet.getDate("dateFin").toLocalDate();
                adhesions.add(new Adhesion(adhesionId, userId, description, name, price, gymId, dateDebut, dateFin));
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
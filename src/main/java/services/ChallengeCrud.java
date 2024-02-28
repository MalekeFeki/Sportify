package services;

import entities.Challenge;
import entities.enums.TypeDifficulty;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeCrud implements IChallengeCrud<Challenge> {
    private Connection connection;

    public ChallengeCrud() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterChallenge(Challenge challenge) {
        String req1 = "INSERT INTO challenge(difficulty, description) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req1)) {
            pst.setString(1, challenge.getDifficulty().name());
            pst.setString(2, challenge.getDescription());
            pst.executeUpdate();
            System.out.println("Challenge ajouté");
        } catch (SQLException e) {
            System.out.println("Error while adding challenge: " + e.getMessage());
        }
    }

    @Override
    public List<Challenge> afficherChallenges() {
        List<Challenge> challengeList = new ArrayList<>();
        String req3 = "SELECT * FROM challenge";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req3)) {
            while (rs.next()) {
                Challenge challenge = new Challenge(
                        TypeDifficulty.valueOf(rs.getString("difficulty")),
                        rs.getString("description")
                );
                challenge.setIdC(rs.getInt("idC"));
                challengeList.add(challenge);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching challenges: " + e.getMessage());
        }
        System.out.println(challengeList);
        return challengeList;
    }

    @Override
    public void modifierChallenge(Challenge challenge) {
        String req2 = "UPDATE challenge SET difficulty=?, description=? WHERE idC=?";
        try (PreparedStatement pst = connection.prepareStatement(req2)) {
            pst.setString(1, challenge.getDifficulty().name());
            pst.setString(2, challenge.getDescription());
            pst.setInt(3, challenge.getIdC());
            pst.executeUpdate();
            System.out.println("Challenge modifié");
        } catch (SQLException e) {
            System.out.println("Error while modifying challenge: " + e.getMessage());
        }
    }

    @Override
    public void supprimerChallenge(int id) {
        String req3 = "DELETE FROM challenge WHERE idC=?";
        try (PreparedStatement pst = connection.prepareStatement(req3)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Challenge supprimé");
        } catch (SQLException e) {
            System.out.println("Error while deleting challenge: " + e.getMessage());
        }
    }
}

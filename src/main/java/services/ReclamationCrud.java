/*package services;

import entities.Reclamation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tools.MyConnection;

public class ReclamationCrudImpl implements IReclamationCrud {

    private final Connection connection;

    public ReclamationCrudImpl() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public boolean ajouterReclamation(Reclamation reclamation) {
        String requete = "INSERT INTO reclamation (contenu, date) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, reclamation.getContenu());
            preparedStatement.setDate(2, new java.sql.Date(reclamation.getDate().getTime()));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifierReclamation(Reclamation reclamation) {
        String requete = "UPDATE reclamation SET contenu=?, date=?, reponse=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setString(1, reclamation.getDescription());
            //preparedStatement.setDate(2, new java.sql.Date(reclamation.getDate().getTime()));
            //preparedStatement.setString(3, reclamation.getReponse());
            preparedStatement.setInt(2, reclamation.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification de la réclamation : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimerReclamation(int id) {
        String requete = "DELETE FROM reclamation WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Reclamation> afficherReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        String requete = "SELECT * FROM reclamation";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requete);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setDescription(resultSet.getString("contenu"));
                //reclamation.setDate(resultSet.getDate("date"));
                //reclamation.setReponse(resultSet.getString("reponse"));
                reclamations.add(reclamation);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des réclamations : " + ex.getMessage());
        }
        return reclamations;
    }
}*/

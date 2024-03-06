package services;

import entities.Avis;
import entities.enums.TypeAvis;
import tools.MyConnection;

import java.sql.*;
import java.util.*;

public class AvisCrud implements IAvisCrud<Avis> {
    private Connection connection;

    public AvisCrud() {
        connection = MyConnection.getInstance().getCnx();
    }
    /********************************/
    @Override
    public void ajouterAvis(Avis avis) {
        String req1 = "INSERT INTO avis(type, description) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req1)) {
            pst.setString(1, avis.getType().name());
            pst.setString(2, avis.getDescription());
            pst.executeUpdate();
            System.out.println("Avis ajouté");
        } catch (SQLException e) {
            System.out.println("Error while adding avis: " + e.getMessage());
        }
    }
    /********************************/
    @Override
    public List<Avis> afficherAvis() {
        List<Avis> avisList = new ArrayList<>();
        String req3 = "SELECT * FROM avis";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req3)) {
            while (rs.next()) {
                Avis avis = new Avis();
                avis.setIdA(rs.getInt(1));
                avis.setType(TypeAvis.valueOf(rs.getString("type")));
                avis.setDescription(rs.getString("description"));
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching avis: " + e.getMessage());
        }
        System.out.println(avisList);
        return avisList;
    }
    /********************************/
    @Override
    public void modifierAvis(Avis avis) {
        String req2 = "UPDATE avis SET type=?, description=? WHERE idA=?";
        try (PreparedStatement pst = connection.prepareStatement(req2)) {
            pst.setString(1, avis.getType().name());
            pst.setString(2, avis.getDescription());
            pst.setInt(3, avis.getIdA());
            pst.executeUpdate();
            System.out.println("Avis modifié");
        } catch (SQLException e) {
            System.out.println("Error while modifying avis: " + e.getMessage());
        }
    }
    /********************************/
    @Override
    public void supprimerAvis(int id) {
        String req3 = "DELETE FROM avis WHERE idA=?";
        try (PreparedStatement pst = connection.prepareStatement(req3)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Avis supprimé");
        } catch (SQLException e) {
            System.out.println("Error while deleting avis: " + e.getMessage());
        }
    }
    /********************************/
public Avis calculerAvisMoyen(List<Avis> avisList) {
    if (avisList == null || avisList.isEmpty()) {
        return new Avis(0, TypeAvis.MOYEN, "Aucun avis disponible");
    }

    int totalNotes = 0;

    for (Avis avis : avisList) {
        switch (avis.getType()) {
            case MEDIOCRE:
                totalNotes += 1;
                break;
            case PASSABLE:
                totalNotes += 2;
                break;
            case MOYEN:
                totalNotes += 3;
                break;
            case BIEN:
                totalNotes += 4;
                break;
            case EXCELLENT:
                totalNotes += 5;
                break;
        }
    }

    int moyenneNotes = totalNotes / avisList.size();

    TypeAvis typeAvisMoyen;
    if (moyenneNotes <= 1) {
        typeAvisMoyen = TypeAvis.MEDIOCRE;
    } else if (moyenneNotes <= 2) {
        typeAvisMoyen = TypeAvis.PASSABLE;
    } else if (moyenneNotes <= 3) {
        typeAvisMoyen = TypeAvis.MOYEN;
    } else if (moyenneNotes <= 4) {
        typeAvisMoyen = TypeAvis.BIEN;
    } else {
        typeAvisMoyen = TypeAvis.EXCELLENT;
    }

    return new Avis(0, typeAvisMoyen, "");
}
}

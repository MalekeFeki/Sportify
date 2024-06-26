package services;

import entities.Adhesion;

import java.sql.SQLException;
import java.util.List;

    public interface IAdhésionCrud {

        boolean deleteAdhesionByPaymentInfo(Adhesion adhesion);

        Adhesion getAdhesionByUserIdAndGymId(int userId, int gymId) throws SQLException ;

        boolean createAdhesion(Adhesion adhesion);

        // void updateAdhesion(Adhesion adhésion);

        List<Adhesion> getAllAdhesions(int userId);

        boolean userExists(int userId);

        boolean gymExists(int gymId);
    }
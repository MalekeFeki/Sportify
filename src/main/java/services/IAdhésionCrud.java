package services;

import entities.Adhesion;

import java.util.List;

    public interface IAdhésionCrud {

        void deleteAdhesion(int adhésionId, int userId, int gymId);

        Adhesion getAdhesionById(int adhésionId);

        boolean createAdhesion(Adhesion adhesion);

        void updateAdhesion(Adhesion adhésion);

        List<Adhesion> getAllAdhesions();

        boolean userExists(int userId);

        boolean gymExists(int gymId);
    }
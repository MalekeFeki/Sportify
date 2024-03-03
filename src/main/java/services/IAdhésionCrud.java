package services;

import entities.Adhesion;

import java.util.List;

    public interface IAdhésionCrud {
        boolean createAdhésion(Adhesion adhésion);

        void deleteAdhésion(int adhésionId, int userId, int gymId);

        Adhesion getAdhésionById(int adhésionId);

        void updateAdhésion(Adhesion adhésion);

        List<Adhesion> getAllAdhésions();

        boolean userExists(int userId);

        boolean gymExists(int gymId);
    }
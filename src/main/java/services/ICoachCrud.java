package services;

import entities.Coach;
import java.util.List;

public interface ICoachCrud {

    void ajouterCoach(Coach coach);
    List<Coach> listerTousLesCoachs();
    void modifierCoach(Coach coach);
    void supprimerCoach(int idCoach);
    Coach trouverCoachParId(int idCoach);
}
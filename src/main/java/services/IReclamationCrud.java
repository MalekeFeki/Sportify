package services;

import entities.Reclamation;
import java.util.List;

public interface IReclamationCrud {

    boolean ajouterReclamation(Reclamation reclamation);

    boolean modifierReclamation(Reclamation reclamation);

    boolean supprimerReclamation(int id);

    List<Reclamation> afficherReclamations();
}

package services;
import entities.Seance;
import java.util.List;

public interface ISeanceCrud<T> {

    void ajouterSeance(Seance s);

    List<Seance> afficherSeance();

    void modifierSeance(Seance s);

    void supprimerSeance(String nom);
}

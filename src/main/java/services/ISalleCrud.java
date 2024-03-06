package services;

import entities.Salle;

import java.util.List;

public interface ISalleCrud<T> {
    public void ajouterSalle(T u);
    public List<T> afficherSalle();

    public void modifierSalle(T u);


    void supprimerSalle(int id);

    void supprimertoutSalle(String nom);
}

package services;

import entities.Salle;

import java.util.List;

public interface ISalleCrud<T> {
    public void ajouterSalle(T u);
    public List<T> afficherSalle();

    public void modifierSalle(T u);

    public void supprimerSalle(T u);

    }

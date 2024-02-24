package services;

import entities.Evenement;
import entities.Utilisateur;

import java.util.List;

public interface IEvenementCrud<T> {
    public void ajouterEvent(T u);

    public  List<T> afficherEvent();

    public void modifierEvent(Evenement u);
    public void supprimerEvent(int id);
}

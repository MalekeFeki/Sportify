package services;

import entities.Utilisateur;

import java.util.List;

public interface IUtilisateurCrud<T> {
    public void ajouterEntite(T u);
    public List<T> afficherEntite();

    public void modifierEntite(Utilisateur u);

    void modifierProfil(Utilisateur u);

    public void supprimerEntite(int id);

    void modifier2(Utilisateur e);
}

package services;

import entities.Produit;

import java.util.List;

public interface IProduitCrud<T> {
    void ajouterProduit(Produit produit);
    List<T> afficherProduits();
    void modifierProduit(Produit produit);
    void supprimerProduit(int id);
}

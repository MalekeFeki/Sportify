package controllers;

import entities.Avis;
import entities.enums.TypeAvis;
import services.AvisCrud;

import java.util.List;
public class AfficherAvisController {
    private AvisCrud avisCrud;

    public AfficherAvisController() {
        this.avisCrud = new AvisCrud();
    }

    public void ajouterAvis(TypeAvis type, String description) {
        Avis avis = new Avis(0, type, description);
        avisCrud.ajouterAvis(avis);
    }

    public void afficherAvis() {
        List<Avis> avisList = avisCrud.afficherAvis();
        if (avisList.isEmpty()) {
            System.out.println("Aucun avis disponible");
        } else {
            for (Avis avis : avisList) {
                System.out.println(avis.toString());
            }
        }
    }

    public void modifierAvis(int id, TypeAvis type, String description) {
        Avis avis = new Avis(id, type, description);
        avisCrud.modifierAvis(avis);
    }

    public void supprimerAvis(int id) {
        avisCrud.supprimerAvis(id);
    }

    public void filterBadWordsAndPrint(String description) {
        String filteredDescription = avisCrud.filterBadWords(description);
        System.out.println(filteredDescription);
    }

    public void calculerEtAfficherAvisMoyen(List<Avis> listeAvis) {
        Avis avisMoyen = avisCrud.calculerAvisMoyen(listeAvis);
        System.out.println(avisMoyen.toString());
    }
}

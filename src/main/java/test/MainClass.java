package test;

import entities.Avis;
import entities.enums.TypeAvis;
import services.AvisCrud;
import tools.MyConnection;

public class MainClass {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();
        System.out.println(mc.hashCode() + "-" + mc2.hashCode());


        Avis avis = new Avis(1, TypeAvis.BIEN, "This is a good review.");


        AvisCrud avisCrud = new AvisCrud();


        avisCrud.ajouterAvis(avis);
        System.out.println("Avis ajouté.");


        System.out.println("Liste des avis:");
        avisCrud.afficherAvis().forEach(System.out::println);


       /*avis.setDescription("Updated review content.");
       // avisCrud.modifierAvis(avis);
        System.out.println("Avis modifié.");


        System.out.println("Liste des avis après modification:");
        avisCrud.afficherAvis().forEach(System.out::println);

        avisCrud.supprimerAvis(avis.getIdA());
        System.out.println("Avis supprimé.");


        System.out.println("Liste des avis après suppression:");
        avisCrud.afficherAvis().forEach(System.out::println);*/
    }
}

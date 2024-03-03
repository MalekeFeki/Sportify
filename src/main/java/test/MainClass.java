package test;

import entities.Avis;
import entities.Challenge;
import entities.enums.TypeAvis;
import entities.enums.TypeDifficulty;
import services.AvisCrud;
import services.ChallengeCrud;
import tools.MyConnection;

public class MainClass {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();
        System.out.println(mc.hashCode() + "-" + mc2.hashCode());


        Challenge challenge = new Challenge(1, "ChallengeName", TypeDifficulty.SIMPLE, "This is a good review.");



        ChallengeCrud ChallengeCrud = new ChallengeCrud();

          ChallengeCrud.ajouterChallenge(challenge);
        System.out.println("Challenge ajouté.");


        System.out.println("Liste des avis:");
        ChallengeCrud.afficherChallenges().forEach(System.out::println);


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

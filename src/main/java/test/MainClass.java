package test;

import entities.Avis;
import entities.enums.TypeAvis;
import services.AvisCrud;

import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        // Initialize the connection or any other setup if needed
        AvisCrud avisCrud = new AvisCrud();

        // Displaying the list of Avis
        System.out.println("Liste des avis:");
        List<Avis> avisList = avisCrud.afficherAvis();
        avisList.forEach(System.out::println);

        // Calculating the average Avis
        Avis avisMoyen = avisCrud.calculerAvisMoyen(avisList);

        // Displaying the average Avis
        System.out.println("Avis moyen calculé:");
        System.out.println(avisMoyen);
    }
}

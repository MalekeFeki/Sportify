package test;

import entities.Salle;
import services.SalleCrud;
import tools.MyConnection;

import java.util.HashSet;
import java.util.Set;

public class MainClass {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();
        System.out.println(mc.hashCode() + "-" + mc2.hashCode());

        Set<String> options = new HashSet<>();
        options.add("wifi");
        options.add("parking");
        options.add("nutritionniste");

        Salle p = new Salle("La piscine", "hay amal", "gabes",options);
        SalleCrud sc = new SalleCrud();

        // Print information about the Salle object
        System.out.println(p.toString()+ p.getOptions());



        // Print information about all Salle objects using SalleCrud
        System.out.println(sc.afficherSalle());
    }
}

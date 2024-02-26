package test;

import entities.Coach;
import services.CoachCrud;
import tools.MyConnection;

public class MainClass {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();
        System.out.println(mc.hashCode() + "-" + mc2.hashCode());

        //Utilisateur p = new Utilisateur(14505878, 53378560, "feki", "malek", "malekfeki18@gmail.com", "", Role.MEMBRE);
        //UtilisateurCrud pcd = new UtilisateurCrud();
        //pcd.ajouterEntite2(p);

       // Coach c = new Coach(14505878, "Dkhili", "Ines", "abhfjkkkk", Coach.Sexe.FEMME, Coach.Seance.valueOf("a"));
        //CoachCrud abc = new CoachCrud();
        //abc.ajouterCoach(c);
    }
}

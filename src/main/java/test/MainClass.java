package test;

import entities.Utilisateur;
import entities.enums.Role;
import services.UtilisateurCrud;
import tools.MyConnection;


public class MainClass {
    public static void main(String[] args) {
        MyConnection mc=MyConnection.getInstance();
        MyConnection mc2=MyConnection.getInstance();
        System.out.println(mc.hashCode()+"-"+mc2.hashCode());
        Utilisateur p=new Utilisateur(14505878,53378560,"feki","malek","malekfeki18@gmail.com","", Role.MEMBRE);
        UtilisateurCrud pcd=new UtilisateurCrud();
         System.out.println(pcd.afficherEntite());
        pcd.ajouterEntite2(p);






    }
}

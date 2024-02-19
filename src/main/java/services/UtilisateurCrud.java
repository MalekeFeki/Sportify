package services;


import entities.Utilisateur;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurCrud implements IUtilisateurCrud<Utilisateur> {
    Connection cnx2;
    public UtilisateurCrud(){
        cnx2= MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouterEntite(Utilisateur u) {
        String req1 = "INSERT INTO utilisateur(cin, num_tel, nom, prenom, email, mdp, role) VALUES ('" + u.getCin() + "','" + u.getNum_tel() + "','" + u.getNom() + "','" + u.getPrenom() + "','" + u.getEmail() + "','" + u.getMdp() + "','" + u.getRole() + "')";
        try {
            Statement st=cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Utilisateur ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> afficherEntite() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req3 = "SELECT * FROM utilisateur";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs=stm.executeQuery(req3);
            while (rs.next()){
                Utilisateur u=new Utilisateur();
                u.setIdC(rs.getInt(1));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString(3));
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }
    public void ajouterEntite2(Utilisateur u){
        String requete= "INSERT INTO utilisateur (cin, num_tel,nom,prenom,email,mdp,role) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst=cnx2.prepareStatement(requete);
            pst.setInt(1,u.getCin());
            pst.setInt(2,u.getNum_tel());
            pst.setString(3,u.getNom());
            pst.setString(4,u.getPrenom());
            pst.setString(5,u.getEmail());
            pst.setString(6,u.getMdp());
            pst.setString(7,u.getRole().name());
            pst.executeUpdate();
            System.out.println("Ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifierEntite(Utilisateur u) {
        String req2 = "UPDATE utilisateur SET cin=?, num_tel=?, nom=?, prenom=?, email=?, mdp=? WHERE id=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req2);
            pst.setInt(1, u.getCin());
            pst.setInt(2, u.getNum_tel());
            pst.setString(3, u.getNom());
            pst.setString(4, u.getPrenom());
            pst.setInt(5, u.getIdC()); // Assuming id is the primary key
            pst.setString(6, u.getEmail());
            pst.setString(7, u.getMdp());
            pst.executeUpdate();
            System.out.println("utilisateur modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerEntite(int id) {
        String req3 = "DELETE FROM utilisateur WHERE id=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("utilisateur supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

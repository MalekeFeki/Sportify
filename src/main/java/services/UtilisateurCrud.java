package services;


import entities.Utilisateur;
import entities.enums.Role;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UtilisateurCrud implements IUtilisateurCrud<Utilisateur> {
    private List<Utilisateur> registeredUsers;
    Connection cnx2;
    public UtilisateurCrud(){
        cnx2= MyConnection.getInstance().getCnx();
        //registeredUsers = getAllUtilisateurs();
    }
    // Méthode pour charger la liste des utilisateurs enregistrés depuis la base de données
    private List<Utilisateur> loadRegisteredUsers() {
        List<Utilisateur> users = new ArrayList<>();
        // Charger les utilisateurs depuis la base de données
        // Utilisez votre méthode getAllUtilisateurs() ou une autre méthode appropriée pour cela
        return users;
    }

    // Méthode pour obtenir la liste des utilisateurs enregistrés
    public List<Utilisateur> getRegisteredUsers() {
        if (registeredUsers == null) {
            // Charger la liste des utilisateurs enregistrés si elle n'a pas déjà été chargée
            registeredUsers = loadRegisteredUsers();
        }
        return registeredUsers;
    }

    @Override
    public void ajouterEntite(Utilisateur u) {
        String req1 = "INSERT INTO utilisateur(cin, num_tel, nom, prenom, email, mdp, role) VALUES ('" + u.getCin() + "','" + u.getNum_tel() + "','" + u.getNom() + "','" + u.getPrenom() + "','" + u.getEmail() + "','" + u.getMdp() + "','" + u.getRole() + "')";
        try {
            Statement st=cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Utilisateur ajouté");
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
                u.setId(rs.getInt(1));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString(3));
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }
    public void ajouterEntite2(Utilisateur u) {


        String requete = "INSERT INTO utilisateur (cin, num_tel, nom, prenom, email, mdp, role) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst = cnx2.prepareStatement(requete);
            pst.setInt(1, u.getCin());
            pst.setInt(2, u.getNum_tel());
            pst.setString(3, u.getNom());
            pst.setString(4, u.getPrenom());
            pst.setString(5, u.getEmail());
            pst.setString(6, u.getMdp());
            pst.setString(7, u.getRole().name());
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
            pst.setString(5, u.getEmail());
            pst.setString(6, u.getMdp());
            pst.setInt(7, u.getId()); // Assuming id is the primary key
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
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();

            // Préparer la requête SQL pour récupérer tous les utilisateurs
            String sql = "SELECT * FROM utilisateur";
            try (PreparedStatement statement = cnx2.prepareStatement(sql)) {
                // Exécuter la requête et récupérer le résultat
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Parcourir le résultat et ajouter chaque utilisateur à la liste
                    while (resultSet.next()) {
                        Utilisateur utilisateur = new Utilisateur();
                        utilisateur.setId(resultSet.getInt("id"));
                        utilisateur.setCin(resultSet.getInt("cin"));
                        utilisateur.setNum_tel(resultSet.getInt("num_tel"));
                        utilisateur.setNom(resultSet.getString("nom"));
                        utilisateur.setPrenom(resultSet.getString("prenom"));
                        utilisateur.setEmail(resultSet.getString("email"));
                        String roleString = resultSet.getString("role");
                        Role role = Role.valueOf(roleString);
                        utilisateur.setRole(role);
                        utilisateurs.add(utilisateur);
                    }
                }
            }
         catch (SQLException ex) {
            ex.printStackTrace();
        }
        return utilisateurs;
    }


    public void registerUser(Utilisateur user) {
        // Add the user to the list of registered users
        registeredUsers.add(user);
    }

    public boolean authenticateUser(String email, String password) {
        // Check if there's a registered user with matching email and password
        return registeredUsers.stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getMdp().equals(password));
    }
    public boolean utilisateurExisteDeja(String cin, String email) {


        // Vérifier si un utilisateur avec le même CIN ou la même adresse email existe déjà
        String query = "SELECT COUNT(*) FROM utilisateur WHERE cin = ? OR email = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, cin);
            pst.setString(2, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les exceptions de manière appropriée
        }

        return false;
    }

    public Utilisateur login(String email, String password) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM utilisateur WHERE email = ? AND mdp = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // L'utilisateur est trouvé dans la base de données
                    utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setCin(rs.getInt("cin"));
                    utilisateur.setNum_tel(rs.getInt("num_tel"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    String roleString = rs.getString("role");
                    Role role = Role.valueOf(roleString);
                    utilisateur.setRole(role);

                    // Afficher un message de succès
                    System.out.println("Connexion réussie !");
                } else {
                    // L'utilisateur n'est pas trouvé dans la base de données
                    System.out.println("Connexion échouée : email ou mot de passe incorrect !");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

}
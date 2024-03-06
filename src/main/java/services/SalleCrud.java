package services;


import entities.Salle;

import tools.MyConnection;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SalleCrud implements ISalleCrud<Salle> {
    static Connection cnx2;

    public SalleCrud() {

        cnx2 = MyConnection.getInstance().getCnx();
    }


    private static final SalleCrud instance = new SalleCrud();

    public static SalleCrud getInstance() {
        return instance;
    }


//    @Override
//    public void ajouterSalle(Salle s) {
//        // Use a local connection variable
//        try (Connection connection = MyConnection.getInstance().getCnx()) {
//            // Assuming options is a Set<String> in your Salle class
//            Set<String> options = s.getOptions();
//            String optionsString = String.join(",", options);
//            String req1 = "INSERT INTO salle (nom, adresse, region, options) " +
//                    "VALUES (?, ?, ?, ?)";
//
//            try (PreparedStatement st = connection.prepareStatement(req1)) {
//                st.setString(1, s.getNomS());
//                st.setString(2, s.getAdresse());
//                st.setString(3, s.getRegion());
//                st.setString(4, optionsString);
//                st.executeUpdate(req1);
//                System.out.println("Salle ajoutée");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    @Override
    public void ajouterSalle(Salle s) {
        // Assuming options is a Set<String> in your Salle class
        Set<String> options = s.getOptions();
        String optionsString = String.join(",", options);
        String req1 = "INSERT INTO salle (nom, adresse, region, options, imageSalle) " +
                "VALUES ('" + s.getNomS() + "','" + s.getAdresse() + "','" + s.getRegion() + "','" + optionsString + "','" + s.getImageSalle() + "')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Salle ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isAddressAlreadyUsed(String address) {
        try {
            // Assuming you have a method to get a Salle by address
            Salle existingSalle = getSalleByAddress(address);

            // If a salle with the given address is found, return true
            return existingSalle != null;
        } catch (Exception e) {
            // Handle any exceptions (e.g., database access error)
            e.printStackTrace();
            return false;
        }
    }

    // Example method to retrieve a Salle by address (replace with your actual implementation)
    private Salle getSalleByAddress(String address) {
        // Implement the logic to fetch a Salle by address from your database
        // This is just a placeholder; replace it with your actual database access code
        // Return null if no salle is found with the given address
        // Replace the following lines with your actual database access code
        List<Salle> salleList = getAllSalles(); // Replace with your actual method
        return salleList.stream()
                .filter(salle -> salle.getAdresse().equals(address))
                .findFirst()
                .orElse(null);
    }

//    @Override
//    public List<Salle> afficherSalle() {
//        List<Salle> salles = new ArrayList<>();
//        String req3 = "SELECT * FROM salle";
//        try /*(PreparedStatement pst = cnx2.prepareStatement(req3))*/{
//            Statement stm = cnx2.createStatement();
//            ResultSet rs = stm.executeQuery(req3);
//            while (rs.next()) {
//                Salle s = new Salle();
//                s.setIdS(rs.getInt(1));
//                s.setNomS(rs.getString("nom"));
//                s.setAdresse(rs.getString(3));
//                s.setRegion(rs.getString(4));
//                salles.add(s);
//            }
//            // Print the list of Salle objects
//            for (Salle salle : salles) {
//                System.out.println(salle);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return salles;
//    }


    @Override
    public List<Salle> afficherSalle() {
        List<Salle> salles = new ArrayList<>();
        String req3 = "SELECT * FROM salle";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs=stm.executeQuery(req3);
            while (rs.next()){
                Salle s=new Salle();
                s.setIdS(rs.getInt(1));
                s.setNomS(rs.getString("nom"));
                s.setAdresse(rs.getString(3));
                s.setRegion(rs.getString(4));
                s.setImageSalle(rs.getString(6));
                String optionsString = rs.getString("options");
                Set<String> options = new HashSet<>(Arrays.asList(optionsString.split(",")));

// Trim each option to remove leading and trailing spaces
                Set<String> trimmedOptions = options.stream().map(String::trim).collect(Collectors.toSet());

                s.setOptions(trimmedOptions);


                salles.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return salles;
    }



    @Override
    public void modifierSalle(Salle s) {
        String req2 = "UPDATE salle SET nom=?, adresse=?, region=?, options=?, imageSalle=? WHERE idS=?";
        System.out.println(s);

        Set<String> options = s.getOptions();
        String optionsString = String.join(",", options);

        try {
            PreparedStatement pst = cnx2.prepareStatement(req2);
            pst.setString(1, s.getNomS());
            pst.setString(2, s.getAdresse());
            pst.setString(3, s.getRegion());
            pst.setString(4,optionsString);
            pst.setString(5,s.getImageSalle());
            pst.setInt(6, s.getIdS());
            pst.executeUpdate();


                System.out.println("Salle modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    public Set<String> parseOptionsString(String optionsString) {
        String[] optionsArray = optionsString.split(", ");
        return new HashSet<>(Arrays.asList(optionsArray));
    }

    @Override
    public void supprimerSalle(int id) {
        String req3 = "DELETE FROM salle WHERE idS=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Salle supprimé");
            // Réinitialiser la séquence d'auto-incrémentation
            String resetSequenceQuery = "ALTER TABLE salle AUTO_INCREMENT = 1";
            PreparedStatement resetSequenceStatement = cnx2.prepareStatement(resetSequenceQuery);
            resetSequenceStatement.executeUpdate();
            System.out.println("Séquence réinitialisée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimertoutSalle(String nom) {
        String req3 = "DELETE FROM salle WHERE nom=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setString(1, nom);
            pst.executeUpdate();
            System.out.println("Salle supprimé");
            // Réinitialiser la séquence d'auto-incrémentation
            String resetSequenceQuery = "ALTER TABLE salle AUTO_INCREMENT = 1";
            PreparedStatement resetSequenceStatement = cnx2.prepareStatement(resetSequenceQuery);
            resetSequenceStatement.executeUpdate();
            System.out.println("Séquence réinitialisée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }






    private static final String SELECT_ALL_GYMS_QUERY = "SELECT * FROM salle";

//    public List<Salle> getAllSalles() {
//        List<Salle> salles = new ArrayList<>();
//
//        try (Connection connection = MyConnection.getInstance().getCnx();
//             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GYMS_QUERY);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            while (resultSet.next()) {
//                String nom = resultSet.getString("nom");
//                String adresse = resultSet.getString("adresse");
//                String region = resultSet.getString("region");
//                String options = resultSet.getString("options");
//
//                // Create a Salle object and add it to the list
//                Salle salle = new Salle(nom, adresse, region, Collections.singleton(options));
//                salles.add(salle);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Handle any SQL exception according to your application's requirements
//        }
//
//        return salles;
//    }

    public List<Salle> getAllSalles() {
        List<Salle> salles= new ArrayList<>();

        // Préparer la requête SQL pour récupérer tous les Salles
        String sql = "SELECT * FROM salle";
        try (PreparedStatement statement = cnx2.prepareStatement(sql)) {
            // Exécuter la requête et récupérer le résultat
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcourir le résultat et ajouter chaque Salle à la liste
                while (resultSet.next()) {
                    Salle Salle = new Salle();
                    Salle.setNomS(resultSet.getString("nom"));
                    Salle.setAdresse(resultSet.getString("adresse"));
                    Salle.setRegion(resultSet.getString("region"));
                    String options = resultSet.getString("options");
                    Salle.setImageSalle(resultSet.getString("imageSalle"));
                    salles.add(Salle);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return salles;
    }

    public static Salle getSallesById(int id) {
        Salle salle = null;
        String query = "SELECT * FROM salle WHERE id = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    salle = new Salle();
                    salle.setNomS(rs.getString("nom"));
                    salle.setAdresse(rs.getString("adresse"));
                    salle.setRegion(rs.getString("region"));
                    String options = rs.getString("options");


                    // Ajoutez d'autres champs selon vos besoins
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salle;
    }

    
}

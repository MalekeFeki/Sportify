package services;

import entities.Evenement;
import entities.enums.GenreEv;
import entities.enums.typeEvent;
import entities.enums.cityEV;
import tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EvenementCrud implements IEvenementCrud<Evenement> {
    Connection cnx2 ;
    public EvenementCrud(){
        this.cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEvent(Evenement u) {

        String req ="INSERT INTO Evenement(NomEv,DatedDebutEV,DatedFinEV,HeureEV,DescrptionEv,Photo,lieu,city,Tele,Email,FB_link,IG_link,GenreEvenement,typeEV,nombrePersonneInteresse) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst ;
        try {
            pst = cnx2.prepareStatement(req);
            pst.setString(1,u.getNomEv());
            pst.setDate(2, (Date) u.getDatedDebutEV());
            pst.setDate(3, (Date) u.getDatedFinEV());
            pst.setString(4,u.getHeureEV());
            pst.setString(5,u.getDescrptionEv());
            pst.setString(6,u.getPhoto());
            pst.setString(7,u.getLieu());
            pst.setString(8,u.getCity().name());
            pst.setString(9,u.getTele());
            pst.setString(10,u.getEmail());
            pst.setString(11,u.getFB_link());
            pst.setString(12,u.getIG_link());
            pst.setString(13,u.getGenreEvenement().name());
            pst.setString(14,u.getTypeEV().name());
            pst.setInt(15,u.getNombrePersonneInteresse());
            pst.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }


    public  List<Evenement> afficherEvent()  {
        List<Evenement> Evenements = new ArrayList<>();
        String req2= "SELECT * FROM Evenement";
        Statement st1 = null;
        try {
            st1 = cnx2.createStatement();
            ResultSet rs = st1.executeQuery(req2);
            while (rs.next()){
                Evenement p =new Evenement();
                p.setIDevent(rs.getInt(1));
                p.setNomEv(rs.getString("NomEv"));
                p.setDatedDebutEV(rs.getDate("DatedDebutEV"));
                p.setDatedFinEV(rs.getDate("DatedFinEV"));
                p.setHeureEV(rs.getString("HeureEV"));
                p.setDescrptionEv(rs.getString("DescrptionEv"));
                p.setPhoto(rs.getString("Photo"));
                p.setLieu(rs.getString("lieu"));
                p.setCity(cityEV.valueOf(rs.getString("city")));
                p.setTele(rs.getString("Tele"));
                p.setEmail(rs.getString("Email"));
                p.setFB_link(rs.getString("FB_link"));
                p.setIG_link(rs.getString("IG_link"));
                p.setGenreEvenement(GenreEv.valueOf(rs.getString("GenreEvenement")));
                p.setTypeEV(typeEvent.valueOf(rs.getString("typeEV")));
                p.setNombrePersonneInteresse(rs.getInt("nombrePersonneInteresse"));
                p.setCapacite(rs.getInt("Capacite"));
                Evenements.add(p);

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Evenements ;
    }

    @Override
    public void modifierEvent(Evenement u) {
        String req = "UPDATE Evenement SET NomEv=?, DatedDebutEV=?, DatedFinEV=?, HeureEV=?, DescrptionEv=?, " +
                "Photo=?, lieu=?,City=? ,Tele=?, Email=?, FB_link=?, IG_link=?, GenreEvenement=?, " +
                "typeEV=?, Capacite=? WHERE IDevent=?";

        try (PreparedStatement ps = cnx2.prepareStatement(req)) {
            ps.setString(1, u.getNomEv());
            ps.setDate(2, (Date) u.getDatedDebutEV());
            ps.setDate(3, (Date) u.getDatedFinEV());
            ps.setString(4, u.getHeureEV());
            ps.setString(5, u.getDescrptionEv());
            ps.setString(6, u.getPhoto());
            ps.setString(7, u.getLieu());
            ps.setString(8, u.getCity().toString());
            ps.setString(9, u.getTele());
            ps.setString(10, u.getEmail());
            ps.setString(11, u.getFB_link());
            ps.setString(12, u.getIG_link());
            ps.setString(13, u.getGenreEvenement().toString());
            ps.setString(14, u.getTypeEV().toString());
            ps.setInt(15, u.getCapacite());
            ps.setInt(16, u.getIDevent());

            int r = ps.executeUpdate();
            if (r > 0) {
                System.out.println("Event updated");
            } else {
                System.out.println("No event");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void supprimerEvent(int id) {
        String req = "DELETE FROM Evenement WHERE IDevent=?";

        try (PreparedStatement ps = cnx2.prepareStatement(req)) {
            ps.setInt(1, id);

            int r= ps.executeUpdate();
            if (r > 0) {
                System.out.println("Event deleted");
            } else {
                System.out.println("No event");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<String> getEventTypes() {
        List<String> eventTypes = new ArrayList<>();
        // Query to get distinct event types from the database
        String query = "SELECT DISTINCT GenreEvenement FROM Evenement";
        try (Statement statement = cnx2.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                eventTypes.add(resultSet.getString("GenreEvenement"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return eventTypes;
    }

    public List<String> getCities() {
        List<String> cities = new ArrayList<>();
        // Query to get distinct cities from the database
        String query = "SELECT DISTINCT city FROM Evenement";
        try (Statement statement = cnx2.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                cities.add(resultSet.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return cities;
    }



    public List<Evenement> filterEvents(String eventType, String city) {
        List<Evenement> filteredEvents = new ArrayList<>();
        // Construct the SQL query based on the provided parameters
        String query = "SELECT * FROM Evenement WHERE GenreEvenement = ? AND city = ?";
        try (PreparedStatement preparedStatement = cnx2.prepareStatement(query)) {
            preparedStatement.setString(1, eventType);
            preparedStatement.setString(2, city);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Evenement event = mapResultSetToEvent(resultSet);
                    filteredEvents.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return filteredEvents;
    }
    private Evenement mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        Evenement event = new Evenement();
        event.setIDevent(resultSet.getInt("IDevent"));
        event.setNomEv(resultSet.getString("NomEv"));
        event.setDatedDebutEV(resultSet.getDate("DatedDebutEV"));
        event.setDatedFinEV(resultSet.getDate("DatedFinEV"));
        event.setHeureEV(resultSet.getString("HeureEV"));
        event.setDescrptionEv(resultSet.getString("DescrptionEv"));
        event.setPhoto(resultSet.getString("Photo"));
        event.setLieu(resultSet.getString("lieu"));
        event.setCity(cityEV.valueOf(resultSet.getString("city")));
        event.setTele(resultSet.getString("Tele"));
        event.setEmail(resultSet.getString("Email"));
        event.setFB_link(resultSet.getString("FB_link"));
        event.setIG_link(resultSet.getString("IG_link"));
        event.setGenreEvenement(GenreEv.valueOf(resultSet.getString("GenreEvenement")));
        event.setTypeEV(typeEvent.valueOf(resultSet.getString("typeEV")));
        event.setNombrePersonneInteresse(resultSet.getInt("nombrePersonneInteresse"));
        event.setCapacite(resultSet.getInt("Capacite"));
        return event;
    }

}

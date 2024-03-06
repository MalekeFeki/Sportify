package services;

import entities.Evenement;
import entities.EventReservation;
import entities.enums.GenreEv;
import entities.enums.cityEV;
import entities.enums.typeEvent;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventReservationCrud implements IEventReservationCrud<EventReservation> {

    Connection cnx2;

    public EventReservationCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public boolean ajouterReservation(EventReservation u) {
        String req = "INSERT INTO EventReservation(eventId, reservationId, nom, prenom, cin, Email, num_tele) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pst = cnx2.prepareStatement(req)) {
            pst.setInt(1, u.getEventId());
            pst.setInt(2, u.getReservationId());
            pst.setString(3, u.getNom());
            pst.setString(4, u.getPrenom());
            pst.setInt(5, u.getCin());
            pst.setString(6, u.getEmail());
            pst.setInt(7, u.getNum_tele());
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public List<EventReservation> afficherReservation(int eventId) {
        List<EventReservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM EventReservation WHERE eventId = ?";
        try (PreparedStatement preparedStatement = cnx2.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    EventReservation reservation = new EventReservation();
                    reservation.setCin(resultSet.getInt("cin"));
                    reservation.setEventId(resultSet.getInt("eventId"));
                    reservation.setReservationId(resultSet.getInt("reservationId"));
                    reservation.setNom(resultSet.getString("nom"));
                    reservation.setNum_tele(resultSet.getInt("num_tele"));
                    reservation.setPrenom(resultSet.getString("prenom"));
                    reservation.setEmail(resultSet.getString("Email"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations: " + e.getMessage());
        }
        return reservations;
    }

}
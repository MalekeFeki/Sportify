package services;

import entities.EventReservation;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            return rowsAffected > 0;  // Returns true if at least one row was affected (success)
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;  // Return false in case of exception
        }
    }
}
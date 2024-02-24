package services;

import entities.EventReservation;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventReservationCrud implements IEventReservationCrud<EventReservation>{

    Connection cnx2 ;
    public EventReservationCrud(){
        cnx2 = MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouterReservation(EventReservation u) {
        String req ="INSERT INTO EventReservation(eventId,reservationId,nom,prenom,cin,Email,num_tele) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = cnx2.prepareStatement(req);
            pst.setInt(1,u.getEventId());
            pst.setInt(2,u.getReservationId());
            pst.setString(1,u.getNom());
            pst.setString(2,u.getPrenom());
            pst.setInt(2,u.getCin());
            pst.setString(2,u.getEmail());
            pst.setInt(2,u.getNum_tele());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

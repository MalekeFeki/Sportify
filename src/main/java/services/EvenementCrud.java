package services;

import entities.Evenement;
import entities.enums.GenreEv;
import entities.enums.typeEvent;
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

        String req ="INSERT INTO Evenement(NomEv,DatedDebutEV,DatedFinEV,HeureEV,DescrptionEv,Photo,lieu,Tele,Email,FB_link,IG_link,GenreEvenement,typeEV,nombrePersonneInteresse) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(8,u.getTele());
            pst.setString(9,u.getEmail());
            pst.setString(10,u.getFB_link());
            pst.setString(11,u.getIG_link());
            pst.setString(12,u.getGenreEvenement().name());
            pst.setString(13,u.getTypeEV().name());
            pst.setInt(14,u.getNombrePersonneInteresse());
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
                "Photo=?, lieu=?, Tele=?, Email=?, FB_link=?, IG_link=?, GenreEvenement=?, " +
                "typeEV=?, Capacite=? WHERE IDevent=?";

        try (PreparedStatement ps = cnx2.prepareStatement(req)) {
            ps.setString(1, u.getNomEv());
            ps.setDate(2, (Date) u.getDatedDebutEV());
            ps.setDate(3, (Date) u.getDatedFinEV());
            ps.setString(4, u.getHeureEV());
            ps.setString(5, u.getDescrptionEv());
            ps.setString(6, u.getPhoto());
            ps.setString(7, u.getLieu());
            ps.setString(8, u.getTele());
            ps.setString(9, u.getEmail());
            ps.setString(10, u.getFB_link());
            ps.setString(11, u.getIG_link());
            ps.setString(12, u.getGenreEvenement().toString());
            ps.setString(13, u.getTypeEV().toString());
            ps.setInt(14, u.getCapacite());
            ps.setInt(15, u.getIDevent());

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
}

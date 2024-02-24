package entities;

public class EventReservation {
    private int eventId;
    private Evenement event;
    private int reservationId ;
    private String nom ;
    private String prenom ;
    private int cin ;
    private String Email;
    private int num_tele ;

    public EventReservation() {
    }

    public EventReservation(int eventId, Evenement event, int reservationId, String nom, String prenom, int cin, String email, int num_tele) {
        this.eventId = eventId;
        this.event = event;
        this.reservationId = reservationId;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        Email = email;
        this.num_tele = num_tele;

        event.getReservations().add(this);
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Evenement getEvent() {
        return event;
    }

    public void setEvent(Evenement event) {
        this.event = event;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getNum_tele() {
        return num_tele;
    }

    public void setNum_tele(int num_tele) {
        this.num_tele = num_tele;
    }

}

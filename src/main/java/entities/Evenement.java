package entities;

import entities.enums.GenreEv;
import entities.enums.typeEvent;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Evenement {
    private List<EventReservation> reservations;
    private int IDevent;
    private String NomEv;
    private Date DatedDebutEV;
    private Date DatedFinEV;
    private String HeureEV;
    private String DescrptionEv;
    private String Photo;
    private String lieu;
    private String Tele;
    private String Email;
    private String FB_link;
    private String IG_link;
    private GenreEv GenreEvenement;
    private typeEvent typeEV ;
    private int nombrePersonneInteresse;
    private int Capacite;

    public Evenement() {
    }

    public Evenement(List<EventReservation> reservations, int IDevent, String nomEv, Date datedDebutEV, Date datedFinEV, String heureEV, String descrptionEv, String photo, String lieu, String tele, String email, String FB_link, String IG_link, GenreEv genreEvenement, typeEvent typeEV, int nombrePersonneInteresse, int capacite) {
        this.reservations = reservations;
        this.IDevent = IDevent;
        NomEv = nomEv;
        DatedDebutEV = datedDebutEV;
        DatedFinEV = datedFinEV;
        HeureEV = heureEV;
        DescrptionEv = descrptionEv;
        Photo = photo;
        this.lieu = lieu;
        Tele = tele;
        Email = email;
        this.FB_link = FB_link;
        this.IG_link = IG_link;
        GenreEvenement = genreEvenement;
        this.typeEV = typeEV;
        this.nombrePersonneInteresse = nombrePersonneInteresse;
        Capacite = capacite;
    }

    public Evenement(String nomEv, Date datedDebutEV, Date datedFinEV, String heureEV, String descrptionEv, String photo, String lieu, String tele, String email, String FB_link, String IG_link, GenreEv genreEvenement, typeEvent typeEV, int capacite) {
        NomEv = nomEv;
        DatedDebutEV = datedDebutEV;
        DatedFinEV = datedFinEV;
        HeureEV = heureEV;
        DescrptionEv = descrptionEv;
        Photo = photo;
        this.lieu = lieu;
        Tele = tele;
        Email = email;
        this.FB_link = FB_link;
        this.IG_link = IG_link;
        GenreEvenement = genreEvenement;
        this.typeEV = typeEV;
        Capacite = capacite;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "reservations=" + reservations +
                ", IDevent=" + IDevent +
                ", NomEv='" + NomEv + '\'' +
                ", DatedDebutEV=" + DatedDebutEV +
                ", DatedFinEV=" + DatedFinEV +
                ", HeureEV='" + HeureEV + '\'' +
                ", DescrptionEv='" + DescrptionEv + '\'' +
                ", Photo='" + Photo + '\'' +
                ", lieu='" + lieu + '\'' +
                ", Tele='" + Tele + '\'' +
                ", Email='" + Email + '\'' +
                ", FB_link='" + FB_link + '\'' +
                ", IG_link='" + IG_link + '\'' +
                ", GenreEvenement=" + GenreEvenement +
                ", typeEV=" + typeEV +
                ", nombrePersonneInteresse=" + nombrePersonneInteresse +
                ", Capacite=" + Capacite +
                '}';
    }

    public List<EventReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<EventReservation> reservations) {
        this.reservations = reservations;
    }

    public int getIDevent() {
        return IDevent;
    }

    public void setIDevent(int IDevent) {
        this.IDevent = IDevent;
    }

    public String getNomEv() {
        return NomEv;
    }

    public void setNomEv(String nomEv) {
        NomEv = nomEv;
    }

    public Date getDatedDebutEV() {
        return DatedDebutEV;
    }

    public void setDatedDebutEV(Date datedDebutEV) {
        DatedDebutEV = datedDebutEV;
    }

    public Date getDatedFinEV() {
        return DatedFinEV;
    }

    public void setDatedFinEV(Date datedFinEV) {
        DatedFinEV = datedFinEV;
    }

    public String getHeureEV() {
        return HeureEV;
    }

    public void setHeureEV(String heureEV) {
        HeureEV = heureEV;
    }

    public String getDescrptionEv() {
        return DescrptionEv;
    }

    public void setDescrptionEv(String descrptionEv) {
        DescrptionEv = descrptionEv;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTele() {
        return Tele;
    }

    public void setTele(String tele) {
        Tele = tele;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFB_link() {
        return FB_link;
    }

    public void setFB_link(String FB_link) {
        this.FB_link = FB_link;
    }

    public String getIG_link() {
        return IG_link;
    }

    public void setIG_link(String IG_link) {
        this.IG_link = IG_link;
    }

    public GenreEv getGenreEvenement() {
        return GenreEvenement;
    }

    public void setGenreEvenement(GenreEv genreEvenement) {
        GenreEvenement = genreEvenement;
    }

    public typeEvent getTypeEV() {
        return typeEV;
    }

    public void setTypeEV(typeEvent typeEV) {
        this.typeEV = typeEV;
    }

    public int getNombrePersonneInteresse() {
        return nombrePersonneInteresse;
    }

    public void setNombrePersonneInteresse(int nombrePersonneInteresse) {
        this.nombrePersonneInteresse = nombrePersonneInteresse;
    }

    public int getCapacite() {
        return Capacite;
    }

    public void setCapacite(int capacite) {
        Capacite = capacite;
    }
}

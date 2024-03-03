package entities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Paiement {

    private int idPayment;
    private String numeroCarteBancaire = new String();
    private String ccv;
    private LocalDate expirationDate=LocalDate.now();
    private LocalDate datePayment = LocalDate.now();
    private LocalTime hourPayment = LocalTime.now();
    private int userId=1;
    private String PromoCode;

    private int PostalCode ;
    private LocalDate dateDebutAbonnement=LocalDate.now();
    private LocalDate dateFinAbonnement=LocalDate.now();
    private double price;

    public Paiement(int idPayment, String numeroCarteBancaire, String ccv, LocalDate expirationDate, LocalDate datePayment, LocalTime hourPayment, int userId, String promoCode, int postalCode, LocalDate dateDebutAbonnement, LocalDate dateFinAbonnement, double price) {
        this.idPayment = idPayment;
        this.numeroCarteBancaire = numeroCarteBancaire;
        this.ccv = ccv;
        this.expirationDate = expirationDate;
        this.datePayment = datePayment;
        this.hourPayment = hourPayment;
        this.userId = userId;
        PromoCode = promoCode;
        this.PostalCode = postalCode;
        this.dateDebutAbonnement = dateDebutAbonnement;
        this.dateFinAbonnement = dateFinAbonnement;
        this.price = price;
    }


    public Paiement(int idPayment, String numeroCarteBancaire, String ccv, LocalDate expirationDate, LocalDate datePayment, LocalTime hourPayment, int userId, String PromoCode, int PostalCode) {
        this.idPayment = idPayment;
        this.numeroCarteBancaire = numeroCarteBancaire;
        this.ccv = ccv;
        this.expirationDate = expirationDate;
        this.datePayment = datePayment;
        this.hourPayment = hourPayment;
        this.userId = userId;
        this.PromoCode = PromoCode ;
        this.PostalCode = PostalCode ;
    }
    public Paiement(String numeroCarteBancaire, String ccv, LocalDate expirationDate, LocalDate datePayment, LocalTime hourPayment, String promoCode) {
        this.numeroCarteBancaire = numeroCarteBancaire;
        this.ccv = ccv;
        this.expirationDate = expirationDate;
        this.datePayment = datePayment;
        this.hourPayment = hourPayment;
        this.PromoCode = promoCode;
    }
    public Paiement(){}

    public Paiement(String cardNumber, String ccv, LocalDate expiration, String promocode, int postalCode) {
   this.numeroCarteBancaire = cardNumber ;
   this.ccv= ccv ;
   this.expirationDate = expiration ;
   this.PromoCode = promocode ;
   this.PostalCode = postalCode ;
    }

    public Paiement(String numeroCarteBancaire, String ccv, LocalDate expiration, String promocode, int postalCode, LocalDate dateDebut, LocalDate dateFin, Double price) {
        this.numeroCarteBancaire = numeroCarteBancaire ;
        this.ccv= ccv ;
        this.expirationDate = expiration ;
        this.PromoCode = promocode ;
        this.PostalCode = postalCode;
        this.dateDebutAbonnement = dateDebut;
        this.dateFinAbonnement = dateFin;
        this.price = price;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public String getNumeroCarteBancaire() {
        return numeroCarteBancaire;
    }

    public void setNumeroCarteBancaire(String numeroCarteBancaire) {
        this.numeroCarteBancaire = numeroCarteBancaire;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(LocalDate datePayment) {
        this.datePayment = datePayment;
    }

    public LocalTime getHourPayment() {
        return hourPayment;
    }

    public void setHourPayment(LocalTime hourPayment) {
        this.hourPayment = hourPayment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPromoCode(){return PromoCode ;}

    public void setPromoCode(String PromoCode) {
        this.PromoCode=PromoCode ;
    }

    public int getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(int postalCode) {
        PostalCode = postalCode;
    }

    public LocalDate getDateDebutAbonnement() {
        return dateDebutAbonnement;
    }

    public LocalDate getDateFinAbonnement() {
        return dateFinAbonnement;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paiement paiement = (Paiement) o;
        return (idPayment == paiement.idPayment) &&
                (userId == paiement.userId) &&
                Objects.equals(numeroCarteBancaire, paiement.numeroCarteBancaire) &&
                Objects.equals(ccv, paiement.ccv) &&
                Objects.equals(expirationDate, paiement.expirationDate) &&
                Objects.equals(datePayment, paiement.datePayment) &&
                Objects.equals(hourPayment, paiement.hourPayment) &&
                Objects.equals(PromoCode, paiement.PromoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPayment, numeroCarteBancaire, ccv, expirationDate, datePayment, hourPayment, userId,PromoCode);
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPayment=" + idPayment +
                ", numeroCarteBancaire='" + numeroCarteBancaire + '\'' +
                ", ccv='" + ccv + '\'' +
                ", expirationDate=" + expirationDate +
                ", datePayment=" + datePayment +
                ", hourPayment=" + hourPayment +
                ", userId=" + userId +
                ", PromoCode=" + PromoCode;
    }


}
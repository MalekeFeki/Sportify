package entities;

import java.time.LocalDate;
import java.util.Objects;

public class Adhesion {
    private  int adhesionId ;
    private int userId = 1;
    private String description = "Abonnement pour les gens qui aimes les défis";
    private String name = "Abonnement Gold";
    private double price;
    private int gymId =1 ;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public Adhesion(int adhesionId, int userId, String description, String name, double price, int gymId) {
        this.adhesionId = adhesionId;
        this.userId = userId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.gymId = gymId;
    }

    public Adhesion(int adhesionId, int userId, String description, String name, double price, int gymId, LocalDate dateDebut, LocalDate dateFin) {
        this.adhesionId = adhesionId;
        this.userId = userId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.gymId = gymId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Adhesion(LocalDate dateDebut, LocalDate dateFin,double price) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.price = price;
    }

    public Adhesion(String description, String name, double price, LocalDate dateDebut, LocalDate dateFin) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Adhesion(int adhesionId, int userId, String description, String name, double price, int gymId, LocalDate dateDebut) {
        this.adhesionId = adhesionId;
        this.userId = userId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.gymId = gymId;
        this.dateDebut = dateDebut;
    }


    public int getAdhesionId() {
        return adhesionId;
    }

    public void setAdhesionId(int adhesionId) {
        this.adhesionId = adhesionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Adhesion{" +
                "adhesionId=" + adhesionId +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", gymId=" + gymId +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adhesion adhesion = (Adhesion) o;
        return adhesionId == adhesion.adhesionId && userId == adhesion.userId && Double.compare(price, adhesion.price) == 0 && gymId == adhesion.gymId && Objects.equals(description, adhesion.description) && Objects.equals(name, adhesion.name) && Objects.equals(dateDebut, adhesion.dateDebut) && Objects.equals(dateFin, adhesion.dateFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adhesionId, userId, description, name, price, gymId, dateDebut, dateFin);
    }
}

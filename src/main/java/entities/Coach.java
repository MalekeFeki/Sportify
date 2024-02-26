package entities;


import entities.enums.Seance;
import entities.enums.Sexe;

public class Coach {

    private String nom;
    private String prenom;
    private String description;
    private Sexe sexe;
    private Seance seance;



    public Coach(String nom, String prenom, String description, Sexe sexe, Seance seance) {
        this.nom = nom;
        this.prenom = prenom;
        this.description = description;
        this.sexe = sexe;
        this.seance = seance;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", description='" + description + '\'' +
                ", sexe=" + sexe +
                ", seance=" + seance +
                '}';
    }

   /* public enum Sexe {
        HOMME, FEMME
    }*/

   /* public enum Seance {
        a, b, c, d
    }*/
}

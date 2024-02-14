package entities;

import java.util.Objects;

public class Utilisateur {
    private int idC;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;

    public Utilisateur() {
    }

    public Utilisateur(int idC, String nom, String prenom, String email, String mdp) {
        this.idC = idC;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        if (idC != that.idC) return false;
        if (!Objects.equals(nom, that.nom)) return false;
        if (!Objects.equals(prenom, that.prenom)) return false;
        if (!Objects.equals(email, that.email)) return false;
        return Objects.equals(mdp, that.mdp);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idC=" + idC +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = idC;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mdp != null ? mdp.hashCode() : 0);
        return result;
    }
}

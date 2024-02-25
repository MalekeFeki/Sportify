package entities;

import entities.enums.Role;

import java.util.Objects;

public class Utilisateur {
    private int id;
    private int cin;
    private int num_tel;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private Role role;

    public Utilisateur() {
    }

    public Utilisateur(int id, int cin, int num_tel, String nom, String prenom, String email, String mdp,Role role) {
        this.id = id;
        this.cin=cin;
        this.num_tel=num_tel;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role=role;
    }
    public Utilisateur(int cin, int num_tel,String nom, String prenom, String email, String mdp,Role role) {
        this.cin=cin;
        this.num_tel=num_tel;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role=role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        // Vérifier si l'email est au format email kifeh?
            this.email = email;

    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        {
            this.mdp = mdp;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        if (id != that.id) return false;
        return cin == that.cin;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + cin;
        return result;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idC=" + id +
                ", cin=" + cin +
                ", num_tel=" + num_tel +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role=" + role +
                '}';
    }
}

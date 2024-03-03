package entities;

import entities.enums.TypeDifficulty;

public class Challenge {

    private int idC;
    private String nom;
    private TypeDifficulty difficulty;
    private String description;

    public Challenge(int idC,String nom, TypeDifficulty difficulty, String description) {
        this.nom=nom;
        this.idC = idC;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Challenge() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public TypeDifficulty getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
            return description;
    }

    public void setDifficulty(TypeDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "idC=" + idC +
                ", nom='" + nom + '\'' +
                ", difficulty=" + difficulty +
                ", description='" + description + '\'' +
                '}';
    }
}

package entities;

import entities.enums.TypeDifficulty;

public class Challenge {

    private int idC;
    private TypeDifficulty difficulty;
    private String description;

    public Challenge(TypeDifficulty difficulty, String description) {
        this.idC = idC;
        this.difficulty = difficulty;
        this.description = description;
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
                ", difficulty=" + difficulty +
                ", description='" + description + '\'' +
                '}';
    }
}

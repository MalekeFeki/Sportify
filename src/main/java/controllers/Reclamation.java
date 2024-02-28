package controllers;

import javafx.scene.text.Text;

public class Reclamation {
    private int id_reclamation;
    private String texte;

    public Reclamation(int texte_reclamation, String texte) {
        this.id_reclamation = id_reclamation;
        this.texte = texte;
    }

    public Reclamation(String texteReclamation) {
        this.texte = texteReclamation;
    }

    public int getIdReclamation() {
        return id_reclamation;
    }

    public void setIdReclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getTexteReclamation() {
        return texte;

    }
}

package entities;

import java.util.Date;


public class Seance {
    private int idSeance;
    private String nomSeance;
    private int debut;
    private int fin;
    private Date dateS;

    public Seance() {
    }

    public Seance(int idSeance,String nomSeance, int debut, int fin, Date dateS) {
        this.idSeance = idSeance;
        this.nomSeance = nomSeance;
        this.debut = debut;
        this.fin=fin;
        this.dateS = dateS;
    }

    public Seance(String nomSeance, int debut, int fin, Date dateS) {
        this.nomSeance = nomSeance;
        this.debut = debut;
        this.fin=fin;
        this.dateS = dateS;
    }


    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public String getNomSeance() {
        return nomSeance;
    }

    public void setNomSeance(String nomSeance) {
        this.nomSeance = nomSeance;
    }

    public String getDebut() {
        return String.valueOf(debut);
    }

    public void setDebut(int debut) {
        this.debut = debut;
    }

    public String getFin() {
        return String.valueOf(fin);
    }

    public java.sql.Date getDateS() {
        return (java.sql.Date) dateS;
    }

    public void setDateS(Date dateS) {
        this.dateS = dateS;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "Id=" + idSeance +
                ", Début=" + debut +
                ", Fin=" + fin +
                ", Date=" + dateS;
    }
}

package entities;

import java.util.HashSet;
import java.util.Set;

public class Salle {
    private int idS;
    private String nomS;
    private String adresse;
    private String region;

    Set<String> options = new HashSet<>();

    public Salle() {
        // Default constructor
    }

    public Salle(String nomS, String adresse, String region, Set<String> options) {
        this.nomS = nomS;
        this.adresse = adresse;
        this.region = region;
        this.options = new HashSet<>(options);


    }



    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
    }

    public String getNomS() {
        return nomS;
    }

    public void setNomS(String nomS) {
        this.nomS = nomS;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<String> getOptions() {
        return options;
    }

    public void setOptions(Set<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Salle De Sport{" +
                "idS=" + idS +
                ", nom=" + nomS +
                ", adresse=" + adresse +
                ", region='" + region + '\'';
    }
}

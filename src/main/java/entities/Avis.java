package entities;
import entities.enums.TypeAvis;

import java.util.ArrayList;

public class Avis {

    private int idA;

    private TypeAvis type;

    private String description;

    public Avis(){

    }

    public Avis(int idA,TypeAvis type,String description){
        this.idA=idA;
        this.type=type;
        this.description=description;
    }
    public int getIdA() {
        return idA;
    }
    public String getDescription() {
        return description;
    }
    public TypeAvis getType() {
        return type;
    }

    public void setType(TypeAvis type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdA(int idA) {
        this.idA = idA;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "idA=" + idA +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }


}

package services;

import java.util.List;

public interface IEventReservationCrud<T> {

    public boolean ajouterReservation(T u);
    public List<T> afficherReservation(int id);
}

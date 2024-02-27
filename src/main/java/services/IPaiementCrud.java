package services;

import entities.Paiement;

import java.util.List;

public interface IPaiementCrud<T> {
    void create(Paiement p);
    List<Paiement> read();
    void update(Paiement p);
    void delete(int idPayment, int userId);
}

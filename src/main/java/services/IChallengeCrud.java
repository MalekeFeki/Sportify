package services;

import entities.Challenge;

import java.util.List;

public interface IChallengeCrud<T> {
    void ajouterChallenge(T entity);

    List<T> afficherChallenges();

    void modifierChallenge(T entity);

    void supprimerChallenge(int id);
}

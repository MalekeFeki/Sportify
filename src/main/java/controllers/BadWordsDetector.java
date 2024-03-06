package controllers;

import java.util.ArrayList;
import java.util.List;

public class BadWordsDetector {
    // Liste de mots inappropriés
    private static final List<String> BAD_WORDS = List.of("badword1", "badword2", "badword3");

    // Méthode pour vérifier si une chaîne contient un mot inapproprié
    public static boolean containsBadWord(String text) {
        // Convertir le texte en minuscules pour éviter les correspondances insensibles à la casse
        text = text.toLowerCase();

        // Vérifier si le texte contient un mot inapproprié
        for (String word : BAD_WORDS) {
            if (text.contains(word)) {
                return true; // Mot inapproprié trouvé
            }
        }

        return false; // Aucun mot inapproprié trouvé
    }

    // Méthode pour obtenir une liste de mots inappropriés trouvés dans une chaîne
    public static List<String> getBadWords(String text) {
        List<String> foundBadWords = new ArrayList<>();
        text = text.toLowerCase();

        for (String word : BAD_WORDS) {
            if (text.contains(word)) {
                foundBadWords.add(word);
            }
        }

        return foundBadWords;
    }
}
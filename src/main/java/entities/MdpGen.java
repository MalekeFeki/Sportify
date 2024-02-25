package entities;

import java.util.Random;

public class MdpGen {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String genererMdp() {
        Random random = new Random();
        StringBuilder mdpBuilder = new StringBuilder();

        // Ajoutez une majuscule au mot de passe généré
        mdpBuilder.append(CHARACTERS.charAt(random.nextInt(26)));

        // Génère les caractères restants du mot de passe
        for (int i = 1; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            mdpBuilder.append(CHARACTERS.charAt(index));
        }

        return mdpBuilder.toString();
    }
}

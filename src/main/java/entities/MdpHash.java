package entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MdpHash {
    public static String hashPassword(String password) {
        try {
            // Créer une instance de l'algorithme de hachage MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Appliquer le hachage au mot de passe
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convertir les bytes hachés en format hexadécimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String password = "monMotDePasse";
        String hashedPassword = hashPassword(password);
        System.out.println("Mot de passe haché : " + hashedPassword);
    }
}

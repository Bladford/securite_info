package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Exercice3 {
    static String hash = "5a74dd4eef347734c8a0a9a3188abd11";

    public static void main() {
        menu();
    }

    public static void menu() {
        try {
            findPassword();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithme de hachage non supporté : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    public static void findPassword() throws NoSuchAlgorithmException, IOException {
        // Charger le fichier de mots de passe
        List<String> passwords = Files.readAllLines(Paths.get("src/main/resources/rockyou.txt"), StandardCharsets.UTF_8);

        MessageDigest md = MessageDigest.getInstance("MD5");

        long startTime = System.currentTimeMillis(); // Enregistre le temps de début
        int passwordCount = 0; // Compteur pour le nombre de mots de passe testés

        for (String password : passwords) {
            md.reset();
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String myHash = bytesToHex(digest);
            passwordCount++; // Incrémente le compteur à chaque mot de passe testé
            if (myHash.equalsIgnoreCase(hash)) {
                long endTime = System.currentTimeMillis(); // Enregistre le temps de fin
                System.out.println("Le mot de passe est : " + password);
                System.out.println("Nombre de mots de passe testés : " + passwordCount);
                System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
                return;
            }
        }
        long endTime = System.currentTimeMillis(); // Enregistre le temps de fin si le mot de passe n'est pas trouvé
        System.out.println("Mot de passe non trouvé");
        System.out.println("Nombre de mots de passe testés : " + passwordCount);
        System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

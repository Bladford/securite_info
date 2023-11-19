package org.example;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.lang.Thread.sleep;

public class Exercice3 {
    static String hash = "5a74dd4eef347734c8a0a9a3188abd11";

    public static void main(String[] args) {
        // Dézippe le fichier src/main/resources/rockyou.zip
        try {
            ZipFile zipFile = new ZipFile("src/main/resources/rockyou.zip");
            zipFile.extractAll("src/main/resources/");

        } catch (ZipException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la décompression du fichier rockyou.zip");
            return; // Arrête l'exécution si une erreur se produit lors de la décompression
        }

        try {
            findPassword();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithme de hachage non supporté : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        // Supprime le fichier rockyou.txt
        File file = new File("src/main/resources/rockyou.txt");
        if (!file.delete()) {
            System.err.println("Erreur lors de la suppression du fichier rockyou.txt");
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
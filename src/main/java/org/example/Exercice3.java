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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Exercice3 {
    static String hash = "5a74dd4eef347734c8a0a9a3188abd11";
    static AtomicBoolean found = new AtomicBoolean(false);

    public static void main(String[] args) {
        // Dézippe le fichier src/main/resources/rockyou.zip
        try {
            ZipFile zipFile = new ZipFile("src/main/resources/rockyou.zip");
            zipFile.extractAll("src/main/resources/");
        } catch (ZipException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la décompression du fichier rockyou.zip");
            return;
        }

        int numberOfThreads = 8; // Nombre de threads à utiliser
        try {
            findPassword(numberOfThreads);
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

    public static void findPassword(int numberOfThreads) throws NoSuchAlgorithmException, IOException {
        List<String> passwords = Files.readAllLines(Paths.get("src/main/resources/rockyou.txt"), StandardCharsets.UTF_8);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executor.submit(() -> {
                try {
                    processPasswords(passwords, numberOfThreads, finalI, startTime);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Attendre que tous les threads terminent
        }

        if (!found.get()) {
            long endTime = System.currentTimeMillis();
            System.out.println("Mot de passe non trouvé");
            System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
        }
    }

    private static void processPasswords(List<String> passwords, int numberOfThreads, int threadIndex, long startTime) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        int passwordCount = 0;

        for (int i = threadIndex; i < passwords.size(); i += numberOfThreads) {
            if (found.get()) {
                return; // Si un autre thread a trouvé le mot de passe
            }

            String password = passwords.get(i);
            md.reset();
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String myHash = bytesToHex(digest);
            passwordCount++;

            if (myHash.equalsIgnoreCase(hash)) {
                found.set(true);
                long endTime = System.currentTimeMillis();
                System.out.println("Le mot de passe est : " + password);
                System.out.println("Nombre de mots de passe testés : " + passwordCount);
                System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
                return;
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

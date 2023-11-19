package org.example;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.util.List;

public class Exercice4 {

    private static final String SOURCE_ZIP_FILE_PATH = "src/main/resources/archive.zip";
    private static final String DESTINATION_FOLDER_PATH = "src/main/resources/";

    public static void main(String[] args) {
        casserArchive();
    }

    private static void casserArchive() {
        long startTime = System.currentTimeMillis();
        StringBuilder motDePasse = new StringBuilder("a");
        int compteur = 0;
        boolean archiveBroken = false;
        // Tant que l'archive n'est pas cassée
        while (!archiveBroken) {
            try {
                decompressZip(motDePasse.toString());
                System.out.println("Le mot de passe est : " + motDePasse);
                archiveBroken = true;
            } catch (ZipException e) {
                incrementPassword(motDePasse);
                compteur++;
            }
        }
        // Affichage des statistiques
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'exécution : " + (endTime - startTime) + " ms");
        System.out.println("Nombre de mots de passe testés : " + compteur);
    }

    // Incrémente le mot de passe
    private static void incrementPassword(StringBuilder motDePasse) {
        int index = motDePasse.length() - 1;
        // Tant que le caractère à l'index est un 'z'
        while (index >= 0 && motDePasse.charAt(index) == 'z') {
            motDePasse.setCharAt(index, 'a');
            index--;
        }
        if (index < 0) {
            motDePasse.insert(0, 'a');
        } else {
            motDePasse.setCharAt(index, (char) (motDePasse.charAt(index) + 1));
        }
    }

    // Décompresse l'archive avec le mot de passe donné
    private static void decompressZip(String password) throws ZipException {
        ZipFile zipFile = new ZipFile(SOURCE_ZIP_FILE_PATH, password.toCharArray());
        // Si l'archive est chiffrée
        if (zipFile.isEncrypted()) {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                zipFile.extractFile(fileHeader, DESTINATION_FOLDER_PATH);
            }
        } else {
            zipFile.extractAll(DESTINATION_FOLDER_PATH);
        }
        System.out.println("Décompression terminée !");
    }
}

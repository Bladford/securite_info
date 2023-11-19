package org.example;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Exercice6 {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Choisissez une option :");
            System.out.println("1 - Décoder l'image simple");
            System.out.println("2 - Décoder l'image hardcore (décodera aussi l'image simple)");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        decodeSimple();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        decodeSimple();
                        decodeHardcore();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                default:
                    System.out.println("Option invalide");
                    break;
            }
        }
    }

    private static void decodeSimple() throws IOException {
        System.out.println("Déchiffrement de l'image simple...");
        decodeFile("src/main/resources/encrypted_file_simple.jpg", "src/main/resources/decrypted_file_simple.jpg");
        System.out.println("Image simple déchiffrée.");
    }

    private static void decodeHardcore() throws IOException {
        System.out.println("Déchiffrement de l'image hardcore...");
        String decryptedFile = "src/main/resources/decrypted_file_simple.jpg";
        String encryptedFile = "src/main/resources/encrypted_file_hard.jpg";
        String outputFile = "src/main/resources/decrypted_file_hard.jpg";

        // Lire les fichiers
        byte[] decryptedData = readFile(decryptedFile);
        byte[] encryptedData = readFile(encryptedFile);

        // Récupérer la clé XOR à partir des premiers octets
        byte[] key = getXORKey(decryptedData, encryptedData);

        // Déchiffrer le fichier
        byte[] decryptedHard = xorWithKey(encryptedData, key);

        // Écrire le fichier déchiffré
        writeFile(outputFile, decryptedHard);

        System.out.println("Image hardcore déchiffrée.");
    }

    // Méthodes auxiliaires (readFile, writeFile, getXORKey, xorWithKey, decodeFile) ici...

    public static void decodeFile(String fromFileName, String toFileName) throws IOException {
        try (FileInputStream fileInput = new FileInputStream(fromFileName);
             FileOutputStream fileOutput = new FileOutputStream(toFileName)) {

            // Lire la première valeur pour calculer la clé
            int tmp = fileInput.read();
            int key = tmp ^ 255;

            // Réinitialiser le pointeur de fichier au début
            fileInput.getChannel().position(0);

            // Lire et écrire en appliquant XOR avec la clé
            while ((tmp = fileInput.read()) != -1) {
                fileOutput.write(tmp ^ key);
            }
        }
    }

    private static byte[] readFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return data;
    }

    private static void writeFile(String path, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data);
        fos.close();
    }

    private static byte[] getXORKey(byte[] decrypted, byte[] encrypted) {
        byte[] key = new byte[decrypted.length];
        for (int i = 0; i < decrypted.length; i++) {
            key[i] = (byte) (decrypted[i] ^ encrypted[i]);
        }
        return key;
    }

    private static byte[] xorWithKey(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return result;
    }
}

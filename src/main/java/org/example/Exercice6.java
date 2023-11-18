package org.example;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exercice6 {

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

    public static void main(String[] args) {
        try {
            decodeFile("src/main/resources/encrypted_file_simple.jpg", "src/main/resources/decrypted_file_simple.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

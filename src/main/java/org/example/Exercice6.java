package org.example;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Exercice6 {

    public static void main(String[] args) {
        String encryptedFilePath = "ress/encrypted_file_hard.jpg";
        try {
                    analyzeFrequency(encryptedFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private static void analyzeFrequency(String filePath) throws IOException {
                Map<Byte, Integer> frequencyMap = new HashMap<>();

                try (FileInputStream fileStream = new FileInputStream(filePath)) {
                    int b;
                    while ((b = fileStream.read()) != -1) {
                        byte byteValue = (byte) b;
                        frequencyMap.put(byteValue, frequencyMap.getOrDefault(byteValue, 0) + 1);
                    }
                }

                // Afficher ou analyser la fréquence des octets
                for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
                    System.out.println("Octet: " + entry.getKey() + ", Fréquence: " + entry.getValue());
                }

                // Ici, vous pouvez ajouter une logique supplémentaire pour comparer
                // cette fréquence avec celle d'un fichier JPEG non chiffré.
            }
        }

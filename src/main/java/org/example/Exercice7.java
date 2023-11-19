package org.example;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Exercice7 {

    private static final String HOST = "51.195.253.124";
    private static final int PORT = 11111;

    public static void main(String[] args) {
        // Charger le texte chiffré depuis le fichier
        byte[] ciphertext = loadCiphertextFromFile("src/main/resources/cbc_ciphertext");

        // Découper le texte chiffré en blocs (IV, C1, C2)
        byte[] iv = Arrays.copyOfRange(ciphertext, 0, 16);
        byte[] c1 = Arrays.copyOfRange(ciphertext, 16, 32);
        byte[] c2 = Arrays.copyOfRange(ciphertext, 32, 48);

        // Décrypter les blocs
        byte[] p2;
        byte[] p1;
        try {
            p2 = decryptBlock(c2, c1);
            p1 = decryptBlock(c1, iv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Afficher le message déchiffré
        System.out.println("Message déchiffré: " + new String(p1) + new String(p2));
    }

    private static byte[] decryptBlock(byte[] cipherBlock, byte[] previousBlock) throws IOException {
        byte[] decryptedBlock = new byte[cipherBlock.length];
        byte[] tempBlock = Arrays.copyOf(previousBlock, previousBlock.length);

        for (int i = cipherBlock.length - 1; i >= 0; i--) {
            for (int guess = 0; guess < 256; guess++) {
                tempBlock[i] = (byte) (guess ^ (cipherBlock.length - i));
                if (isPaddingCorrect(concatenate(tempBlock, cipherBlock))) {
                    decryptedBlock[i] = (byte) (guess ^ previousBlock[i]);
                    break;
                }
            }
        }
        return decryptedBlock;
    }

    private static byte[] concatenate(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }


    private static byte[] loadCiphertextFromFile(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    private static boolean isPaddingCorrect(byte[] modifiedBlock) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {
            out.write(modifiedBlock);
            out.flush();
            byte[] response = new byte[1];
            in.read(response);

            // Convertir le byte en une représentation lisible
            //System.out.println("Padding correct: " + Arrays.toString(response));
            return response[0] == 1; // Retourner true si le padding est correct
        }
    }


}

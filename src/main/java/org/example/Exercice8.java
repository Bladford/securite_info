package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.ArrayList;

public class Exercice8{
    public static void main(String[] args) throws Exception {
        final String host = "51.195.253.124";
        final int port = 4321;
        ArrayList<HashSet<Character>> missingChars = new ArrayList<>();

        for (int i = 0; i < 200; i++) { // Nombre de tentatives
            try (Socket socket = new Socket(host, port);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                // Lire la demande de message du serveur
                String encryptedMessage = reader.readLine();
                adjustMissingCharsList(missingChars, encryptedMessage.length());
                // Envoyer le message au serveur
                for (int j = 0; j < encryptedMessage.length(); j++) {
                    missingChars.get(j).remove(encryptedMessage.charAt(j));
                }
            }
        }
        // Afficher le message secret estimé
        StringBuilder secretMessage = new StringBuilder();
        // Pour chaque caractère du message
        for (HashSet<Character> set : missingChars) {
            if (set.size() == 1) {
                secretMessage.append(set.iterator().next());
            } else {
                secretMessage.append("?"); // Caractère inconnu
            }
        }
        System.out.println("Message secret estimé : " + secretMessage);
    }

    // Méthode d'ajustement de la liste des caractères manquants
    private static void adjustMissingCharsList(ArrayList<HashSet<Character>> missingChars, int newLength) {
        // Pour chaque caractère manquant
        for (int i = missingChars.size(); i < newLength; i++) {
            HashSet<Character> charSet = new HashSet<>();
            // Pour chaque caractère de A à Z
            for (char c = 'A'; c <= 'Z'; c++) {
                charSet.add(c);
            }
            // Ajouter le caractère manquant à la liste
            missingChars.add(charSet);
        }
    }
}

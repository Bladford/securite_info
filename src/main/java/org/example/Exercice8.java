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

                String encryptedMessage = reader.readLine();
                adjustMissingCharsList(missingChars, encryptedMessage.length());

                for (int j = 0; j < encryptedMessage.length(); j++) {
                    missingChars.get(j).remove(encryptedMessage.charAt(j));
                }
            }
        }

        StringBuilder secretMessage = new StringBuilder();
        for (HashSet<Character> set : missingChars) {
            if (set.size() == 1) {
                secretMessage.append(set.iterator().next());
            } else {
                secretMessage.append("?"); // Caractère inconnu
            }
        }

        System.out.println("Message secret estimé : " + secretMessage);
    }

    private static void adjustMissingCharsList(ArrayList<HashSet<Character>> missingChars, int newLength) {
        for (int i = missingChars.size(); i < newLength; i++) {
            HashSet<Character> charSet = new HashSet<>();
            for (char c = 'A'; c <= 'Z'; c++) {
                charSet.add(c);
            }
            missingChars.add(charSet);
        }
    }
}

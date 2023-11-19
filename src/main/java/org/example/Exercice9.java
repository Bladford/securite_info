package org.example;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercice9 {

    private static BigInteger moduleAlice;
    private static BigInteger moduleBob;
    private static final int NUM_THREADS = 200; // Nombre de threads à utiliser

    public static void main(String[] args) {
        try {
            // Lire les modules depuis les fichiers
            System.out.println("Lecture des modules RSA...");
            moduleAlice = new BigInteger(Files.readAllBytes(Paths.get("src/main/resources/pk_alice")));
            moduleBob = new BigInteger(Files.readAllBytes(Paths.get("src/main/resources/pk_bob")));

            System.out.println("Module Alice: " + moduleAlice);
            System.out.println("Module Bob: " + moduleBob);

            // Utiliser le multithreading pour accélérer le processus
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

            BigInteger maxLimit = moduleAlice.sqrt().add(BigInteger.ONE);
            BigInteger range = maxLimit.divide(BigInteger.valueOf(NUM_THREADS));

            System.out.println("Début de la factorisation en utilisant " + NUM_THREADS + " threads...");

            for (int i = 0; i < NUM_THREADS; i++) {
                // Calculer les bornes de recherche pour chaque thread
                BigInteger start = range.multiply(BigInteger.valueOf(i)).add(BigInteger.valueOf(2));
                BigInteger end = start.add(range);

                executor.submit(new FactorFinder(start, end, moduleAlice, moduleBob));
            }

            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                System.out.println("La factorisation n'a pas abouti dans le temps imparti.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class FactorFinder implements Runnable {
        private final BigInteger start;
        private final BigInteger end;
        private final BigInteger moduleAlice;
        private final BigInteger moduleBob;

        public FactorFinder(BigInteger start, BigInteger end, BigInteger moduleAlice, BigInteger moduleBob) {
            this.start = start;
            this.end = end;
            this.moduleAlice = moduleAlice;
            this.moduleBob = moduleBob;
        }

        @Override
        public void run() {
            for (BigInteger num = start; num.compareTo(end) < 0; num = num.add(BigInteger.ONE)) {
                if (moduleAlice.mod(num).equals(BigInteger.ZERO) && moduleBob.mod(num).equals(BigInteger.ZERO)) {
                    System.out.println("Facteur commun trouvé : " + num);
                }
            }
        }
    }
}

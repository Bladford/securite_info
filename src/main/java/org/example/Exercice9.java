package org.example;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercice9 {

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return a;
        return gcd(b, a.mod(b));
    }

    public static void main(String[] args) {
        try {
            // Lire les modules RSA à partir des fichiers
            String aliceModulusStr = new String(Files.readAllBytes(Paths.get("src/main/resources/pk_alice")));
            String bobModulusStr = new String(Files.readAllBytes(Paths.get("src/main/resources/pk_bob")));

            BigInteger modulusAlice = new BigInteger(aliceModulusStr.trim());
            BigInteger modulusBob = new BigInteger(bobModulusStr.trim());
            System.out.println("Alice modulus: " + modulusAlice);
            System.out.println("Bob modulus: " + modulusBob);
            // Calculer le PGCD des deux modules
            BigInteger gcd = gcd(modulusAlice, modulusBob);

            // Si le PGCD est plus grand que 1, alors ils ont un nombre premier commun
            if (gcd.compareTo(BigInteger.ONE) > 0) {
                System.out.println("Nombre premier commun trouvé : " + gcd);
                // Trouver l'autre nombre premier pour chaque module
                BigInteger pAlice = gcd;
                BigInteger qAlice = modulusAlice.divide(pAlice);
                BigInteger pBob = gcd;
                BigInteger qBob = modulusBob.divide(pBob);

                System.out.println("Nombres premiers pour Alice: p = " + pAlice + ",\n q = " + qAlice);
                System.out.println("Nombres premiers pour Bob: p = " + pBob + ",\n q = " + qBob);
            } else {
                System.out.println("Aucun nombre premier commun trouvé.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.example;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Exercice9 {
    public static void main(String[] args) {
        try {
            String pathAlice = "src/main/resources/pk_alice";
            String pathBob = "src/main/resources/pk_alice";

            String moduleAlice = new String(Files.readAllBytes(Paths.get(pathAlice))).trim();
            String moduleBob = new String(Files.readAllBytes(Paths.get(pathBob))).trim();

            BigInteger nAlice = new BigInteger(moduleAlice);
            BigInteger nBob = new BigInteger(moduleBob);

            System.out.println("Factorisation des modules...");
            BigInteger[] factorsAlice = factorize(nAlice);
            BigInteger[] factorsBob = factorize(nBob);

            System.out.println("Alice: p = " + factorsAlice[0] + ", q = " + factorsAlice[1]);
            System.out.println("Bob: p = " + factorsBob[0] + ", q = " + factorsBob[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigInteger[] factorize(BigInteger n) {
        BigInteger two = new BigInteger("2");

        if (n.mod(two).equals(BigInteger.ZERO)) {
            return new BigInteger[]{two, n.divide(two)};
        }

        BigInteger i = new BigInteger("3");
        while (i.multiply(i).compareTo(n) <= 0) {
            if (n.mod(i).equals(BigInteger.ZERO)) {
                return new BigInteger[]{i, n.divide(i)};
            }
            i = i.add(two);
        }
        return new BigInteger[]{n, BigInteger.ONE};
    }
}

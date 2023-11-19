package org.example;

import java.text.Normalizer;
import java.util.*;

public class Exercice2 {
    private static final Scanner scanner = new Scanner(System.in);
    private static double[] frequencesFrancais = {
            7.636, // a
            0.901, // b
            3.260, // c
            3.669, // d
            14.715, // e
            1.066, // f
            0.866, // g
            0.737, // h
            7.529, // i
            0.613, // j
            0.074, // k
            5.456, // l
            2.968, // m
            7.095, // n
            5.796, // o
            2.521, // p
            1.362, // q
            6.693, // r
            7.948, // s
            7.244, // t
            6.311, // u
            1.838, // v
            0.049, // w
            0.427, // x
            0.128, // y
            0.326  // z
    };

    public static void main(String[] args) {
        while (true) {
            System.out.println("Que voulez-vous faire ?\n1. Chiffrer\n2. Dechiffrer\n3. Trouver Longueur de Clé\n4. Casser le code\n5. Quitter");
            int choixSaisi = scanner.nextInt();
            scanner.nextLine(); // Consommer le reste de la ligne

            switch (choixSaisi) {
                case 1:
                    chiffrer();
                    break;
                case 2:
                    dechiffrer();
                    break;
                case 3:
                    System.out.println("Veuillez saisir une chaîne de caractères :");
                    String chaine = suppressionIndesirable(scanner.nextLine().toLowerCase());
                    tailleCle(chaine);
                    break;
                case 4:
                    casserLeCode();
                    break;
                case 5:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Veuillez saisir 1, 2, 3, 4 ou 5");
                    break;
            }
        }
    }

    //Méthode pour casser le code
    protected static void casserLeCode() {
        System.out.println("Veuillez saisir une chaîne de caractères :");
        String chaine = scanner.nextLine().toLowerCase();

        String chaineSansPonctuation = suppressionIndesirable(chaine);
        int longueurCle = tailleCle(chaineSansPonctuation);
        System.out.println("Longueur estimée de la clé : " + longueurCle);

        String cle = trouverCle(chaineSansPonctuation, longueurCle);
        System.out.println("Clé estimée : " + cle);

        String texteDechiffre = chiffrerDechiffrer(chaine, cle, false);
        System.out.println("Texte déchiffré : " + texteDechiffre);
    }

    //Méthode pour trouver la clé
    private static String trouverCle(String chaine, int longueurCle) {
        StringBuilder cle = new StringBuilder();
        // Pour chaque lettre de la clé
        for (int i = 0; i < longueurCle; i++) {
            StringBuilder sousTexte = new StringBuilder();
            // Extraire les lettres de la chaîne qui correspondent à la lettre de la clé
            for (int j = i; j < chaine.length(); j += longueurCle) {
                sousTexte.append(chaine.charAt(j));
            }
            // Trouver le décalage de la lettre de la clé
            int decalage = trouverDecalage(sousTexte.toString());
            char lettreCle = (char) ('a' + decalage);
            cle.append(lettreCle);
        }

        return cle.toString();
    }

    //Méthode pour trouver le décalage
    private static int trouverDecalage(String sousTexte) {
        int[] frequences = new int[26];
        // Compter les occurrences de chaque lettre
        for (char c : sousTexte.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                frequences[c - 'a']++;
            }
        }
        // Calculer la corrélation entre les fréquences de la langue française et les fréquences du texte
        int decalage = 0;
        double maxCorrelation = 0;
        // Pour chaque décalage possible
        for (int i = 0; i < 26; i++) {
            double correlation = 0;
            // Pour chaque lettre
            for (int j = 0; j < 26; j++) {
                correlation += frequencesFrancais[j] * frequences[(j + i) % 26];
            }
            if (correlation > maxCorrelation) {
                maxCorrelation = correlation;
                decalage = i;
            }
        }

        return decalage;
    }

    //Méthode pour trouver la taille de la clé
    private static int tailleCle(String chaine) {
        chaine = suppressionIndesirable(chaine.toLowerCase());
        Map<String, Integer> sousChaines = trouverSousChaines(chaine);
        Map<Integer, Integer> distances = calculerDistances(chaine, sousChaines);
        int longueurCle = trouverLongueurCle(distances);
        System.out.println("Longueur probable de la clé : " + longueurCle);
        return longueurCle;
    }

    //Méthode pour trouver les sous-chaînes
    private static Map<String, Integer> trouverSousChaines(String chaine) {
        Map<String, Integer> sousChaines = new HashMap<>();
        int tailleSousChaine = 3; // Vous pouvez ajuster cette valeur

        for (int i = 0; i < chaine.length() - tailleSousChaine + 1; i++) {
            String sousChaine = chaine.substring(i, i + tailleSousChaine);
            if (!sousChaines.containsKey(sousChaine)) {
                sousChaines.put(sousChaine, i);
            }
        }

        return sousChaines;
    }

    //Méthode pour calculer les distances
    private static Map<Integer, Integer> calculerDistances(String chaine, Map<String, Integer> sousChaines) {
        Map<Integer, Integer> distances = new HashMap<>();
        // Pour chaque sous-chaîne
        for (Map.Entry<String, Integer> entry : sousChaines.entrySet()) {
            String sousChaine = entry.getKey();
            int positionPrecedente = entry.getValue();
            int position = positionPrecedente;
            // Tant que cette sous-chaîne apparaît dans la chaîne
            while ((position = chaine.indexOf(sousChaine, position + 1)) != -1) {
                int distance = position - positionPrecedente;
                distances.put(distance, distances.getOrDefault(distance, 0) + 1);
                positionPrecedente = position;
            }
        }

        return distances;
    }

    //Micro class pour trouver la longueur de la clé
    private static class CleProbabilite {
        int longueurCle;
        double probabilite;

        //Constructeur
        public CleProbabilite(int longueurCle, double probabilite) {
            this.longueurCle = longueurCle;
            this.probabilite = probabilite;
        }
    }

    //Méthode pour trouver la longueur de la clé
    private static int trouverLongueurCle(Map<Integer, Integer> distances) {
        Map<Integer, Integer> diviseurs = new HashMap<>();
        int totalOccurrences = 0;

        // Calculer les occurrences de chaque diviseur
        for (int distance : distances.keySet()) {
            for (int diviseur = 1; diviseur <= distance; diviseur++) {
                if (distance % diviseur == 0) {
                    int occurrences = distances.get(distance);
                    diviseurs.put(diviseur, diviseurs.getOrDefault(diviseur, 0) + occurrences);
                    totalOccurrences += occurrences;
                }
            }
        }

        List<CleProbabilite> probabilites = new ArrayList<>();
        // Calculer les probabilités de chaque diviseur
        // Pour chaque diviseur
        for (Map.Entry<Integer, Integer> entry : diviseurs.entrySet()) {
            if (entry.getKey() > 2) { // Ignorer les clés de longueur 1 et 2
                double probability = (double) entry.getValue() / totalOccurrences * 100;
                probabilites.add(new CleProbabilite(entry.getKey(), probability));
            }
        }

        // Trier les probabilités en ordre décroissant
        probabilites.sort((a, b) -> Double.compare(b.probabilite, a.probabilite));

        // Afficher les trois ou quatre premières longueurs de clé avec les plus hautes probabilités
        int nombreDeClesAAfficher = 3; // ou 4, selon votre préférence
        System.out.println("Longueurs de clé les plus probables :");
        for (int i = 0; i < Math.min(nombreDeClesAAfficher, probabilites.size()); i++) {
            CleProbabilite cp = probabilites.get(i);
            System.out.printf("Longueur: %d, Probabilité: %.2f%%\n", cp.longueurCle, cp.probabilite);
        }

        // Retourner la longueur de la clé la plus probable
        return probabilites.get(0).longueurCle;
    }

    //Méthode pour supprimer les caractères indésirables
    private static String suppressionIndesirable(String texte) {
        //suprime tout ce qui n'est pas une lettre
        texte = texte.replaceAll("[^a-zA-Z]", "");
        return texte;
    }

    private static void dechiffrer() {
        System.out.println("Veuillez saisir une chaîne de caractères :");
        String chaine = supprimerAccents(scanner.nextLine().toLowerCase());
        System.out.println("Veuillez saisir la clé :");
        String cleSaisie = supprimerAccents(scanner.nextLine().toLowerCase());

        System.out.println("Chaîne déchiffrée : " + chiffrerDechiffrer(chaine, cleSaisie, false));
    }

    //Méthode pour chiffrer
    private static void chiffrer() {
        System.out.println("Veuillez saisir une chaîne de caractères :");
        String chaine = supprimerAccents(scanner.nextLine().toLowerCase());
        System.out.println("Veuillez saisir la clé :");
        String cleSaisie = supprimerAccents(scanner.nextLine().toLowerCase());

        System.out.println("Chaîne chiffrée : " + chiffrerDechiffrer(chaine, cleSaisie, true));
    }

    //Méthode pour chiffrer ou déchiffrer
    private static String chiffrerDechiffrer(String texte, String cle, boolean chiffrer) {
        StringBuilder resultat = new StringBuilder();
        int positionCle = 0;

        for (int i = 0; i < texte.length(); i++) {
            char caractere = texte.charAt(i);
            if (caractere >= 'a' && caractere <= 'z') {
                int decalage = cle.charAt(positionCle) - 'a';
                if (!chiffrer) {
                    decalage = -decalage;
                }
                char caractereChiffre = (char) ((caractere - 'a' + decalage + 26) % 26 + 'a');
                resultat.append(caractereChiffre);
                positionCle = (positionCle + 1) % cle.length();
            } else {
                resultat.append(caractere); // Conserver les espaces et la ponctuation
            }
        }

        return resultat.toString();
    }

    //Méthode pour supprimer les accents
    private static String supprimerAccents(String texte) {
        texte = Normalizer.normalize(texte, Normalizer.Form.NFD);
        texte = texte.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texte;
    }
}



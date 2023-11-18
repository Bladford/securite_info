package org.temp;

import java.io.*;
import java.util.Scanner;

public class Exercice1 {
    static void menu(){
        boolean continuer = true;
        while (continuer) {
            System.out.println("Que voulez-vous faire ?\n1. Chiffrer\n2. Déchiffrer\n3. Brute Force \n4. Retour au menu principal");
            Scanner choix = new Scanner(System.in);
            int choixSaisi = choix.nextInt();
            if (choixSaisi==1){
                chiffrer();
            } else if (choixSaisi==2){
                dechiffrer();
            } else if (choixSaisi==3) {
                try {
                    bruteForce();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if(choixSaisi==4){
                continuer=false;
            }else{
                System.out.println("Veuillez saisir 1 ou 2 ou 3 ou 4");

            }
        }

    }
    private static void dechiffrer() {
        //demande a l'utilisateur de saisir une chaine de caractères
        System.out.println("Veuillez saisir une chaine de caractères :");
        //création d'un objet de type Scanner
        Scanner chaineACoder = new Scanner(System.in);
        //récupération de la chaine saisie par l'utilisateur
        String chaine = chaineACoder.nextLine();
        chaine=chaine.toLowerCase();
        System.out.println("Veuillez saisir le nombre de décalage :");
        Scanner decalage = new Scanner(System.in);
        int decalageSaisi = decalage.nextInt();
        String chaineCodee = "";
        //parcours de la chaine de caractères
        for (int i = 0; i < chaine.length(); i++) {
            //récupération du caractère à la position i
            char caractere = chaine.charAt(i);
            //récupération du code ascii du caractère
            int ascii = (int) caractere;
            //vérification que le caractère est une lettre minuscule
            if (ascii >= 97 && ascii <= 122) {
                //ajout du décalage au code ascii
                ascii = ascii - decalageSaisi;
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii > 122) {
                    //si le code ascii est supérieur à 122, on le fait repartir à 97
                    ascii = ascii - 26;
                }
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii < 97) {
                    //si le code ascii est inférieur à 97, on le fait repartir à 122
                    ascii = ascii + 26;
                }
                //conversion du code ascii en caractère
                char caractereCode = (char) ascii;
                //ajout du caractère à la chaine codée
                chaineCodee = chaineCodee + caractereCode;
            } else {
                //si le caractère n'est pas une lettre minuscule, on l'ajoute à la chaine codée
                chaineCodee = chaineCodee + caractere;
            }
        }
        System.out.println("La chaine décodée est : " + chaineCodee);
    }

    static void chiffrer(){
        //demande a l'utilisateur de saisir une chaine de caractères
        System.out.println("Veuillez saisir une chaine de caractères :");
        //création d'un objet de type Scanner
        Scanner chaineACoder = new Scanner(System.in);
        //récupération de la chaine saisie par l'utilisateur
        String chaine = chaineACoder.nextLine();
        chaine=chaine.toLowerCase();
        //suprime les accents
        chaine = chaine.replace("é", "e");
        chaine = chaine.replace("è", "e");
        chaine = chaine.replace("ê", "e");
        chaine = chaine.replace("à", "a");
        System.out.println("Veuillez saisir le nombre de décalage :");
        Scanner decalage = new Scanner(System.in);
        int decalageSaisi = decalage.nextInt();
        String chaineCodee = "";
        //parcours de la chaine de caractères
        for (int i = 0; i < chaine.length(); i++) {
            //récupération du caractère à la position i
            char caractere = chaine.charAt(i);
            //récupération du code ascii du caractère
            int ascii = (int) caractere;
            //vérification que le caractère est une lettre minuscule
            if (ascii >= 97 && ascii <= 122) {
                //ajout du décalage au code ascii
                ascii = ascii + decalageSaisi;
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii > 122) {
                    //si le code ascii est supérieur à 122, on le fait repartir à 97
                    ascii = ascii - 26;
                }
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii < 97) {
                    //si le code ascii est inférieur à 97, on le fait repartir à 122
                    ascii = ascii + 26;
                }
                //conversion du code ascii en caractère
                char caractereCode = (char) ascii;
                //ajout du caractère à la chaine codée
                chaineCodee = chaineCodee + caractereCode;
            } else {
                //si le caractère n'est pas une lettre minuscule, on l'ajoute à la chaine codée
                chaineCodee = chaineCodee + caractere;
            }
        }
        System.out.println("La chaine codée est : " + chaineCodee);
    }
    static void bruteForce() throws IOException {
        System.out.println("Veuillez saisir une chaine de caractères  :");
        Scanner chaineADecoder = new Scanner(System.in);
        String chaine = chaineADecoder.nextLine();
        //importe la liste des mots du dictionnaire du fichier liste_français.txt dans src
        InputStream is = new FileInputStream("C:\\Users\\Ocean\\IdeaProjects\\SecuInfo\\src\\liste_francais.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader buffer = new BufferedReader(isr);

        String line = buffer.readLine();
        StringBuilder builder = new StringBuilder();

        String[] dictionnaire = new String[0];

        while(line != null){
            builder.append(line).append("\n");
            line = buffer.readLine();
            dictionnaire = builder.toString().split("\n");
        }
        boolean decodageTrouve = false;
        int decalage = 0;
        //creer la chainebully qui subit les modifs pour les test
        String chaineBully=chaine.toLowerCase();
        chaineBully=chaineBully.replace("'", " ");
        while (!decodageTrouve){
            int motTrouve = 0;
            String chaineTest = dechiffrer(chaineBully, decalage);
            //decoupe la chaine chaine
            String[] chaineDecoupee = chaineTest.split(" ");
            //parcours de la chaine de caractères
            for (int i = 0; i < chaineDecoupee.length; i++) {
                //récupération du mot à la position i
                String mot = chaineDecoupee[i];
                for (int j = 0; j < dictionnaire.length; j++) {
                    if ((mot.equals(dictionnaire[j]) && mot.length()>2)){
                        decodageTrouve=true;
                        System.out.println("Le mot "+mot+" a été trouvé dans le dictionnaire");

                        motTrouve++;
                        if (motTrouve>=2){
                            decodageTrouve=true;
                            break;
                        }

                    }
                }


            }
            if(!decodageTrouve){
                decalage++;
            }
        }
        System.out.println("Le décalage est de "+decalage);
        String chaineDecoder = dechiffrer(chaine, decalage);
        System.out.println("La chaine décodée est : " + chaineDecoder);
    }

    private static String dechiffrer(String chaineADecoder, int decalage) {
        String chaine = chaineADecoder;
        chaine=chaine.toLowerCase();
        int decalageSaisi = decalage;
        String chaineCodee = "";
        //parcours de la chaine de caractères
        for (int i = 0; i < chaine.length(); i++) {
            //récupération du caractère à la position i
            char caractere = chaine.charAt(i);
            //récupération du code ascii du caractère
            int ascii = (int) caractere;
            //vérification que le caractère est une lettre minuscule
            if (ascii >= 97 && ascii <= 122) {
                //ajout du décalage au code ascii
                ascii = ascii - decalageSaisi;
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii > 122) {
                    //si le code ascii est supérieur à 122, on le fait repartir à 97
                    ascii = ascii - 26;
                }
                //vérification que le code ascii est toujours une lettre minuscule
                if (ascii < 97) {
                    //si le code ascii est inférieur à 97, on le fait repartir à 122
                    ascii = ascii + 26;
                }
                //conversion du code ascii en caractère
                char caractereCode = (char) ascii;
                //ajout du caractère à la chaine codée
                chaineCodee = chaineCodee + caractereCode;
            } else {
                //si le caractère n'est pas une lettre minuscule, on l'ajoute à la chaine codée
                chaineCodee = chaineCodee + caractere;
            }
        }
        return chaineCodee;
    }

}
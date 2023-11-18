package org.example;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            menu();
            Main.menu();
        }

    }

    private static void menu() {
        System.out.println("Que voulez-vous faire ?\n1. Exercice 1\n2. Exercice 2\n3. Exercice 3 \n4. Exercice 4\n5. Exercice 5\n6. Exercice 6\n7. Exercice 7\n8. Exercice 8\n9. Exercice 9\n10. Quitter");
        Scanner choix = new Scanner(System.in);
        int choixSaisi = choix.nextInt();
        if (choixSaisi==1){
            Exercice1.menu();
        }
        if (choixSaisi==2) {
            Exercice2.menu();
        }
        if (choixSaisi==3) {
            Exercice3.menu();
        }
        if (choixSaisi==4) {
            Exercice4.main(null);
        }
        if (choixSaisi==5) {
            Exercice5.main(null);
        }
        if (choixSaisi==6) {
            Exercice6.main(null);
        }
        if (choixSaisi==7) {
            Exercice7.main(null);
        }
        if (choixSaisi==8) {
            try {
                Exercice8.main(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (choixSaisi==9) {
            Exercice9.main(null);
        }
        if (choixSaisi==10) {
            System.exit(0);
        }
    }
}
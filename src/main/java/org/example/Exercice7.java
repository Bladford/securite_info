package org.example;
import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Exercice7 {

    private static final int CIPHER_BLOCK_SIZE = 16;

    public static void main(String[] args) {
        byte[] encrypted = new byte[0];
        try {
            encrypted = Files.readAllBytes(Paths.get("src/main/resources/cbc_ciphertext"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Exercice7 attacker = new Exercice7();
        try {
            byte[] decrypted = attacker.decrypt(encrypted);
            System.out.println("Decrypted Message: " + new String(decrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode de déchiffrement
    public byte[] decrypt(byte[] encrypted) throws Exception {
        byte[] decrypted = new byte[encrypted.length - CIPHER_BLOCK_SIZE];
        byte[] iv = Arrays.copyOfRange(encrypted, 0, CIPHER_BLOCK_SIZE);
        byte[] block, decryptedBlock;

        for (int i = CIPHER_BLOCK_SIZE; i < encrypted.length; i += CIPHER_BLOCK_SIZE) {
            block = Arrays.copyOfRange(encrypted, i, i + CIPHER_BLOCK_SIZE);
            decryptedBlock = decryptBlock(block, iv);
            System.arraycopy(decryptedBlock, 0, decrypted, i - CIPHER_BLOCK_SIZE, CIPHER_BLOCK_SIZE);
            iv = block;
        }

        return stripPadding(decrypted);
    }

    // Méthode de déchiffrement d'un block
    private byte[] decryptBlock(byte[] block, byte[] iv) throws Exception {
        byte[] decryptedBlock = new byte[CIPHER_BLOCK_SIZE];
        byte[] trialBlock = new byte[CIPHER_BLOCK_SIZE * 2];

        System.arraycopy(iv, 0, trialBlock, 0, CIPHER_BLOCK_SIZE);
        System.arraycopy(block, 0, trialBlock, CIPHER_BLOCK_SIZE, CIPHER_BLOCK_SIZE);
        //Pour chaque byte du block
        for (int byteIndex = 0; byteIndex < CIPHER_BLOCK_SIZE; byteIndex++) {
            for (int guess = 0; guess <= 255; guess++) {
                trialBlock[CIPHER_BLOCK_SIZE - 1 - byteIndex] = (byte) (iv[CIPHER_BLOCK_SIZE - 1 - byteIndex] ^ guess);
                if (isPaddingCorrect(trialBlock)) {
                    decryptedBlock[CIPHER_BLOCK_SIZE - 1 - byteIndex] = (byte) (block[CIPHER_BLOCK_SIZE - 1 - byteIndex] ^ guess);
                    break;
                }
            }
        }

        return decryptedBlock;
    }

    // Méthode de vérification du padding
    private boolean isPaddingCorrect(byte[] trialBlock) {
        try {
            // On récupère le dernier byte du block
            byte[] paddedBlock = Arrays.copyOfRange(trialBlock, CIPHER_BLOCK_SIZE, trialBlock.length);
            stripPadding(paddedBlock);
            return true;
        } catch (BadPaddingException e) {
            return false;
        }
    }

    // Méthode de suppression du padding
    private byte[] stripPadding(byte[] paddedText) throws BadPaddingException {
        int paddingSize = paddedText[paddedText.length - 1];
        //  On vérifie que le padding est correct
        if (paddingSize < 1 || paddingSize > CIPHER_BLOCK_SIZE) {
            throw new BadPaddingException("Invalid padding length: " + paddingSize);
        }
        //Pour chaque byte du padding
        for (int i = paddedText.length - paddingSize; i < paddedText.length; i++) {
            if (paddedText[i] != paddingSize) {
                throw new BadPaddingException("Invalid padding content at position " + i);
            }
        }
        // On retourne le texte sans le padding
        return Arrays.copyOf(paddedText, paddedText.length - paddingSize);
    }
}

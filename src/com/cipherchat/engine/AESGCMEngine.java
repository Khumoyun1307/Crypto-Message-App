package com.cipherchat.engine;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

public class AESGCMEngine {
    static final int IV_LENGTH = 12;
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;

    public byte[] encrypt(byte[] plainText, byte[] aad, SecretKey key) throws CryptoException {
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            if (aad != null) cipher.updateAAD(aad);

            byte[] cipherText = cipher.doFinal(plainText);

            byte[] result = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);
            return result;
        } catch (Exception e) {
            throw new CryptoException("AES-GCM encryption failed", e);
        }
    }

    public byte[] decrypt(byte[] ivAndCipherText, byte[] aad, SecretKey key) throws CryptoException {
        try {
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(ivAndCipherText, 0, iv, 0, IV_LENGTH);
            byte[] cipherText = new byte[ivAndCipherText.length - IV_LENGTH];
            System.arraycopy(ivAndCipherText, IV_LENGTH, cipherText, 0, cipherText.length);

            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            if (aad != null) cipher.updateAAD(aad);

            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new CryptoException("AES-GCM decryption failed", e);
        }
    }
}
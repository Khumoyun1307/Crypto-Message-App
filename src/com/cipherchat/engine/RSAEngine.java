package com.cipherchat.engine;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAEngine {
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public byte[] wrapKey(byte[] keyBytes, PublicKey publicKey) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(keyBytes);
        } catch (Exception e) {
            throw new CryptoException("RSA key wrap failed", e);
        }
    }

    public byte[] unwrapKey(byte[] wrappedKey, PrivateKey privateKey) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(wrappedKey);
        } catch (Exception e) {
            throw new CryptoException("RSA key unwrap failed", e);
        }
    }
}
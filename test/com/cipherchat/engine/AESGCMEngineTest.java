package com.cipherchat.engine;

import org.junit.jupiter.api.Test;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class AESGCMEngineTest {
    private final AESGCMEngine engine = new AESGCMEngine();

    @Test
    public void testEncryptDecryptWithoutAAD() throws CryptoException, NoSuchAlgorithmException {
        byte[] plain = "Hello AES-GCM".getBytes();
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        SecretKey key = gen.generateKey();

        byte[] cipher = engine.encrypt(plain, null, key);
        assertNotNull(cipher);

        byte[] decrypted = engine.decrypt(cipher, null, key);
        assertArrayEquals(plain, decrypted);
    }

    @Test
    public void testEncryptDecryptWithAAD() throws CryptoException, NoSuchAlgorithmException {
        byte[] plain = "Test with AAD".getBytes();
        byte[] aad = "metadata".getBytes();
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        SecretKey key = gen.generateKey();

        byte[] cipher = engine.encrypt(plain, aad, key);
        assertNotNull(cipher);

        byte[] decrypted = engine.decrypt(cipher, aad, key);
        assertArrayEquals(plain, decrypted);
    }
}
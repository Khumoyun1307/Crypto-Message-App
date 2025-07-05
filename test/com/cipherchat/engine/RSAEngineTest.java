package com.cipherchat.engine;

import org.junit.jupiter.api.Test;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import static org.junit.jupiter.api.Assertions.*;

public class RSAEngineTest {
    private final RSAEngine rsa = new RSAEngine();

    @Test
    public void testWrapUnwrap() throws Exception {
        // Generate AES key
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        SecretKey aesKey = gen.generateKey();

        // Generate RSA pair
        KeyPair pair = KeyManager.generateRSAKeyPair();
        byte[] wrapped = rsa.wrapKey(aesKey.getEncoded(), pair.getPublic());
        assertNotNull(wrapped);

        byte[] unwrapped = rsa.unwrapKey(wrapped, pair.getPrivate());
        assertArrayEquals(aesKey.getEncoded(), unwrapped);
    }
}
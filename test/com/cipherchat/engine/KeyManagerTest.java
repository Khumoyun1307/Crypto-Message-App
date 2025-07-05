package com.cipherchat.engine;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class KeyManagerTest {
    @Test
    public void testGenerateSaveLoad() throws CryptoException {
        String username = "testuser";
        KeyPair pair = KeyManager.generateRSAKeyPair();
        KeyManager.saveKeyPair(username, pair);

        assertTrue(Files.exists(Paths.get("data/keys", username, "public.key")));
        assertTrue(Files.exists(Paths.get("data/keys", username, "private.key")));

        KeyPair loaded = KeyManager.loadKeyPair(username);
        assertArrayEquals(pair.getPublic().getEncoded(), loaded.getPublic().getEncoded());
        assertArrayEquals(pair.getPrivate().getEncoded(), loaded.getPrivate().getEncoded());
    }
}

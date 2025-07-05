package com.cipherchat.engine;

import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.security.KeyPair;

public class DefaultCryptoEngineTest {
    private final DefaultCryptoEngine engine = new DefaultCryptoEngine();

    @Test
    public void testFullCycle() throws CryptoException {
        String sender = "alice";
        KeyPair senderPair = KeyManager.generateRSAKeyPair();
        KeyPair recipientPair = KeyManager.generateRSAKeyPair();
        byte[] message = "Secure Message".getBytes();

        EncryptResult enc = engine.encrypt(message, sender,
                recipientPair.getPublic(), senderPair.getPrivate());
        assertNotNull(enc);

        DecryptResult dec = engine.decrypt(enc,
                recipientPair.getPrivate(), senderPair.getPublic());
        assertTrue(dec.isSignatureVerified());
        assertArrayEquals(message, dec.getPlainText());
        assertEquals(sender, dec.getSenderUsername());
    }
}
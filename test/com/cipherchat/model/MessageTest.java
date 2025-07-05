package com.cipherchat.model;

import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.KeyManager;
import com.cipherchat.engine.CryptoException;
import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.security.KeyPair;
import java.security.PublicKey;

public class MessageTest {
    @Test
    public void testMessageEncryptDecrypt() throws Exception {
        // Prepare keys
        KeyPair senderPair = KeyManager.generateRSAKeyPair();
        KeyPair recipientPair = KeyManager.generateRSAKeyPair();
        String sender = "alice";
        String text = "Hello Model Message";

        // Encrypt using engine
        DefaultCryptoEngine engine = new DefaultCryptoEngine();
        EncryptResult enc = engine.encrypt(text.getBytes(), sender,
                recipientPair.getPublic(), senderPair.getPrivate());
        // Wrap into model.Message
        Message msg = new Message(enc);

        // Decrypt via model.Message
        DecryptResult dr = msg.decrypt(recipientPair.getPrivate(), senderPair.getPublic());
        assertTrue(dr.isSignatureVerified());
        assertEquals(sender, dr.getSenderUsername());
        assertArrayEquals(text.getBytes(), dr.getPlainText());
        assertNotNull(msg.getTimestamp());
    }
}

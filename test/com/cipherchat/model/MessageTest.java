package com.cipherchat.model;

import com.cipherchat.engine.KeyManager;
import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private static final String SENDER = "alice";

    @BeforeEach
    public void cleanup() throws Exception {
        // Ensure no leftover key files
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/private.key"));
    }

    @Test
    public void testMessageEncryptDecrypt() throws Exception {
        // Generate sender and recipient key pairs
        KeyPair senderPair = KeyManager.generateRSAKeyPair();
        KeyPair recipientPair = KeyManager.generateRSAKeyPair();

        // Persist sender's key pair so Message.decrypt can load it
        KeyManager.saveKeyPair(SENDER, senderPair);

        // Encrypt a test payload
        DefaultCryptoEngine engine = new DefaultCryptoEngine();
        EncryptResult enc = engine.encrypt(
                "Hello, world!".getBytes(),
                SENDER,
                recipientPair.getPublic(),
                senderPair.getPrivate()
        );
        Message msg = new Message(enc);

        // Decrypt and verify
        DecryptResult result = msg.decrypt(recipientPair.getPrivate());
        assertTrue(result.isSignatureVerified(), "Signature should be verified");
        assertEquals("Hello, world!", new String(result.getPlainText()), "Plaintext should match");
    }
}

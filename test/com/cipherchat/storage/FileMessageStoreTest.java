package com.cipherchat.storage;

import com.cipherchat.model.Message;
import com.cipherchat.engine.KeyManager;
import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.model.EncryptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileMessageStoreTest {
    private static final String USER = "storeUser";
    private static final String SENDER = "senderStore";
    private final FileMessageStore store = new FileMessageStore();

    @BeforeEach
    public void cleanup() throws IOException {
        // Remove any existing message files
        Files.deleteIfExists(Paths.get("data/messages/" + USER + ".msgs"));
        // Remove any existing key files for both user and sender
        Files.deleteIfExists(Paths.get("data/keys/" + USER + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + USER + "/private.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/private.key"));
    }

    @Test
    public void testLoadEmpty() throws Exception {
        List<Message> empty = store.load(USER);
        assertNotNull(empty);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        // Prepare keys and persist them
        KeyPair senderPair = KeyManager.generateRSAKeyPair();
        KeyPair recipientPair = KeyManager.generateRSAKeyPair();

        // Persist both key pairs so loadKeyPair can find them
        KeyManager.saveKeyPair(SENDER, senderPair);
        KeyManager.saveKeyPair(USER, recipientPair);

        // Encrypt a sample message
        DefaultCryptoEngine engine = new DefaultCryptoEngine();
        EncryptResult enc = engine.encrypt(
                "Persistent?".getBytes(),
                SENDER,
                recipientPair.getPublic(),
                senderPair.getPrivate()
        );
        Message msg = new Message(enc);

        // Save to store
        store.save(USER, msg);

        // Load from store
        List<Message> loaded = store.load(USER);
        assertEquals(1, loaded.size(), "Should have exactly one stored message");
        Message loadedMsg = loaded.get(0);

        // Decrypt loaded message
        var result = loadedMsg.decrypt(recipientPair.getPrivate());
        assertTrue(result.isSignatureVerified(), "Signature should verify");
        assertEquals("Persistent?", new String(result.getPlainText()), "Plaintext should match");
    }
}

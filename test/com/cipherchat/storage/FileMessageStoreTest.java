package com.cipherchat.storage;

import com.cipherchat.model.Message;
import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.KeyManager;
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
    private final FileMessageStore store = new FileMessageStore();

    @BeforeEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get("data/messages/" + USER + ".msgs"));
        Files.deleteIfExists(Paths.get("data/keys/" + USER + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + USER + "/private.key"));
    }

    @Test
    public void testLoadEmpty() throws Exception {
        List<Message> empty = store.load(USER);
        assertNotNull(empty);
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        // Prepare keys and engine
        KeyPair senderPair = KeyManager.generateRSAKeyPair();
        KeyPair recipientPair = KeyManager.generateRSAKeyPair();
        String sender = "senderStore";

        // Encrypt a sample message
        DefaultCryptoEngine engine = new DefaultCryptoEngine();
        EncryptResult enc = engine.encrypt("Persistent?".getBytes(), sender,
                recipientPair.getPublic(), senderPair.getPrivate());
        Message msg = new Message(enc);

        // Save
        store.save(USER, msg);
        // Load
        List<Message> loaded = store.load(USER);
        assertEquals(1, loaded.size());
        Message loadedMsg = loaded.get(0);
        // Decrypt loaded
        var result = loadedMsg.decrypt(recipientPair.getPrivate(), senderPair.getPublic());
        assertTrue(result.isSignatureVerified());
        assertEquals("Persistent?", new String(result.getPlainText()));
    }
}

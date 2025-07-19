package com.cipherchat.model;

import com.cipherchat.engine.CryptoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {
    private static final String SENDER = "aliceUP";
    private static final String RECIPIENT = "bobUP";

    @BeforeEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + SENDER + "/private.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + RECIPIENT + "/public.key"));
        Files.deleteIfExists(Paths.get("data/keys/" + RECIPIENT + "/private.key"));
        Files.deleteIfExists(Paths.get("data/messages/" + RECIPIENT + ".msgs"));
    }

    @Test
    public void testCreateAndLoadUser() throws CryptoException, IOException, ClassNotFoundException {
        UserProfile alice = UserProfile.createNewUser(SENDER);
        assertEquals(SENDER, alice.getUsername());
        // Key files must exist
        assertTrue(Files.exists(Paths.get("data/keys/" + SENDER + "/public.key")));
        assertTrue(Files.exists(Paths.get("data/keys/" + SENDER + "/private.key")));

        UserProfile loaded = UserProfile.loadUser(SENDER);
        assertEquals(SENDER, loaded.getUsername());
        assertTrue(loaded.getInbox().isEmpty());
    }

    @Test
    public void testSendAndReceiveMessage() throws Exception {
        UserProfile alice = UserProfile.createNewUser(SENDER);
        UserProfile bob = UserProfile.createNewUser(RECIPIENT);
        String msgText = "Hi Bob, this is Alice!";

        // Alice sends to Bob
        alice.sendMessage(RECIPIENT, msgText);
        // Bob reloads inbox
        bob.refreshInbox();
        List<Message> inbox = bob.getInbox();
        assertEquals(1, inbox.size());

        Message msg = inbox.get(0);
        var result = msg.decrypt(bob.getPrivateKey());
        assertTrue(result.isSignatureVerified());
        assertEquals(msgText, new String(result.getPlainText()));
    }
}

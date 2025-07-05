package com.cipherchat.model;

import com.cipherchat.engine.CryptoException;
import com.cipherchat.engine.KeyManager;
import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.CryptoEngine;
import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.storage.FileMessageStore;
import com.cipherchat.storage.MessageStore;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user profile: key pair, inbox, and crypto operations.
 */
public class UserProfile {
    private final String username;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final CryptoEngine engine;
    private final MessageStore store;
    private final List<Message> inbox;

    private UserProfile(String username,
                        PublicKey pub,
                        PrivateKey priv,
                        List<Message> existingInbox) {
        this.username = username;
        this.publicKey = pub;
        this.privateKey = priv;
        this.engine = new DefaultCryptoEngine();
        this.store = new FileMessageStore();
        this.inbox = new ArrayList<>(existingInbox);
    }

    /**
     * Creates a new user: generates key pair, saves to disk, empty inbox.
     */
    public static UserProfile createNewUser(String username) throws CryptoException, IOException {
        KeyPair kp = KeyManager.generateRSAKeyPair();
        KeyManager.saveKeyPair(username, kp);
        return new UserProfile(username, kp.getPublic(), kp.getPrivate(), new ArrayList<>());
    }

    /**
     * Loads an existing user: loads key pair and inbox from disk.
     */
    public static UserProfile loadUser(String username)
            throws CryptoException, IOException, ClassNotFoundException {
        KeyPair kp = KeyManager.loadKeyPair(username);
        MessageStore store = new FileMessageStore();
        List<Message> loaded = store.load(username);
        return new UserProfile(username, kp.getPublic(), kp.getPrivate(), loaded);
    }

    public String getUsername() { return username; }
    public PublicKey getPublicKey() { return publicKey; }
    public PrivateKey getPrivateKey() { return privateKey; }
    public List<Message> getInbox() { return new ArrayList<>(inbox); }

    /**
     * Sends a text message to the recipient; persists to their inbox file.
     */
    public void sendMessage(String recipientUsername, String plainText)
            throws CryptoException, IOException, ClassNotFoundException {
        // Load recipient's public key
        KeyPair recKp = KeyManager.loadKeyPair(recipientUsername);
        // Encrypt
        byte[] data = plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        EncryptResult enc = ((DefaultCryptoEngine) engine)
                .encrypt(data, username, recKp.getPublic(), privateKey);
        // Wrap in Message
        Message msg = new Message(enc);
        // Persist
        store.save(recipientUsername, msg);
    }

    /**
     * Refreshes inbox from disk, merging any new messages.
     */
    public void refreshInbox() throws IOException, ClassNotFoundException {
        List<Message> loaded = store.load(username);
        inbox.clear();
        inbox.addAll(loaded);
    }
}
package com.cipherchat.model;

import com.cipherchat.engine.CryptoEngine;
import com.cipherchat.engine.DefaultCryptoEngine;
import com.cipherchat.engine.CryptoException;
import com.cipherchat.engine.KeyManager;
import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;

import java.io.Serializable;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Immutable message DTO for storage and in-memory handling.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final byte[] iv;
    private final byte[] cipherText;
    private final byte[] wrappedKey;
    private final byte[] signature;
    private final String senderUsername;
    private final LocalDateTime timestamp;

    /**
     * Construct from engine.EncryptResult; timestamp set to now.
     */
    public Message(EncryptResult result) {
        this.iv = result.getIv();
        this.cipherText = result.getCipherText();
        this.wrappedKey = result.getWrappedKey();
        this.signature = result.getSignature();
        this.senderUsername = result.getSenderUsername();
        this.timestamp = LocalDateTime.now();
    }

    // Getters for serialization & UI
    public byte[] getIv() { return iv; }
    public byte[] getCipherText() { return cipherText; }
    public byte[] getWrappedKey() { return wrappedKey; }
    public byte[] getSignature() { return signature; }
    public String getSenderUsername() { return senderUsername; }
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Decrypts this message using the provided keys, returning a DecryptResult.
     * @param recipientKey RSA private key of recipient
     */

    public DecryptResult decrypt(PrivateKey recipientKey) throws CryptoException {
        KeyPair senderKp = KeyManager.loadKeyPair(senderUsername);
        CryptoEngine engine = new DefaultCryptoEngine();
        return engine.decrypt(
                new EncryptResult(iv, cipherText, wrappedKey, signature, senderUsername),
                recipientKey,
                senderKp.getPublic()
        );
    }
}
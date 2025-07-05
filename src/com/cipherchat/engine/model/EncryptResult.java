package com.cipherchat.engine.model;

public class EncryptResult {
    private final byte[] iv;
    private final byte[] cipherText;
    private final byte[] wrappedKey;
    private final byte[] signature;
    private final String senderUsername;

    public EncryptResult(byte[] iv,
                         byte[] cipherText,
                         byte[] wrappedKey,
                         byte[] signature,
                         String senderUsername) {
        this.iv = iv;
        this.cipherText = cipherText;
        this.wrappedKey = wrappedKey;
        this.signature = signature;
        this.senderUsername = senderUsername;
    }
    public byte[] getIv() { return iv; }
    public byte[] getCipherText() { return cipherText; }
    public byte[] getWrappedKey() { return wrappedKey; }
    public byte[] getSignature() { return signature; }
    public String getSenderUsername() { return senderUsername; }
}
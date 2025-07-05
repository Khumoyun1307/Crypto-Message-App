package com.cipherchat.engine.model;

public class DecryptResult {
    private final byte[] plainText;
    private final String senderUsername;
    private final boolean signatureVerified;

    public DecryptResult(byte[] plainText,
                         String senderUsername,
                         boolean signatureVerified) {
        this.plainText = plainText;
        this.senderUsername = senderUsername;
        this.signatureVerified = signatureVerified;
    }
    public byte[] getPlainText() { return plainText; }
    public String getSenderUsername() { return senderUsername; }
    public boolean isSignatureVerified() { return signatureVerified; }
}
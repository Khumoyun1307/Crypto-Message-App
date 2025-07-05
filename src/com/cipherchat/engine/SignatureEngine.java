package com.cipherchat.engine;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SignatureEngine {
    private static final String SIGN_ALGO = "SHA256withRSA";

    public byte[] sign(byte[] data, PrivateKey privateKey) throws CryptoException {
        try {
            Signature sig = Signature.getInstance(SIGN_ALGO);
            sig.initSign(privateKey);
            sig.update(data);
            return sig.sign();
        } catch (Exception e) {
            throw new CryptoException("Signature generation failed", e);
        }
    }

    public boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws CryptoException {
        try {
            Signature sig = Signature.getInstance(SIGN_ALGO);
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signature);
        } catch (Exception e) {
            throw new CryptoException("Signature verification failed", e);
        }
    }
}

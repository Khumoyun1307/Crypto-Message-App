package com.cipherchat.engine;

import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;
import java.security.PublicKey;
import java.security.PrivateKey;

public interface CryptoEngine {
    EncryptResult encrypt(byte[] plainText,
                          String senderUsername,
                          PublicKey recipientPublicKey,
                          PrivateKey senderPrivateKey) throws CryptoException;

    DecryptResult decrypt(EncryptResult result,
                          PrivateKey recipientPrivateKey,
                          PublicKey senderPublicKey) throws CryptoException;
}
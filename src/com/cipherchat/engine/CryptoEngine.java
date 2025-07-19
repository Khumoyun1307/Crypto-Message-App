package com.cipherchat.engine;

import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;

import javax.crypto.SecretKey;
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

    // Generic helpers for any client:
    SecretKey generateAESKey() throws CryptoException;
    byte[] aesGcmEncrypt(byte[] plainText, byte[] aad, SecretKey key) throws CryptoException;
    byte[] wrapKey(byte[] aesKeyBytes, PublicKey recipientPublicKey) throws CryptoException;
    byte[] prepareSignatureData(byte[] ivAndCipherText, byte[] wrappedKey, byte[] aad);
    byte[] signData(byte[] data, PrivateKey senderPrivateKey) throws CryptoException;
    EncryptResult createEncryptedResult(byte[] ivAndCipherText,
                                     byte[] wrappedKey,
                                     byte[] signature,
                                     String senderUsername);
}
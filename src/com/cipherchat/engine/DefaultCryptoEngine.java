package com.cipherchat.engine;

import com.cipherchat.engine.model.EncryptResult;
import com.cipherchat.engine.model.DecryptResult;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class DefaultCryptoEngine implements CryptoEngine {
    private final AESGCMEngine aesEngine = new AESGCMEngine();
    private final RSAEngine rsaEngine = new RSAEngine();
    private final SignatureEngine sigEngine = new SignatureEngine();

    @Override
    public EncryptResult encrypt(byte[] plainText,
                                 String senderUsername,
                                 PublicKey recipientPublicKey,
                                 PrivateKey senderPrivateKey) throws CryptoException {
        SecretKey aesKey = KeyManager.generateAESKey();
        byte[] aad = senderUsername.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] ivAndCipher = aesEngine.encrypt(plainText, aad, aesKey);

        byte[] wrappedKey = rsaEngine.wrapKey(aesKey.getEncoded(), recipientPublicKey);
        byte[] sigData = concatenate(ivAndCipher, wrappedKey, aad);
        byte[] signature = sigEngine.sign(sigData, senderPrivateKey);

        byte[] iv = Arrays.copyOfRange(ivAndCipher, 0, AESGCMEngine.IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(ivAndCipher, AESGCMEngine.IV_LENGTH, ivAndCipher.length);
        return new EncryptResult(iv, cipherText, wrappedKey, signature, senderUsername);
    }

    @Override
    public DecryptResult decrypt(EncryptResult result,
                                 PrivateKey recipientPrivateKey,
                                 PublicKey senderPublicKey) throws CryptoException {
        String sender = result.getSenderUsername();
        byte[] aad = sender.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        byte[] sigData = concatenate(result.getIv(), result.getCipherText(), result.getWrappedKey(), aad);
        boolean verified = sigEngine.verify(sigData, result.getSignature(), senderPublicKey);
        if (!verified) throw new CryptoException("Signature verification failed");

        byte[] aesKeyBytes = rsaEngine.unwrapKey(result.getWrappedKey(), recipientPrivateKey);
        SecretKey aesKey = KeyManager.restoreAESKey(aesKeyBytes);

        byte[] ivAndCipher = concatenate(result.getIv(), result.getCipherText());
        byte[] plainText = aesEngine.decrypt(ivAndCipher, aad, aesKey);
        return new DecryptResult(plainText, sender, verified);
    }

    // Generic helpers
    @Override
    public SecretKey generateAESKey() throws CryptoException {
        return KeyManager.generateAESKey();
    }

    @Override
    public byte[] aesGcmEncrypt(byte[] plainText, byte[] aad, SecretKey key) throws CryptoException {
        return aesEngine.encrypt(plainText, aad, key);
    }

    @Override
    public byte[] wrapKey(byte[] aesKeyBytes, PublicKey recipientPublicKey) throws CryptoException {
        return rsaEngine.wrapKey(aesKeyBytes, recipientPublicKey);
    }

    @Override
    public byte[] prepareSignatureData(byte[] ivAndCipherText, byte[] wrappedKey, byte[] aad) {
        return concatenate(ivAndCipherText, wrappedKey, aad);
    }

    @Override
    public byte[] signData(byte[] data, PrivateKey senderPrivateKey) throws CryptoException {
        return sigEngine.sign(data, senderPrivateKey);
    }

    @Override
    public EncryptResult createEncryptedResult(byte[] ivAndCipherText,
                                             byte[] wrappedKey,
                                             byte[] signature,
                                             String senderUsername) {
        byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, AESGCMEngine.IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(ivAndCipherText, AESGCMEngine.IV_LENGTH, ivAndCipherText.length);
        return new EncryptResult(iv, cipherText, wrappedKey, signature, senderUsername);
    }

    private byte[] concatenate(byte[]... arrays) {
        int total = 0;
        for (byte[] arr : arrays) total += arr.length;
        byte[] out = new byte[total];
        int pos = 0;
        for (byte[] arr : arrays) {
            System.arraycopy(arr, 0, out, pos, arr.length);
            pos += arr.length;
        }
        return out;
    }
}
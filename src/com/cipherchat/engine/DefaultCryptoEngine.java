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
        // 1) Generate AES key
        SecretKey aesKey = KeyManager.generateAESKey();
        // 2) Encrypt plaintext with AAD = senderUsername bytes
        byte[] aad = senderUsername.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] ivAndCipher = aesEngine.encrypt(plainText, aad, aesKey);
        // 3) Wrap AES key
        byte[] wrappedKey = rsaEngine.wrapKey(aesKey.getEncoded(), recipientPublicKey);
        // 4) Prepare data for signature
        byte[] sigData = concatenate(ivAndCipher, wrappedKey, aad);
        byte[] signature = sigEngine.sign(sigData, senderPrivateKey);
        // 5) Extract iv and cipherText
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
        // 1) Verify signature
        byte[] sigData = concatenate(result.getIv(), result.getCipherText(), result.getWrappedKey(), aad);
        boolean verified = sigEngine.verify(sigData, result.getSignature(), senderPublicKey);
        if (!verified) throw new CryptoException("Signature verification failed");
        // 2) Unwrap AES key
        byte[] aesKeyBytes = rsaEngine.unwrapKey(result.getWrappedKey(), recipientPrivateKey);
        SecretKey aesKey = KeyManager.restoreAESKey(aesKeyBytes);
        // 3) Decrypt ciphertext
        byte[] ivAndCipher = concatenate(result.getIv(), result.getCipherText());
        byte[] plainText = aesEngine.decrypt(ivAndCipher, aad, aesKey);
        return new DecryptResult(plainText, sender, verified);
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

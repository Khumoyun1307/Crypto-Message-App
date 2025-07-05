package com.cipherchat.engine;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Set;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;

public class KeyManager {
    private static final String KEY_ALGO = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String KEY_DIR = "data/keys";

    public static KeyPair generateRSAKeyPair() throws CryptoException {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(KEY_ALGO);
            gen.initialize(KEY_SIZE);
            return gen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("RSA key generation failed", e);
        }
    }

    public static SecretKey generateAESKey() throws CryptoException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("AES key generation failed", e);
        }
    }

    public static void saveKeyPair(String username, KeyPair keyPair) throws CryptoException {
        Path userDir = Paths.get(KEY_DIR, username);
        try {
            Files.createDirectories(userDir);
            try (ObjectOutputStream pubOut = new ObjectOutputStream(Files.newOutputStream(userDir.resolve("public.key")))) {
                pubOut.writeObject(keyPair.getPublic());
            }
            try (ObjectOutputStream privOut = new ObjectOutputStream(Files.newOutputStream(userDir.resolve("private.key")))) {
                privOut.writeObject(keyPair.getPrivate());
            }
            // Restrictive perms (Unix)
            try {
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
                Files.setPosixFilePermissions(userDir.resolve("private.key"), perms);
            } catch (UnsupportedOperationException ignored) {}
        } catch (IOException e) {
            throw new CryptoException("Saving key pair failed", e);
        }
    }

    public static KeyPair loadKeyPair(String username) throws CryptoException {
        Path userDir = Paths.get(KEY_DIR, username);
        try (ObjectInputStream pubIn = new ObjectInputStream(Files.newInputStream(userDir.resolve("public.key")))) {
            PublicKey pub = (PublicKey) pubIn.readObject();
            try (ObjectInputStream privIn = new ObjectInputStream(Files.newInputStream(userDir.resolve("private.key")))) {
                PrivateKey priv = (PrivateKey) privIn.readObject();
                return new KeyPair(pub, priv);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new CryptoException("Loading key pair failed", e);
        }
    }

    public static SecretKey restoreAESKey(byte[] keyBytes) {
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }
}

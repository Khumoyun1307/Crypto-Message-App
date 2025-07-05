package com.cipherchat.engine;

import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import static org.junit.jupiter.api.Assertions.*;

public class SignatureEngineTest {
    private final SignatureEngine sig = new SignatureEngine();

    @Test
    public void testSignVerify() throws Exception {
        byte[] data = "Sign this".getBytes();
        KeyPair pair = KeyManager.generateRSAKeyPair();
        byte[] signature = sig.sign(data, pair.getPrivate());
        assertTrue(sig.verify(data, signature, pair.getPublic()));
    }

    @Test
    public void testVerifyFail() throws Exception {
        byte[] data = "Original".getBytes();
        byte[] tampered = "Tampered".getBytes();
        KeyPair pair = KeyManager.generateRSAKeyPair();
        byte[] signature = sig.sign(data, pair.getPrivate());
        assertFalse(sig.verify(tampered, signature, pair.getPublic()));
    }
}

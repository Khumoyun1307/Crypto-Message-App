package com.cipherchat.engine;

public class CryptoException extends Exception {
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
    public CryptoException(String message) {
        super(message);
    }
}
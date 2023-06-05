package com.socialmedia.app.config;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Utility class for generating RSA keys.
 */
@Component
final class KeyGeneratorUtils {
    private KeyGeneratorUtils() {}

    /**
     * Generates a new RSA key pair.
     *
     * @return the generated KeyPair object.
     * @throws IllegalStateException if an error occurs during key pair generation.
     */
    static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}

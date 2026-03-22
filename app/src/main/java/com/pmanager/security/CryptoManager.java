package com.pmanager.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Handles AES-256-GCM encryption/decryption using the Android Keystore.
 * Passwords are encrypted before storing and decrypted only when needed.
 */
public class CryptoManager {

    private static final String KEYSTORE_ALIAS = "pmanager_key";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private static CryptoManager instance;

    private CryptoManager() {
        createKeyIfNeeded();
    }

    public static synchronized CryptoManager getInstance() {
        if (instance == null) {
            instance = new CryptoManager();
        }
        return instance;
    }

    /**
     * Creates an AES-256 key in the Android Keystore if it doesn't exist.
     */
    private void createKeyIfNeeded() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                        KEYSTORE_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build();
                keyGenerator.init(spec);
                keyGenerator.generateKey();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create encryption key", e);
        }
    }

    /**
     * Retrieves the secret key from the Android Keystore.
     */
    private SecretKey getKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            return (SecretKey) keyStore.getKey(KEYSTORE_ALIAS, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get encryption key", e);
        }
    }

    /**
     * Encrypts plaintext using AES-256-GCM.
     * Returns Base64-encoded string containing IV + ciphertext.
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) return "";
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            byte[] iv = cipher.getIV();
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Combine IV + ciphertext
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);

            return Base64.encodeToString(buffer.array(), Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * Decrypts Base64-encoded ciphertext (IV + encrypted data).
     * Returns the original plaintext.
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) return "";
        try {
            byte[] decoded = Base64.decode(encryptedData, Base64.NO_WRAP);

            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), spec);

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}

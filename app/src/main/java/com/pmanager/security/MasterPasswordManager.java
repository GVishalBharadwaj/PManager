package com.pmanager.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manages the master password using PBKDF2WithHmacSHA256 hashing.
 * The hash and salt are stored in EncryptedSharedPreferences.
 */
public class MasterPasswordManager {

    private static final String PREFS_NAME = "pmanager_master_prefs";
    private static final String KEY_HASH = "master_hash";
    private static final String KEY_SALT = "master_salt";
    private static final String KEY_BIOMETRIC_ENABLED = "biometric_enabled";
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 32;

    private static MasterPasswordManager instance;
    private SharedPreferences prefs;

    private MasterPasswordManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create encrypted preferences", e);
        }
    }

    public static synchronized MasterPasswordManager getInstance(Context context) {
        if (instance == null) {
            instance = new MasterPasswordManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Checks if a master password has been set up.
     */
    public boolean isMasterPasswordSet() {
        return prefs.contains(KEY_HASH);
    }

    /**
     * Sets up the master password for the first time.
     */
    public void setupMasterPassword(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        String hash = hashPassword(password, salt);

        prefs.edit()
                .putString(KEY_HASH, hash)
                .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
                .apply();
    }

    /**
     * Verifies the entered password against the stored hash.
     */
    public boolean verifyMasterPassword(String password) {
        String storedHash = prefs.getString(KEY_HASH, null);
        String storedSalt = prefs.getString(KEY_SALT, null);

        if (storedHash == null || storedSalt == null) return false;

        byte[] salt = Base64.decode(storedSalt, Base64.NO_WRAP);
        String hash = hashPassword(password, salt);

        return storedHash.equals(hash);
    }

    /**
     * Changes the master password.
     */
    public boolean changeMasterPassword(String oldPassword, String newPassword) {
        if (!verifyMasterPassword(oldPassword)) return false;
        setupMasterPassword(newPassword);
        return true;
    }

    /**
     * Enables or disables biometric authentication.
     */
    public void setBiometricEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply();
    }

    /**
     * Checks if biometric authentication is enabled.
     */
    public boolean isBiometricEnabled() {
        return prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false);
    }

    /**
     * Hashes the password with PBKDF2WithHmacSHA256.
     */
    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}

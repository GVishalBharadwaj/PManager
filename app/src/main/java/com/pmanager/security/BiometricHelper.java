package com.pmanager.security;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * Wraps the BiometricPrompt API for fingerprint authentication.
 */
public class BiometricHelper {

    public interface BiometricCallback {
        void onSuccess();
        void onError(String error);
        void onFailed();
    }

    /**
     * Checks if biometric authentication is available on this device.
     */
    public static boolean isBiometricAvailable(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        int result = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG);
        return result == BiometricManager.BIOMETRIC_SUCCESS;
    }

    /**
     * Shows the biometric authentication prompt.
     */
    public static void authenticate(FragmentActivity activity, BiometricCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock PManager")
                .setSubtitle("Use your fingerprint to unlock")
                .setNegativeButtonText("Use Password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(activity),
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        callback.onSuccess();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode,
                            @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        callback.onError(errString.toString());
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        callback.onFailed();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
}

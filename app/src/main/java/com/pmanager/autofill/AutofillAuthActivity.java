package com.pmanager.autofill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmanager.security.BiometricHelper;
import com.pmanager.security.MasterPasswordManager;
import com.pmanager.security.SessionManager;

/**
 * Activity for authenticating before autofill when the vault is locked.
 * Shows biometric prompt or password entry.
 */
public class AutofillAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MasterPasswordManager masterManager = MasterPasswordManager.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        if (sessionManager.isSessionActive()) {
            // Already unlocked
            setResult(Activity.RESULT_OK);
            finish();
            return;
        }

        // Try biometric first
        if (masterManager.isBiometricEnabled() && BiometricHelper.isBiometricAvailable(this)) {
            BiometricHelper.authenticate(this, new BiometricHelper.BiometricCallback() {
                @Override
                public void onSuccess() {
                    sessionManager.unlock();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onError(String error) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }

                @Override
                public void onFailed() {
                    Toast.makeText(AutofillAuthActivity.this,
                            "Authentication failed", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
        } else {
            // No biometric — user needs to unlock from the main app
            Toast.makeText(this, "Please unlock PManager first",
                    Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }
}

package com.pmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pmanager.R;
import com.pmanager.security.BiometricHelper;
import com.pmanager.security.MasterPasswordManager;
import com.pmanager.security.SessionManager;

/**
 * Login screen — master password entry + biometric unlock.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private MaterialButton btnUnlock, btnBiometric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilPassword = findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        btnUnlock = findViewById(R.id.btn_unlock);
        btnBiometric = findViewById(R.id.btn_biometric);

        btnUnlock.setOnClickListener(v -> attemptLogin());

        // Show biometric button if enabled and available
        MasterPasswordManager masterManager = MasterPasswordManager.getInstance(this);
        if (masterManager.isBiometricEnabled() && BiometricHelper.isBiometricAvailable(this)) {
            btnBiometric.setVisibility(View.VISIBLE);
            btnBiometric.setOnClickListener(v -> attemptBiometric());

            // Auto-prompt biometric on launch
            attemptBiometric();
        }

        // Handle IME action
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            attemptLogin();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If session is still active (e.g., returned from background quickly), skip login
        if (SessionManager.getInstance(this).isSessionActive()) {
            goToVault();
        }
    }

    private void attemptLogin() {
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        tilPassword.setError(null);

        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_password_required));
            return;
        }

        if (MasterPasswordManager.getInstance(this).verifyMasterPassword(password)) {
            SessionManager.getInstance(this).unlock();
            goToVault();
        } else {
            tilPassword.setError(getString(R.string.error_wrong_password));
        }
    }

    private void attemptBiometric() {
        BiometricHelper.authenticate(this, new BiometricHelper.BiometricCallback() {
            @Override
            public void onSuccess() {
                SessionManager.getInstance(LoginActivity.this).unlock();
                goToVault();
            }

            @Override
            public void onError(String error) {
                // User chose "Use Password" — just stay on this screen
            }

            @Override
            public void onFailed() {
                Toast.makeText(LoginActivity.this,
                        "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToVault() {
        startActivity(new Intent(this, VaultActivity.class));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to splash
        finishAffinity();
    }
}

package com.pmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pmanager.R;
import com.pmanager.security.BiometricHelper;
import com.pmanager.security.MasterPasswordManager;
import com.pmanager.security.SessionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Settings screen for biometric, auto-lock, autofill, and master password management.
 */
public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial switchBiometric;
    private TextView tvAutoLockValue;
    private MasterPasswordManager masterManager;
    private SessionManager sessionManager;

    private final String[] timeoutLabels = {
            "Immediately", "30 seconds", "1 minute", "5 minutes", "Never"
    };
    private final long[] timeoutValues = {
            0, 30_000, 60_000, 300_000, Long.MAX_VALUE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        masterManager = MasterPasswordManager.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        setupBiometric();
        setupAutoLock();
        setupChangePassword();
        setupAutofill();
    }

    private void setupBiometric() {
        switchBiometric = findViewById(R.id.switch_biometric);
        switchBiometric.setChecked(masterManager.isBiometricEnabled());
        switchBiometric.setEnabled(BiometricHelper.isBiometricAvailable(this));

        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !BiometricHelper.isBiometricAvailable(this)) {
                switchBiometric.setChecked(false);
                Toast.makeText(this, "Biometric not available on this device",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            masterManager.setBiometricEnabled(isChecked);
        });
    }

    private void setupAutoLock() {
        tvAutoLockValue = findViewById(R.id.tv_auto_lock_value);
        updateAutoLockLabel();

        findViewById(R.id.setting_auto_lock).setOnClickListener(v -> {
            int currentIndex = getTimeoutIndex(sessionManager.getAutoLockTimeout());
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.pref_auto_lock)
                    .setSingleChoiceItems(timeoutLabels, currentIndex, (dialog, which) -> {
                        sessionManager.setAutoLockTimeout(timeoutValues[which]);
                        updateAutoLockLabel();
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.btn_cancel, null)
                    .show();
        });
    }

    private void updateAutoLockLabel() {
        int index = getTimeoutIndex(sessionManager.getAutoLockTimeout());
        tvAutoLockValue.setText(timeoutLabels[index]);
    }

    private int getTimeoutIndex(long timeout) {
        for (int i = 0; i < timeoutValues.length; i++) {
            if (timeoutValues[i] == timeout) return i;
        }
        return 2; // default: 1 minute
    }

    private void setupChangePassword() {
        findViewById(R.id.setting_change_password).setOnClickListener(v -> {
            showChangePasswordDialog();
        });
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_change_password, null);

        TextInputEditText etOld = dialogView.findViewById(R.id.et_old_password);
        TextInputEditText etNew = dialogView.findViewById(R.id.et_new_password);
        TextInputEditText etConfirm = dialogView.findViewById(R.id.et_confirm_password);
        TextInputLayout tilOld = dialogView.findViewById(R.id.til_old_password);
        TextInputLayout tilNew = dialogView.findViewById(R.id.til_new_password);
        TextInputLayout tilConfirm = dialogView.findViewById(R.id.til_confirm_password);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.pref_change_master)
                .setView(dialogView)
                .setPositiveButton("Change", (dialog, which) -> {
                    String oldPass = etOld.getText() != null ? etOld.getText().toString() : "";
                    String newPass = etNew.getText() != null ? etNew.getText().toString() : "";
                    String confirm = etConfirm.getText() != null ? etConfirm.getText().toString() : "";

                    if (newPass.length() < 8) {
                        Toast.makeText(this, getString(R.string.error_password_short),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPass.equals(confirm)) {
                        Toast.makeText(this, getString(R.string.error_passwords_mismatch),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (masterManager.changeMasterPassword(oldPass, newPass)) {
                        Toast.makeText(this, "Master password changed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.error_wrong_password),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void setupAutofill() {
        findViewById(R.id.setting_autofill).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE);
            intent.setData(android.net.Uri.parse("package:" + getPackageName()));
            try {
                startActivity(intent);
            } catch (Exception e) {
                // Fallback: open autofill settings
                try {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                } catch (Exception ex) {
                    Toast.makeText(this,
                            "Please enable PManager as autofill provider in Settings > Passwords & Autofill",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

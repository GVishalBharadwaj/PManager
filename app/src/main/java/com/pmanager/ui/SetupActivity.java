package com.pmanager.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pmanager.R;
import com.pmanager.security.MasterPasswordManager;
import com.pmanager.security.SessionManager;
import com.pmanager.util.PasswordStrengthChecker;
import com.pmanager.util.PasswordStrengthChecker.Strength;

/**
 * First-launch screen to create the master password.
 */
public class SetupActivity extends AppCompatActivity {

    private TextInputLayout tilPassword, tilConfirm;
    private TextInputEditText etPassword, etConfirm;
    private MaterialButton btnCreate;
    private View strengthContainer;
    private ProgressBar strengthBar;
    private TextView tvStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        tilPassword = findViewById(R.id.til_master_password);
        tilConfirm = findViewById(R.id.til_confirm_password);
        etPassword = findViewById(R.id.et_master_password);
        etConfirm = findViewById(R.id.et_confirm_password);
        btnCreate = findViewById(R.id.btn_create);
        strengthContainer = findViewById(R.id.strength_container);
        strengthBar = findViewById(R.id.strength_bar);
        tvStrength = findViewById(R.id.tv_strength);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateStrength(s.toString());
            }
        });

        btnCreate.setOnClickListener(v -> createVault());
    }

    private void updateStrength(String password) {
        if (password.isEmpty()) {
            strengthContainer.setVisibility(View.GONE);
            return;
        }

        strengthContainer.setVisibility(View.VISIBLE);
        Strength strength = PasswordStrengthChecker.checkStrength(password);
        float progress = PasswordStrengthChecker.getStrengthProgress(strength);

        strengthBar.setProgress((int) (progress * 100));

        int colorRes;
        String label;
        switch (strength) {
            case WEAK:
                colorRes = R.color.strength_weak;
                label = getString(R.string.strength_weak);
                break;
            case FAIR:
                colorRes = R.color.strength_fair;
                label = getString(R.string.strength_fair);
                break;
            case GOOD:
                colorRes = R.color.strength_good;
                label = getString(R.string.strength_good);
                break;
            case STRONG:
                colorRes = R.color.strength_strong;
                label = getString(R.string.strength_strong);
                break;
            default:
                colorRes = R.color.strength_weak;
                label = "";
        }

        int color = ContextCompat.getColor(this, colorRes);
        strengthBar.setProgressTintList(ColorStateList.valueOf(color));
        tvStrength.setText(label);
        tvStrength.setTextColor(color);
    }

    private void createVault() {
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        String confirm = etConfirm.getText() != null ? etConfirm.getText().toString() : "";

        tilPassword.setError(null);
        tilConfirm.setError(null);

        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_password_required));
            return;
        }
        if (password.length() < 8) {
            tilPassword.setError(getString(R.string.error_password_short));
            return;
        }
        if (!password.equals(confirm)) {
            tilConfirm.setError(getString(R.string.error_passwords_mismatch));
            return;
        }

        MasterPasswordManager.getInstance(this).setupMasterPassword(password);
        SessionManager.getInstance(this).unlock();

        startActivity(new Intent(this, VaultActivity.class));
        finishAffinity();
    }
}

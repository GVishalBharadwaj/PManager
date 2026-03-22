package com.pmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.pmanager.R;
import com.pmanager.security.MasterPasswordManager;
import com.pmanager.security.SessionManager;

/**
 * Splash screen that routes to Setup (first launch), Login, or Vault (already unlocked).
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::navigate, SPLASH_DELAY);
    }

    private void navigate() {
        MasterPasswordManager masterManager = MasterPasswordManager.getInstance(this);
        SessionManager sessionManager = SessionManager.getInstance(this);

        Intent intent;
        if (!masterManager.isMasterPasswordSet()) {
            // First launch — set up master password
            intent = new Intent(this, SetupActivity.class);
        } else if (sessionManager.isSessionActive()) {
            // Already unlocked
            intent = new Intent(this, VaultActivity.class);
        } else {
            // Need to login
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

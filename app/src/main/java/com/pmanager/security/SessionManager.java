package com.pmanager.security;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Manages the user session and auto-lock behavior.
 * Observes the app lifecycle to lock when backgrounded beyond the timeout.
 */
public class SessionManager implements DefaultLifecycleObserver {

    private static final String PREFS_NAME = "session_prefs";
    private static final String KEY_AUTO_LOCK_TIMEOUT = "auto_lock_timeout";
    private static final long DEFAULT_TIMEOUT_MS = 60_000; // 60 seconds

    private static SessionManager instance;

    private boolean isUnlocked = false;
    private long backgroundTimestamp = 0;
    private SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Marks the session as unlocked.
     */
    public void unlock() {
        isUnlocked = true;
        backgroundTimestamp = 0;
    }

    /**
     * Locks the session immediately.
     */
    public void lock() {
        isUnlocked = false;
        backgroundTimestamp = 0;
    }

    /**
     * Checks if the session is currently active (unlocked).
     */
    public boolean isSessionActive() {
        return isUnlocked;
    }

    /**
     * Gets the configured auto-lock timeout in milliseconds.
     */
    public long getAutoLockTimeout() {
        return prefs.getLong(KEY_AUTO_LOCK_TIMEOUT, DEFAULT_TIMEOUT_MS);
    }

    /**
     * Sets the auto-lock timeout.
     */
    public void setAutoLockTimeout(long timeoutMs) {
        prefs.edit().putLong(KEY_AUTO_LOCK_TIMEOUT, timeoutMs).apply();
    }

    // Lifecycle callbacks for auto-lock

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        // App went to background
        if (isUnlocked) {
            backgroundTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        // App came to foreground
        if (isUnlocked && backgroundTimestamp > 0) {
            long elapsed = System.currentTimeMillis() - backgroundTimestamp;
            if (elapsed >= getAutoLockTimeout()) {
                lock();
            }
            backgroundTimestamp = 0;
        }
    }
}

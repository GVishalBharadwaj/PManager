package com.pmanager;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.pmanager.security.SessionManager;

public class PManagerApp extends Application {

    private static PManagerApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Observe app lifecycle for auto-lock
        ProcessLifecycleOwner.get().getLifecycle()
                .addObserver(SessionManager.getInstance(this));
    }

    public static PManagerApp getInstance() {
        return instance;
    }
}

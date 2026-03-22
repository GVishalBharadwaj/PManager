package com.pmanager.util;

import android.content.ClipData;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.pmanager.R;

/**
 * Utility for copying text to clipboard with auto-clear after 30 seconds.
 */
public class ClipboardHelper {

    private static final long CLEAR_DELAY_MS = 30_000;
    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Copies text to the clipboard and schedules auto-clear.
     */
    public static void copyToClipboard(Context context, String label, String text) {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();

        // Auto-clear clipboard after 30 seconds
        handler.postDelayed(() -> {
            ClipData emptyClip = ClipData.newPlainText("", "");
            clipboard.setPrimaryClip(emptyClip);
        }, CLEAR_DELAY_MS);
    }
}

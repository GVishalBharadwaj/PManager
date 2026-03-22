package com.pmanager.ui;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pmanager.R;
import com.pmanager.data.AppDatabase;
import com.pmanager.data.PasswordDao;
import com.pmanager.data.PasswordEntry;
import com.pmanager.security.CryptoManager;
import com.pmanager.util.ClipboardHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * Displays details of a saved password with copy, reveal, edit, and delete actions.
 */
public class PasswordDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvCategory, tvUsername, tvPassword, tvWebsite, tvNotes,
            tvCreated, tvModified;
    private View categoryDot;
    private MaterialCardView cardWebsite, cardNotes;
    private ImageButton btnTogglePassword;
    private PasswordDao dao;
    private PasswordEntry entry;
    private boolean passwordVisible = false;
    private String decryptedPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);

        dao = AppDatabase.getInstance(this).passwordDao();

        initViews();

        long id = getIntent().getLongExtra("password_id", -1);
        if (id == -1) {
            finish();
            return;
        }
        loadEntry(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload in case edited
        long id = getIntent().getLongExtra("password_id", -1);
        if (id != -1) loadEntry(id);
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvTitle = findViewById(R.id.tv_title);
        tvCategory = findViewById(R.id.tv_category);
        categoryDot = findViewById(R.id.category_dot);
        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        tvWebsite = findViewById(R.id.tv_website);
        tvNotes = findViewById(R.id.tv_notes);
        tvCreated = findViewById(R.id.tv_created);
        tvModified = findViewById(R.id.tv_modified);
        cardWebsite = findViewById(R.id.card_website);
        cardNotes = findViewById(R.id.card_notes);

        ImageButton btnCopyUsername = findViewById(R.id.btn_copy_username);
        ImageButton btnCopyPassword = findViewById(R.id.btn_copy_password);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        MaterialButton btnEdit = findViewById(R.id.btn_edit);
        MaterialButton btnDelete = findViewById(R.id.btn_delete);

        btnCopyUsername.setOnClickListener(v -> {
            if (entry != null) {
                ClipboardHelper.copyToClipboard(this, "Username", entry.getUsername());
            }
        });

        btnCopyPassword.setOnClickListener(v -> {
            if (!decryptedPassword.isEmpty()) {
                ClipboardHelper.copyToClipboard(this, "Password", decryptedPassword);
            }
        });

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        btnEdit.setOnClickListener(v -> {
            if (entry != null) {
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra("password_id", entry.getId());
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadEntry(long id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            entry = dao.getById(id);
            if (entry == null) {
                runOnUiThread(this::finish);
                return;
            }

            try {
                decryptedPassword = CryptoManager.getInstance()
                        .decrypt(entry.getEncryptedPassword());
            } catch (Exception e) {
                decryptedPassword = "••••••••";
            }

            runOnUiThread(this::displayEntry);
        });
    }

    private void displayEntry() {
        tvTitle.setText(entry.getTitle());
        tvCategory.setText(entry.getCategory());
        tvUsername.setText(entry.getUsername());

        // Password masked by default
        passwordVisible = false;
        tvPassword.setText("••••••••••••");

        // Category dot
        int color = getCategoryColor(entry.getCategory());
        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(color);
        categoryDot.setBackground(dot);

        // Website
        if (entry.getWebsite() != null && !entry.getWebsite().isEmpty()) {
            cardWebsite.setVisibility(View.VISIBLE);
            tvWebsite.setText(entry.getWebsite());
        }

        // Notes
        if (entry.getNotes() != null && !entry.getNotes().isEmpty()) {
            cardNotes.setVisibility(View.VISIBLE);
            tvNotes.setText(entry.getNotes());
        }

        // Timestamps
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault());
        tvCreated.setText("Created: " + sdf.format(new Date(entry.getCreatedAt())));
        tvModified.setText("Modified: " + sdf.format(new Date(entry.getUpdatedAt())));
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        tvPassword.setText(passwordVisible ? decryptedPassword : "••••••••••••");
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        dao.delete(entry);
                        runOnUiThread(this::finish);
                    });
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private int getCategoryColor(String category) {
        if (category == null) return ContextCompat.getColor(this, R.color.cat_other);
        switch (category.toLowerCase()) {
            case "social": return ContextCompat.getColor(this, R.color.cat_social);
            case "email": return ContextCompat.getColor(this, R.color.cat_email);
            case "finance": return ContextCompat.getColor(this, R.color.cat_finance);
            case "shopping": return ContextCompat.getColor(this, R.color.cat_shopping);
            case "work": return ContextCompat.getColor(this, R.color.cat_work);
            default: return ContextCompat.getColor(this, R.color.cat_other);
        }
    }
}

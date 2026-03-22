package com.pmanager.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pmanager.R;
import com.pmanager.data.AppDatabase;
import com.pmanager.data.PasswordDao;
import com.pmanager.data.PasswordEntry;
import com.pmanager.security.CryptoManager;
import com.pmanager.util.PasswordStrengthChecker;
import com.pmanager.util.PasswordStrengthChecker.Strength;

import java.util.concurrent.Executors;

/**
 * Screen to add a new password or edit an existing one.
 */
public class AddEditActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etUsername, etPassword, etWebsite, etNotes;
    private TextInputLayout tilTitle, tilPassword;
    private AutoCompleteTextView dropdownCategory;
    private MaterialButton btnSave, btnGenerate;
    private ProgressBar strengthBar;
    private TextView tvStrength;
    private View strengthContainer;
    private MaterialToolbar toolbar;

    private PasswordDao dao;
    private long editId = -1;
    private PasswordEntry existingEntry;

    private final String[] categories = {"Social", "Email", "Finance", "Shopping", "Work", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dao = AppDatabase.getInstance(this).passwordDao();

        initViews();
        setupCategory();
        setupStrengthIndicator();

        // Check if editing
        editId = getIntent().getLongExtra("password_id", -1);
        if (editId != -1) {
            toolbar.setTitle(getString(R.string.edit_title));
            loadExistingEntry();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.et_title);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etWebsite = findViewById(R.id.et_website);
        etNotes = findViewById(R.id.et_notes);
        tilTitle = findViewById(R.id.til_title);
        tilPassword = findViewById(R.id.til_password);
        dropdownCategory = findViewById(R.id.dropdown_category);
        btnSave = findViewById(R.id.btn_save);
        btnGenerate = findViewById(R.id.btn_generate);
        strengthBar = findViewById(R.id.strength_bar);
        tvStrength = findViewById(R.id.tv_strength);
        strengthContainer = findViewById(R.id.strength_container);

        btnSave.setOnClickListener(v -> save());
        btnGenerate.setOnClickListener(v -> showGeneratorDialog());
    }

    private void setupCategory() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        dropdownCategory.setAdapter(adapter);
        dropdownCategory.setText(categories[5], false); // Default: "Other"
    }

    private void setupStrengthIndicator() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateStrength(s.toString());
            }
        });
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
            case WEAK: colorRes = R.color.strength_weak; label = getString(R.string.strength_weak); break;
            case FAIR: colorRes = R.color.strength_fair; label = getString(R.string.strength_fair); break;
            case GOOD: colorRes = R.color.strength_good; label = getString(R.string.strength_good); break;
            default: colorRes = R.color.strength_strong; label = getString(R.string.strength_strong); break;
        }
        int color = ContextCompat.getColor(this, colorRes);
        strengthBar.setProgressTintList(ColorStateList.valueOf(color));
        tvStrength.setText(label);
        tvStrength.setTextColor(color);
    }

    private void loadExistingEntry() {
        Executors.newSingleThreadExecutor().execute(() -> {
            existingEntry = dao.getById(editId);
            if (existingEntry != null) {
                runOnUiThread(() -> {
                    etTitle.setText(existingEntry.getTitle());
                    etUsername.setText(existingEntry.getUsername());
                    try {
                        String decrypted = CryptoManager.getInstance()
                                .decrypt(existingEntry.getEncryptedPassword());
                        etPassword.setText(decrypted);
                    } catch (Exception e) {
                        // ignore
                    }
                    etWebsite.setText(existingEntry.getWebsite());
                    etNotes.setText(existingEntry.getNotes());
                    if (existingEntry.getCategory() != null) {
                        dropdownCategory.setText(existingEntry.getCategory(), false);
                    }
                });
            }
        });
    }

    private void showGeneratorDialog() {
        PasswordGeneratorDialog dialog = new PasswordGeneratorDialog();
        dialog.setOnPasswordSelectedListener(password -> {
            etPassword.setText(password);
        });
        dialog.show(getSupportFragmentManager(), "generator");
    }

    private void save() {
        String title = getText(etTitle);
        String username = getText(etUsername);
        String password = getText(etPassword);
        String website = getText(etWebsite);
        String notes = getText(etNotes);
        String category = dropdownCategory.getText().toString();

        // Validate
        tilTitle.setError(null);
        tilPassword.setError(null);

        if (title.isEmpty()) {
            tilTitle.setError(getString(R.string.error_title_required));
            return;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_password_field_required));
            return;
        }

        // Encrypt password
        String encrypted = CryptoManager.getInstance().encrypt(password);
        long now = System.currentTimeMillis();

        Executors.newSingleThreadExecutor().execute(() -> {
            if (editId != -1 && existingEntry != null) {
                // Update
                existingEntry.setTitle(title);
                existingEntry.setUsername(username);
                existingEntry.setEncryptedPassword(encrypted);
                existingEntry.setWebsite(website);
                existingEntry.setNotes(notes);
                existingEntry.setCategory(category);
                existingEntry.setUpdatedAt(now);
                dao.update(existingEntry);
            } else {
                // Insert
                PasswordEntry entry = new PasswordEntry();
                entry.setTitle(title);
                entry.setUsername(username);
                entry.setEncryptedPassword(encrypted);
                entry.setWebsite(website);
                entry.setNotes(notes);
                entry.setCategory(category);
                entry.setCreatedAt(now);
                entry.setUpdatedAt(now);
                dao.insert(entry);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}

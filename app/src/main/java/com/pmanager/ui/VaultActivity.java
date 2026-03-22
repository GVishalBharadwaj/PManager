package com.pmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pmanager.R;
import com.pmanager.data.AppDatabase;
import com.pmanager.data.PasswordDao;
import com.pmanager.data.PasswordEntry;
import com.pmanager.security.SessionManager;

import java.util.List;

/**
 * Main vault screen showing all saved passwords with search and category filtering.
 */
public class VaultActivity extends AppCompatActivity {

    private RecyclerView rvPasswords;
    private LinearLayout emptyState;
    private PasswordAdapter adapter;
    private PasswordDao dao;
    private TextInputEditText etSearch;
    private ChipGroup chipGroup;

    private String currentCategory = null; // null = All
    private LiveData<List<PasswordEntry>> currentLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        // Check session
        if (!SessionManager.getInstance(this).isSessionActive()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        dao = AppDatabase.getInstance(this).passwordDao();

        initViews();
        setupSearch();
        setupCategoryChips();
        loadPasswords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check session on resume (auto-lock)
        if (!SessionManager.getInstance(this).isSessionActive()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_vault);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        rvPasswords = findViewById(R.id.rv_passwords);
        emptyState = findViewById(R.id.empty_state);
        etSearch = findViewById(R.id.et_search);

        adapter = new PasswordAdapter();
        rvPasswords.setLayoutManager(new LinearLayoutManager(this));
        rvPasswords.setAdapter(adapter);

        adapter.setOnItemClickListener(entry -> {
            Intent intent = new Intent(this, PasswordDetailActivity.class);
            intent.putExtra("password_id", entry.getId());
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEditActivity.class));
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                loadPasswords();
            }
        });
    }

    private void setupCategoryChips() {
        chipGroup = findViewById(R.id.chip_group);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = null;
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_all) currentCategory = null;
                else if (checkedId == R.id.chip_social) currentCategory = "Social";
                else if (checkedId == R.id.chip_email) currentCategory = "Email";
                else if (checkedId == R.id.chip_finance) currentCategory = "Finance";
                else if (checkedId == R.id.chip_shopping) currentCategory = "Shopping";
                else if (checkedId == R.id.chip_work) currentCategory = "Work";
                else if (checkedId == R.id.chip_other) currentCategory = "Other";
            }
            loadPasswords();
        });
    }

    private void loadPasswords() {
        // Remove previous observer
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        String query = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";

        if (!query.isEmpty()) {
            currentLiveData = dao.searchPasswords(query);
        } else if (currentCategory != null) {
            currentLiveData = dao.getByCategory(currentCategory);
        } else {
            currentLiveData = dao.getAllPasswords();
        }

        currentLiveData.observe(this, passwords -> {
            adapter.setPasswords(passwords);
            emptyState.setVisibility(passwords.isEmpty() ? View.VISIBLE : View.GONE);
            rvPasswords.setVisibility(passwords.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    private boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_lock) {
            SessionManager.getInstance(this).lock();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }
}

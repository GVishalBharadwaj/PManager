package com.pmanager.autofill;

import android.app.assist.AssistStructure;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.pmanager.R;
import com.pmanager.data.AppDatabase;
import com.pmanager.data.PasswordDao;
import com.pmanager.data.PasswordEntry;
import com.pmanager.security.CryptoManager;

import java.util.List;

/**
 * Android Autofill Service that provides saved credentials to other apps.
 * Matches credentials by web domain or app package name.
 */
public class PManagerAutofillService extends AutofillService {

    @Override
    public void onFillRequest(@NonNull FillRequest request,
                               @NonNull CancellationSignal cancellationSignal,
                               @NonNull FillCallback callback) {

        // Get the latest AssistStructure
        List<FillContext> contexts = request.getFillContexts();
        AssistStructure structure = contexts.get(contexts.size() - 1).getStructure();

        // Parse the structure
        AutofillHelper.ParsedStructure parsed = AutofillHelper.parseStructure(structure);
        if (!parsed.hasAutofillableFields()) {
            callback.onSuccess(null);
            return;
        }

        // Find matching credentials
        String domain = parsed.webDomain != null ?
                AutofillHelper.extractDomain(parsed.webDomain) : "";
        String packageName = structure.getActivityComponent().getPackageName();

        PasswordDao dao = AppDatabase.getInstance(this).passwordDao();

        // Try matching by domain first, then by package name
        List<PasswordEntry> matches;
        if (!domain.isEmpty()) {
            matches = dao.getByWebsiteSync(domain);
        } else {
            matches = dao.getByWebsiteSync(packageName);
        }

        if (matches == null || matches.isEmpty()) {
            // No matches found — offer to save later
            AutofillHelper.AutofillFieldInfo usernameField = parsed.getUsernameField();
            AutofillHelper.AutofillFieldInfo passwordField = parsed.getPasswordField();

            if (usernameField != null && passwordField != null) {
                FillResponse.Builder responseBuilder = new FillResponse.Builder();
                responseBuilder.setSaveInfo(
                        new SaveInfo.Builder(
                                SaveInfo.SAVE_DATA_TYPE_USERNAME | SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                                new android.view.autofill.AutofillId[]{
                                        usernameField.autofillId,
                                        passwordField.autofillId
                                })
                                .build());
                callback.onSuccess(responseBuilder.build());
            } else {
                callback.onSuccess(null);
            }
            return;
        }

        // Build fill response with matching credentials
        FillResponse.Builder responseBuilder = new FillResponse.Builder();

        for (PasswordEntry entry : matches) {
            String decryptedPassword;
            try {
                decryptedPassword = CryptoManager.getInstance()
                        .decrypt(entry.getEncryptedPassword());
            } catch (Exception e) {
                continue;
            }

            Dataset.Builder datasetBuilder = new Dataset.Builder();

            // Create presentation view
            RemoteViews presentation = new RemoteViews(getPackageName(),
                    android.R.layout.simple_list_item_1);
            presentation.setTextViewText(android.R.id.text1,
                    entry.getTitle() + " (" + entry.getUsername() + ")");

            AutofillHelper.AutofillFieldInfo usernameField = parsed.getUsernameField();
            AutofillHelper.AutofillFieldInfo passwordField = parsed.getPasswordField();

            if (usernameField != null) {
                datasetBuilder.setValue(usernameField.autofillId,
                        AutofillValue.forText(entry.getUsername()), presentation);
            }
            if (passwordField != null) {
                datasetBuilder.setValue(passwordField.autofillId,
                        AutofillValue.forText(decryptedPassword), presentation);
            }

            try {
                responseBuilder.addDataset(datasetBuilder.build());
            } catch (Exception e) {
                // Skip malformed datasets
            }
        }

        try {
            callback.onSuccess(responseBuilder.build());
        } catch (Exception e) {
            callback.onSuccess(null);
        }
    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest request,
                               @NonNull SaveCallback callback) {
        // Get the structure
        List<FillContext> contexts = request.getFillContexts();
        AssistStructure structure = contexts.get(contexts.size() - 1).getStructure();

        AutofillHelper.ParsedStructure parsed = AutofillHelper.parseStructure(structure);

        String username = "";
        String password = "";

        // Extract the filled values
        for (AutofillHelper.AutofillFieldInfo field : parsed.fields) {
            // We need to re-traverse to get the values
        }

        // For now, acknowledge the save request
        // Full implementation would extract values from the structure and
        // prompt the user to save in PManager
        callback.onSuccess();
    }
}

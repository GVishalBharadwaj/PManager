package com.pmanager.autofill;

import android.app.assist.AssistStructure;
import android.text.TextUtils;
import android.view.View;
import android.view.autofill.AutofillId;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to traverse the AssistStructure and identify
 * username/password fields for autofill.
 */
public class AutofillHelper {

    public static class AutofillFieldInfo {
        public AutofillId autofillId;
        public String[] hints;
        public String webDomain;
        public boolean isPassword;
        public boolean isUsername;
    }

    public static class ParsedStructure {
        public List<AutofillFieldInfo> fields = new ArrayList<>();
        public String webDomain;
        public String packageName;

        public AutofillFieldInfo getUsernameField() {
            for (AutofillFieldInfo f : fields) {
                if (f.isUsername) return f;
            }
            return null;
        }

        public AutofillFieldInfo getPasswordField() {
            for (AutofillFieldInfo f : fields) {
                if (f.isPassword) return f;
            }
            return null;
        }

        public boolean hasAutofillableFields() {
            return getUsernameField() != null || getPasswordField() != null;
        }
    }

    /**
     * Parses an AssistStructure to find autofillable fields.
     */
    public static ParsedStructure parseStructure(AssistStructure structure) {
        ParsedStructure result = new ParsedStructure();

        for (int i = 0; i < structure.getWindowNodeCount(); i++) {
            AssistStructure.WindowNode windowNode = structure.getWindowNodeAt(i);
            AssistStructure.ViewNode rootNode = windowNode.getRootViewNode();
            traverseNode(rootNode, result);
        }

        return result;
    }

    private static void traverseNode(AssistStructure.ViewNode node, ParsedStructure result) {
        if (node == null) return;

        // Check for web domain
        String webDomain = node.getWebDomain();
        if (!TextUtils.isEmpty(webDomain)) {
            result.webDomain = webDomain;
        }

        // Check if this node has autofill hints or is an editable text field
        AutofillId autofillId = node.getAutofillId();
        String[] hints = node.getAutofillHints();
        int inputType = node.getInputType();

        if (autofillId != null) {
            AutofillFieldInfo field = null;

            // Check autofill hints first (preferred)
            if (hints != null && hints.length > 0) {
                field = new AutofillFieldInfo();
                field.autofillId = autofillId;
                field.hints = hints;
                field.webDomain = webDomain;

                for (String hint : hints) {
                    if (isPasswordHint(hint)) {
                        field.isPassword = true;
                    } else if (isUsernameHint(hint)) {
                        field.isUsername = true;
                    }
                }
            }
            // Fallback: check input type or ID hints
            else if (inputType != 0) {
                String idEntry = node.getIdEntry();
                String hintText = node.getHint() != null ? node.getHint().toString() : "";
                String combined = (idEntry != null ? idEntry : "") + " " + hintText;
                combined = combined.toLowerCase();

                if (isPasswordInputType(inputType) || combined.contains("password")
                        || combined.contains("pass")) {
                    field = new AutofillFieldInfo();
                    field.autofillId = autofillId;
                    field.isPassword = true;
                } else if (combined.contains("user") || combined.contains("email")
                        || combined.contains("login") || combined.contains("account")) {
                    field = new AutofillFieldInfo();
                    field.autofillId = autofillId;
                    field.isUsername = true;
                }
            }

            if (field != null) {
                result.fields.add(field);
            }
        }

        // Recurse into children
        for (int i = 0; i < node.getChildCount(); i++) {
            traverseNode(node.getChildAt(i), result);
        }
    }

    private static boolean isPasswordHint(String hint) {
        return View.AUTOFILL_HINT_PASSWORD.equalsIgnoreCase(hint);
    }

    private static boolean isUsernameHint(String hint) {
        return View.AUTOFILL_HINT_USERNAME.equalsIgnoreCase(hint)
                || View.AUTOFILL_HINT_EMAIL_ADDRESS.equalsIgnoreCase(hint)
                || "email".equalsIgnoreCase(hint);
    }

    private static boolean isPasswordInputType(int inputType) {
        int variation = inputType & android.text.InputType.TYPE_MASK_VARIATION;
        return variation == android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                || variation == android.text.InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                || variation == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    }

    /**
     * Extracts a domain for matching from a URL or web domain string.
     */
    public static String extractDomain(String input) {
        if (input == null) return "";
        // Remove protocol
        input = input.replaceFirst("https?://", "");
        // Remove path
        int slashIndex = input.indexOf('/');
        if (slashIndex > 0) input = input.substring(0, slashIndex);
        // Remove www.
        if (input.startsWith("www.")) input = input.substring(4);
        return input.toLowerCase();
    }
}

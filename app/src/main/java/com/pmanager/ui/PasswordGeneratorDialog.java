package com.pmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pmanager.R;
import com.pmanager.util.PasswordGenerator;
import com.pmanager.util.PasswordStrengthChecker;
import com.pmanager.util.PasswordStrengthChecker.Strength;

/**
 * Bottom sheet dialog for generating random passwords.
 */
public class PasswordGeneratorDialog extends BottomSheetDialogFragment {

    public interface OnPasswordSelectedListener {
        void onPasswordSelected(String password);
    }

    private OnPasswordSelectedListener listener;
    private TextView tvGenerated, tvLength, tvStrength;
    private ProgressBar strengthBar;
    private Slider sliderLength;
    private SwitchMaterial switchUpper, switchLower, switchDigits, switchSpecial;

    private String generatedPassword = "";

    public void setOnPasswordSelectedListener(OnPasswordSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.Theme_Design_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_password_generator, container, false);

        tvGenerated = view.findViewById(R.id.tv_generated);
        tvLength = view.findViewById(R.id.tv_length);
        tvStrength = view.findViewById(R.id.tv_strength);
        strengthBar = view.findViewById(R.id.strength_bar);
        sliderLength = view.findViewById(R.id.slider_length);
        switchUpper = view.findViewById(R.id.switch_upper);
        switchLower = view.findViewById(R.id.switch_lower);
        switchDigits = view.findViewById(R.id.switch_digits);
        switchSpecial = view.findViewById(R.id.switch_special);

        MaterialButton btnRegenerate = view.findViewById(R.id.btn_regenerate);
        MaterialButton btnUse = view.findViewById(R.id.btn_use);

        // Slider change
        sliderLength.addOnChangeListener((slider, value, fromUser) -> {
            tvLength.setText(getString(R.string.label_length, (int) value));
            generatePassword();
        });

        // Switch changes
        SwitchMaterial[] switches = {switchUpper, switchLower, switchDigits, switchSpecial};
        for (SwitchMaterial sw : switches) {
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> generatePassword());
        }

        btnRegenerate.setOnClickListener(v -> generatePassword());
        btnUse.setOnClickListener(v -> {
            if (listener != null && !generatedPassword.isEmpty()) {
                listener.onPasswordSelected(generatedPassword);
            }
            dismiss();
        });

        // Initial generation
        tvLength.setText(getString(R.string.label_length, 16));
        generatePassword();

        return view;
    }

    private void generatePassword() {
        int length = (int) sliderLength.getValue();
        boolean upper = switchUpper.isChecked();
        boolean lower = switchLower.isChecked();
        boolean digits = switchDigits.isChecked();
        boolean special = switchSpecial.isChecked();

        generatedPassword = PasswordGenerator.generate(length, upper, lower, digits, special);
        tvGenerated.setText(generatedPassword);

        // Update strength
        Strength strength = PasswordStrengthChecker.checkStrength(generatedPassword);
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
        int color = ContextCompat.getColor(requireContext(), colorRes);
        strengthBar.setProgressTintList(android.content.res.ColorStateList.valueOf(color));
        tvStrength.setText(label);
        tvStrength.setTextColor(color);
    }
}

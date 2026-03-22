package com.pmanager.util;

/**
 * Evaluates password strength based on length, character variety, and common patterns.
 */
public class PasswordStrengthChecker {

    public enum Strength {
        WEAK(0), FAIR(1), GOOD(2), STRONG(3);

        public final int level;
        Strength(int level) { this.level = level; }
    }

    /**
     * Returns the strength of a password.
     */
    public static Strength checkStrength(String password) {
        if (password == null || password.isEmpty()) return Strength.WEAK;

        int score = 0;
        int length = password.length();

        // Length scoring
        if (length >= 8) score++;
        if (length >= 12) score++;
        if (length >= 16) score++;

        // Character variety
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        int variety = 0;
        if (hasUpper) variety++;
        if (hasLower) variety++;
        if (hasDigit) variety++;
        if (hasSpecial) variety++;

        score += variety;

        // Penalty for common patterns
        String lower = password.toLowerCase();
        if (lower.contains("password") || lower.contains("123456") ||
                lower.contains("qwerty") || lower.contains("abc123")) {
            score -= 2;
        }

        // Penalty for all same character
        if (password.chars().distinct().count() <= 2) {
            score -= 2;
        }

        // Map score to strength
        if (score <= 2) return Strength.WEAK;
        if (score <= 4) return Strength.FAIR;
        if (score <= 5) return Strength.GOOD;
        return Strength.STRONG;
    }

    /**
     * Returns a visual progress value (0.0 to 1.0) for the strength bar.
     */
    public static float getStrengthProgress(Strength strength) {
        switch (strength) {
            case WEAK: return 0.25f;
            case FAIR: return 0.50f;
            case GOOD: return 0.75f;
            case STRONG: return 1.0f;
            default: return 0f;
        }
    }
}

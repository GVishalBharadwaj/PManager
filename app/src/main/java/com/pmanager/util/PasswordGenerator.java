package com.pmanager.util;

import java.security.SecureRandom;

/**
 * Generates random passwords with configurable character sets.
 */
public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random password with the specified options.
     *
     * @param length    Password length (8 to 64)
     * @param useUpper  Include uppercase letters
     * @param useLower  Include lowercase letters
     * @param useDigits Include digits
     * @param useSpecial Include special characters
     * @return Generated password
     */
    public static String generate(int length, boolean useUpper, boolean useLower,
                                   boolean useDigits, boolean useSpecial) {
        StringBuilder charPool = new StringBuilder();
        if (useUpper) charPool.append(UPPERCASE);
        if (useLower) charPool.append(LOWERCASE);
        if (useDigits) charPool.append(DIGITS);
        if (useSpecial) charPool.append(SPECIAL);

        // Fallback to lowercase if nothing selected
        if (charPool.length() == 0) {
            charPool.append(LOWERCASE);
        }

        // Clamp length
        length = Math.max(8, Math.min(64, length));

        StringBuilder password = new StringBuilder(length);

        // Ensure at least one character from each selected set
        if (useUpper) password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        if (useLower) password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        if (useDigits) password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        if (useSpecial) password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill remaining length
        String pool = charPool.toString();
        while (password.length() < length) {
            password.append(pool.charAt(random.nextInt(pool.length())));
        }

        // Shuffle the password
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}

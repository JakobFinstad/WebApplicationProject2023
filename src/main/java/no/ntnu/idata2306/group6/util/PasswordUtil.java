package no.ntnu.idata2306.group6.util;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash a password.
     *
     * @param plainPassword plain text to encode
     * @return the encoded plain text
     */
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);

        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Checks a plain password and hashed password to find if they are the same.
     *
     * @param plainPassword the password that shall be checked
     * @param hashedPassword the hashed password to check for validation from
     * @return {@code TRUE} if the password is unhashed version of hashed password, else returns {@code FALSE}
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword,hashedPassword);
    }
}

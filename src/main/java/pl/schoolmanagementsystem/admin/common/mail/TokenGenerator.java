package pl.schoolmanagementsystem.admin.common.mail;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    private final static String pullOfCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateToken() {
        return RandomStringUtils.random(20, pullOfCharacters);
    }
}

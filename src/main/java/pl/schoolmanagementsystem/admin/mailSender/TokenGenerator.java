package pl.schoolmanagementsystem.admin.mailSender;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    private final static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateToken() {
        return RandomStringUtils.random(12, characters);
    }
}

package pl.schoolmanagementsystem.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    private final static String pullOfCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateToken() {
        return RandomStringUtils.random(20, pullOfCharacters);
    }
    //TODO tu można dodać np taką biblioteke de.huxhorn.sulky.ulid.ULID
}

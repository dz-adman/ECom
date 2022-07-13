package com.ad.ecom.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum EComUtil {
    INSTANCE;

    private final String REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

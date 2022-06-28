package com.ad.ecom.core.registration.service.impl;

import com.ad.ecom.core.registration.service.EmailValidatorService;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValidatorServiceImpl implements EmailValidatorService {

    private final String REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    @Override
    public boolean validate(String email) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

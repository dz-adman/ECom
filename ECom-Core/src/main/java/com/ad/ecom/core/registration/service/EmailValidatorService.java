package com.ad.ecom.core.registration.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailValidatorService {
    public boolean validate(String email);
}

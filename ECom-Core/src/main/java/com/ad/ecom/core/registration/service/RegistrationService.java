package com.ad.ecom.core.registration.service;

import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.registration.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
    public ResponseEntity<ResponseMessage> register(RegistrationRequest request);
    public ResponseEntity<String> verifyAccount(String token);
    public ResponseEntity<ResponseMessage> regenerateToken(RegistrationRequest request);
}

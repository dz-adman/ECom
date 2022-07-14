package com.ad.ecom.core.registration.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.registration.dto.RegistrationRequest;
import com.ad.ecom.core.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "/verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        return registrationService.verifyAccount(token);
    }

    @PostMapping(path = "/regentoken")
    public ResponseEntity<ResponseMessage> regenerateToken(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.regenerateToken(request);
    }
}

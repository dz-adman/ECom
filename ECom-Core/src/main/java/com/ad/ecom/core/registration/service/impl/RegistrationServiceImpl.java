package com.ad.ecom.core.registration.service.impl;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import com.ad.ecom.core.ecomuser.repository.EcomUserRepository;
import com.ad.ecom.core.registration.dto.RegistrationRequest;
import com.ad.ecom.core.registration.persistance.VerificationToken;
import com.ad.ecom.core.registration.repository.VerificationTokenRepository;
import com.ad.ecom.core.registration.service.EmailValidatorService;
import com.ad.ecom.core.registration.service.RegistrationService;
import com.ad.ecom.core.registration.util.VerificationEmailEvent;
import com.ad.ecom.core.registration.util.WebTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${serverUrl}:${server.port}")
    private String serverUrl;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private EcomUserRepository ecomUserRepository;
    @Autowired
    private EmailValidatorService emailValidatorService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private EcomUserRepository userRepository;

    @Override
    public ResponseEntity<ResponseMessage> register(RegistrationRequest request) {
        boolean validEmail = emailValidatorService.validate(request.getEmail());
        if(!validEmail)
            throw new IllegalStateException("Email Not Valid");
        ResponseMessage respMsg = new ResponseMessage();
        boolean userExists = ecomUserRepository.existsByLoginIdAndEmailAndDeletedFalse(request.getLoginId(), request.getEmail());
        if(!userExists) {
            try {
                EcomUser user = new EcomUser(request.getFirstName(), request.getLastName(), request.getLoginId(), request.getEmail(),
                                             bCryptPasswordEncoder.encode(request.getPassword()), request.getRole(), true, false, false);
                ecomUserRepository.save(user);
                // send confirmation link email
                this.sendConfirmationMail(user);
                String respBody = "User registration successful [loginId: " + user.getLoginId() + "]. Please check your email for account activation.";
                respMsg.addResponse(ResponseType.SUCCESS, respBody);
                return new ResponseEntity(respMsg, HttpStatus.CREATED);
            } catch (Exception ex) {
                respMsg.addResponse(ResponseType.ERROR, "Insufficient Data!");
                return new ResponseEntity(respMsg, HttpStatus.BAD_REQUEST);
            }
        }
        respMsg.addResponse(ResponseType.ERROR, "Email or loginId already exists!");
        return new ResponseEntity(respMsg, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> verifyAccount(String token) {
        boolean verified = false, alreadyUsed = false;
        if(token != null && !token.isBlank() && !token.isEmpty()) {
            Optional<VerificationToken> tokenObject = tokenRepository.findByToken(token);
            if(tokenObject.isPresent()) {
                // if valid token
                if(tokenObject.get().getToken().equals(token)) {
                    // if token already used
                    if(tokenObject.get().isUsed()) alreadyUsed = true;
                    // if token not used already
                    else {
                        verified = true;
                        tokenObject.get().setUsed(true);
                        tokenRepository.save(tokenObject.get());
                        EcomUser user = tokenObject.get().getUser();
                        user.setEnabled(true);
                        user.setLocked(false);
                        userRepository.save(user);
                    }
                }
            }
        }
        if(alreadyUsed)
            return new ResponseEntity("<h2 style=\"color:red\">Token is already used</h2>", HttpStatus.ALREADY_REPORTED);
        if(verified) {
            String resp = WebTemplates.getAccountVerificationSuccessTemplate(serverUrl, contextPath);
            return new ResponseEntity(resp, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity("<h2 style=\"color:red\">Invalid token or request!</h2>", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendConfirmationMail(EcomUser user) {
        eventPublisher.publishEvent(new VerificationEmailEvent(this, user, serverUrl + contextPath + "/registration/verify"));
    }

    @Override
    public ResponseEntity<ResponseMessage> regenerateToken(RegistrationRequest request) {
        ResponseMessage respMsg = new ResponseMessage();
        if(Optional.ofNullable(request).isPresent() && (request.getEmail() != null || request.getLoginId() != null)) {
            Optional<EcomUser> user = userRepository.findByLoginIdOrEmailAndDeletedFalse(request.getLoginId(), request.getEmail());
            if(user.isPresent()) {
                if(!user.get().getEnabled()) {
                    sendConfirmationMail(user.get());
                    respMsg.addResponse(ResponseType.SUCCESS, "Account activation token regenerated successfully. Please check your email for account activation.");
                    return new ResponseEntity(respMsg, HttpStatus.CREATED);
                } else {
                    respMsg.addResponse(ResponseType.SUCCESS, "Account is already active.");
                    return new ResponseEntity(respMsg, HttpStatus.BAD_REQUEST);
                }
            }
            respMsg.addResponse(ResponseType.ERROR, "No account matched with this loginId or email!");
            return new ResponseEntity(respMsg, HttpStatus.NOT_FOUND);
        }
        respMsg.addResponse(ResponseType.ERROR, "Insufficient Data!");
        return new ResponseEntity(respMsg, HttpStatus.BAD_REQUEST);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private synchronized void flushExpiredTokens() {
        Date dt = new Date(Calendar.getInstance().getTime().getTime());
        Optional<List<VerificationToken>> tokens = tokenRepository.getExpiredTokens(dt);
        if(tokens.isPresent()) {
            tokens.get().stream()
                    .forEach(t -> tokenRepository.delete(t));
        }
    }
}

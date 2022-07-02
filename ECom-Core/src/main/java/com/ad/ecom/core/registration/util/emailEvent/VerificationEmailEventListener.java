package com.ad.ecom.core.registration.util.emailEvent;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import com.ad.ecom.core.registration.persistance.VerificationToken;
import com.ad.ecom.core.registration.repository.VerificationTokenRepository;
import com.ad.ecom.core.registration.util.WebTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class VerificationEmailEventListener {

    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Async
    // @EventListener automatically register an ApplicationListener matching the arguments of the method
    @EventListener
    public Boolean confirmRegistration(VerificationEmailEvent event) {
        EcomUser user = event.getUser();
        String token = UUID.randomUUID().toString();
        if(!user.isEnabled()) {
            Optional<VerificationToken> tokenEntry = tokenRepository.findByUser(user);
            if (tokenEntry.isPresent()) {
                tokenEntry.get().setToken(token);
                tokenEntry.get().setGeneratedOn(new Date(Calendar.getInstance().getTime().getTime()));
                tokenEntry.get().setExpiresOn(tokenEntry.get().calculateExpiryDate(15));
                tokenRepository.save(tokenEntry.get());
            } else {
                VerificationToken verificationToken = new VerificationToken();
                verificationToken.setToken(token);
                verificationToken.setGeneratedOn(new Date(Calendar.getInstance().getTime().getTime()));
                verificationToken.setExpiresOn(verificationToken.calculateExpiryDate(15));
                verificationToken.setUsed(false);
                verificationToken.setUser(event.getUser());
                tokenRepository.save(verificationToken);
            }
        }
        final String URL = event.getUrl() + "/" + token;
        String message = WebTemplates.RegistrationConfirmationTemplate(user, URL);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject("ECOM - Account Activation");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

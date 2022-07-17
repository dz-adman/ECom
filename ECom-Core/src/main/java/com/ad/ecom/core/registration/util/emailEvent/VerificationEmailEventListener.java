package com.ad.ecom.core.registration.util.emailEvent;

import com.ad.ecom.core.util.WebTemplates;
import com.ad.ecom.ecomuser.persistence.EcomUser;
import com.ad.ecom.registration.persistence.VerificationToken;
import com.ad.ecom.registration.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Component
public class VerificationEmailEventListener {

    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    // @EventListener automatically register an ApplicationListener matching the arguments of the method
    @EventListener
    @Async
    public Boolean confirmRegistration(VerificationEmailEvent event) {
        EcomUser user = event.getUser();
        String token = UUID.randomUUID().toString();
        if(!user.isEnabled()) {
            Optional<VerificationToken> tokenEntry = tokenRepository.findByUser(user);
            if (tokenEntry.isPresent()) {
                tokenEntry.get().setToken(token);
                tokenEntry.get().setGeneratedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
                tokenEntry.get().setExpiresOn(tokenEntry.get().generateExpiryDate(15));
                tokenRepository.save(tokenEntry.get());
            } else {
                VerificationToken verificationToken = new VerificationToken();
                verificationToken.setToken(token);
                verificationToken.setGeneratedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
                verificationToken.setExpiresOn(verificationToken.generateExpiryDate(15));
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

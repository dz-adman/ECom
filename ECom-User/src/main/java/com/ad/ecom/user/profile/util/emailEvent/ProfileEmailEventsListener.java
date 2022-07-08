package com.ad.ecom.user.profile.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import com.ad.ecom.registratiom.persistance.VerificationToken;
import com.ad.ecom.registratiom.repository.VerificationTokenRepository;
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
import java.util.UUID;

@Component
public class ProfileEmailEventsListener {

    private static final String MSG_FORMAT = "Hi %s %s,<br/><br/>We have received an Account-Deletion request for your ECom account.\n" +
            "Please find your secure token below for confirmation.<br/>" +
            "<b>%s</b><br/><br/>Please change your password if not done by you.<br/><br/>" +
            "<b>ECOM Team</b>";
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    // @EventListener automatically register an ApplicationListener matching the arguments of the method
    @EventListener
    @Async
    public Boolean accountDelTokenGenAndMailSend(AccountDelTokenEmailEvent event) {
        EcomUser user = event.getUser();
        // Generate and save new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setGeneratedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
        verificationToken.setExpiresOn(verificationToken.generateExpiryDate(15));
        verificationToken.setUsed(false);
        verificationToken.setUser(event.getUser());
        tokenRepository.save(verificationToken);

        String message =  String.format(MSG_FORMAT, user.getFirstName(), user.getLastName(), token);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject("ECOM - Account-Deletion Token");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

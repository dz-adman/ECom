package com.ad.ecom.user.profile.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import com.ad.ecom.registration.persistance.VerificationToken;
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
import java.util.UUID;

@Component
public class ProfileEmailEventsListener {

    private static final String ACNT_DEL_TOKEN_FORMAT = "Hi %s %s,<br/><br/>We have received an <b>Account-Deletion</b> request for your ECom account.<br/>" +
            "Please find your secure token below for confirmation.<br/>" +
            "<b>%s</b><br/><br/>Please change your password if not done by you.<br/><br/>" +
            "<b>ECOM Team</b>";
    private static final String UPDT_PWD_TOKEN_FORMAT = "Hi %s %s,<br/><br/>We have received a <b>Password-Update</b> request for your ECom account.<br/>" +
            "Please find your secure token below for confirmation.<br/>" +
            "<b>%s</b><br/><br/>Please change your password if not done by you.<br/><br/>" +
            "<b>ECOM Team</b>";
    private static final String UPDT_PWD_FORMAT = "Hi %s %s,<br/><br/>Password for your ECom account has been updated successfully.<br/><br/>" +
            "<b>ECOM Team</b>";

    private static final String UPDT_EMAIL_ID_TOKEN_FORMAT = "Hi %s %s,<br/><br/>We have received a <b>EmailId-Update</b> request for your ECom account.<br/>" +
            "Please find your secure token below for confirmation.<br/>" +
            "<b>%s</b><br/><br/>Please change your password if not done by you.<br/><br/>" +
            "<b>ECOM Team</b>";
    private static final String UPDT_EMAIL_ID_FORMAT = "Hi %s %s,<br/><br/>EmailId for your ECom account has been updated to <b>%s</b> successfully.<br/><br/>" +
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

        String message =  String.format(ACNT_DEL_TOKEN_FORMAT, user.getFirstName(), user.getLastName(), token);
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

    @EventListener
    @Async
    public Boolean updatePwdTokenGenAndMailSend(UpdatePwdTokenEmailEvent event) {
        EcomUser user = event.getUser();
        // Generate and save new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setGeneratedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
        verificationToken.setExpiresOn(verificationToken.generateExpiryDate(15));
        verificationToken.setUsed(false);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);

        String message =  String.format(UPDT_PWD_TOKEN_FORMAT, user.getFirstName(), user.getLastName(), token);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject("ECOM - Password-Update Token");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @EventListener
    @Async
    public Boolean updatePwdSuccessMailSend(UpdatePwdEmailEvent event) {
        EcomUser user = event.getUser();
        String message =  String.format(UPDT_PWD_FORMAT, user.getFirstName(), user.getLastName());
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject("ECOM - Password-Update Successful");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @EventListener
    @Async
    public Boolean updateEmailIdTokenGenAndMailSend(UpdateEmailIdTokenEmailEvent event) {
        EcomUser user = event.getUser();
        // Generate and save new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setGeneratedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
        verificationToken.setExpiresOn(verificationToken.generateExpiryDate(15));
        verificationToken.setUsed(false);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);

        String message =  String.format(UPDT_EMAIL_ID_TOKEN_FORMAT, user.getFirstName(), user.getLastName(), token);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject("ECOM - Email-Update Token");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @EventListener
    @Async
    public Boolean updateEmailIdSuccessMailSend(UpdateEmailIdEmailEvent event) {
        String message =  String.format(UPDT_EMAIL_ID_FORMAT, event.getFirstName(), event.getLastName(), event.getNewEmailId());
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("support@ecom.com"));
            helper.setTo(new InternetAddress(event.getOldEmailId()));
            helper.setSubject("ECOM - Email-Update Successful");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

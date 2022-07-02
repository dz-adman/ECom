package com.ad.ecom.util.emailEvent;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import com.ad.ecom.core.registration.util.WebTemplates;
import com.ad.ecom.orders.stubs.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class OrderStatusUpdateEmailEventListener {

    @Value("${serverUrl}:${server.port}")
    private String serverUrl;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private JavaMailSender mailSender;

    private static final String EMAIL_SUBJECT = "ECOM - Status Update for your Order[order id : %s]";

    @Async
    // @EventListener automatically register an ApplicationListener matching the arguments of the method
    @EventListener
    public Boolean sendNotificationMail(OrderStatusUpdateEmailEvent event) {
        EcomUser user = event.getUser();
        long orderId = event.getOrderId();
        OrderStatus orderStatus = event.getOrderStatus();
        String body = WebTemplates.OrderStatusUpdateTemplate(user, orderId, orderStatus, serverUrl+contextPath+"/login");
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(new InternetAddress("admin@ecom.com"));
            helper.setTo(new InternetAddress(user.getEmail()));
            helper.setSubject(String.format(EMAIL_SUBJECT, orderId));
            helper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

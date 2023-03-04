package com.ad.ecom.core.registration.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EComUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VerificationEmailEvent extends ApplicationEvent {
    private EComUser user;
    private String url;

    public VerificationEmailEvent(Object source, EComUser user, String url) {
        super(source);
        this.user = user;
        this.url = url;
    }
}
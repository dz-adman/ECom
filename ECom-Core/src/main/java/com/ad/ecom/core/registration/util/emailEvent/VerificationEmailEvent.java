package com.ad.ecom.core.registration.util.emailEvent;

import com.ad.ecom.ecomuser.persistence.EcomUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VerificationEmailEvent extends ApplicationEvent {
    private EcomUser user;
    private String url;

    public VerificationEmailEvent(Object source, EcomUser user, String url) {
        super(source);
        this.user = user;
        this.url = url;
    }
}